/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dreamlu.mica.redis.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.constant.MicaConstant;
import net.dreamlu.mica.core.utils.CharPool;
import net.dreamlu.mica.core.utils.INetUtil;
import net.dreamlu.mica.core.utils.ReflectUtil;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.redis.config.MicaRedisProperties;
import net.dreamlu.mica.redis.config.RedisTemplateConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;

/**
 * redis 监听器
 *
 * @author L.cm
 */
@Slf4j
@RequiredArgsConstructor
public class RStreamListenerDetector implements BeanPostProcessor, SmartInitializingSingleton {
	// redis 重连等会触发异常，异常时不取消订阅
	private static final Predicate<Throwable> CANCEL_SUBSCRIPTION_ON_ERROR = t -> false;
	private final ApplicationContext applicationContext;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<?> userClass = ClassUtils.getUserClass(bean);
		ReflectionUtils.doWithMethods(userClass, method -> {
			RStreamListener listener = AnnotationUtils.findAnnotation(method, RStreamListener.class);
			if (listener != null) {
				String streamKey = listener.name();
				Assert.hasText(streamKey, "@RStreamListener name must not be empty.");
				log.info("Found @RStreamListener on bean:{} method:{}", beanName, method);
				// 校验 method，method 入参数大于等于1
				int paramCount = method.getParameterCount();
				if (paramCount > 1) {
					throw new IllegalArgumentException("@RStreamListener on method " + method + " parameter count must less or equal to 1.");
				}
				// streamOffset
				ReadOffset readOffset = listener.offsetModel().getReadOffset();
				StreamOffset<String> streamOffset = StreamOffset.create(streamKey, readOffset);
				// 消费模式
				MessageModel messageModel = listener.messageModel();
				if (MessageModel.BROADCASTING == messageModel) {
					broadCast(streamOffset, bean, method, listener.readRawBytes());
				} else {
					String consumerGroup = getConsumerGroup();
					String groupId = StringUtil.isNotBlank(listener.group()) ? listener.group() : consumerGroup;
					String consumerName = getConsumerName();
					Consumer consumer = Consumer.from(groupId, consumerName);
					// 如果需要，创建 group
					createGroupIfNeed(streamKey, readOffset, groupId);
					cluster(consumer, streamOffset, listener, bean, method);
				}
			}
		}, ReflectionUtils.USER_DECLARED_METHODS);
		return bean;
	}

	private void broadCast(StreamOffset<String> streamOffset, Object bean, Method method, boolean isReadRawBytes) {
		StreamMessageListenerContainer.StreamReadRequest<String> streamReadRequest = StreamMessageListenerContainer.StreamReadRequest
			.builder(streamOffset)
			// 重连会触发异常
			.cancelOnError(CANCEL_SUBSCRIPTION_ON_ERROR)
			.build();
		getStreamMessageListenerContainer().register(streamReadRequest, (message) -> {
			// MapBackedRecord
			invokeMethod(getRedisTemplate(), bean, method, message, isReadRawBytes);
		});
	}

	private void cluster(Consumer consumer, StreamOffset<String> streamOffset, RStreamListener listener, Object bean, Method method) {
		boolean autoAcknowledge = listener.autoAcknowledge();
		StreamMessageListenerContainer.ConsumerStreamReadRequest<String> readRequest = StreamMessageListenerContainer.StreamReadRequest
			.builder(streamOffset)
			.consumer(consumer)
			.autoAcknowledge(autoAcknowledge)
			// 重连会触发异常
			.cancelOnError(CANCEL_SUBSCRIPTION_ON_ERROR)
			.build();
		RedisTemplate<String, Object> redisTemplate = getRedisTemplate();
		StreamOperations<String, Object, Object> opsForStream = redisTemplate.opsForStream();
		getStreamMessageListenerContainer().register(readRequest, (message) -> {
			// MapBackedRecord
			invokeMethod(redisTemplate, bean, method, message, listener.readRawBytes());
			// ack
			if (!autoAcknowledge) {
				opsForStream.acknowledge(consumer.getGroup(), message);
			}
		});
	}

	private void createGroupIfNeed(String streamKey, ReadOffset readOffset, String group) {
		RedisTemplate<String, Object> redisTemplate = getRedisTemplate();
		StreamOperations<String, Object, Object> opsForStream = redisTemplate.opsForStream();
		try {
			StreamInfo.XInfoGroups groups = opsForStream.groups(streamKey);
			if (groups.stream().noneMatch((x) -> group.equals(x.groupName()))) {
				opsForStream.createGroup(streamKey, readOffset, group);
			}
		} catch (RedisSystemException e) {
			// RedisCommandExecutionException: ERR no such key
			opsForStream.createGroup(streamKey, group);
		}
	}

	private void invokeMethod(RedisTemplate<String, Object> redisTemplate,
							  Object bean, Method method,
							  MapRecord<String, String, byte[]> mapRecord, boolean isReadRawBytes) {
		// 支持没有参数的方法
		if (method.getParameterCount() == 0) {
			ReflectUtil.invokeMethod(method, bean);
			return;
		}
		if (isReadRawBytes) {
			ReflectUtil.invokeMethod(method, bean, mapRecord);
		} else {
			ReflectUtil.invokeMethod(method, bean, getRecordValue(redisTemplate, mapRecord));
		}
	}

	private Object getRecordValue(RedisTemplate<String, Object> redisTemplate,
								  MapRecord<String, String, byte[]> mapRecord) {
		Map<String, byte[]> messageValue = mapRecord.getValue();
		if (messageValue.containsKey(RStreamTemplate.OBJECT_PAYLOAD_KEY)) {
			byte[] payloads = messageValue.get(RStreamTemplate.OBJECT_PAYLOAD_KEY);
			Object deserialize = redisTemplate.getValueSerializer().deserialize(payloads);
			return ObjectRecord.create(mapRecord.getStream(), deserialize).withId(mapRecord.getId());
		} else {
			return mapRecord.mapEntries(entry -> {
				String key = entry.getKey();
				Object value = redisTemplate.getValueSerializer().deserialize(entry.getValue());
				return Collections.singletonMap(key, value).entrySet().iterator().next();
			});
		}
	}

	private MicaRedisProperties getMicaRedisProperties() {
		return applicationContext.getBean(MicaRedisProperties.class);
	}

	private ObjectProvider<ServerProperties> getServerPropertiesObjectProvider() {
		return applicationContext.getBeanProvider(ServerProperties.class);
	}

	private RedisTemplate<String, Object> getRedisTemplate() {
		return applicationContext.getBean(RedisTemplateConfiguration.REDIS_TEMPLATE_BEAN_NAME, RedisTemplate.class);
	}

	private StreamMessageListenerContainer<String, MapRecord<String, String, byte[]>> getStreamMessageListenerContainer() {
		return applicationContext.getBean(StreamMessageListenerContainer.class);
	}

	/**
	 * 获取消费组名
	 *
	 * @return 消费组
	 */
	private String getConsumerGroup() {
		MicaRedisProperties properties = getMicaRedisProperties();
		MicaRedisProperties.Stream streamProperties = properties.getStream();
		// 消费组名称
		String consumerGroup = streamProperties.getConsumerGroup();
		Environment environment = applicationContext.getEnvironment();
		if (StringUtil.isBlank(consumerGroup)) {
			String appName = environment.getRequiredProperty(MicaConstant.SPRING_APP_NAME_KEY);
			String profile = environment.getProperty(MicaConstant.ACTIVE_PROFILES_PROPERTY);
			return StringUtil.isBlank(profile) ? appName : appName + CharPool.COLON + profile;
		}
		return consumerGroup;
	}

	private String getConsumerName() {
		MicaRedisProperties properties = getMicaRedisProperties();
		MicaRedisProperties.Stream streamProperties = properties.getStream();
		// 消费者名称
		ObjectProvider<ServerProperties> serverPropertiesObjectProvider = getServerPropertiesObjectProvider();
		String consumerName = streamProperties.getConsumerName();
		if (StringUtil.isBlank(consumerName)) {
			final StringBuilder consumerNameBuilder = new StringBuilder(INetUtil.getHostIp());
			serverPropertiesObjectProvider.ifAvailable(serverProperties -> {
				consumerNameBuilder.append(CharPool.COLON).append(serverProperties.getPort());
			});
			return consumerNameBuilder.toString();
		}
		return consumerName;
	}

	@Override
	public void afterSingletonsInstantiated() {
		// 启动 streamMessageListenerContainer
		getStreamMessageListenerContainer().start();
	}
}
