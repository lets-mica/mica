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

package net.dreamlu.mica.redis.config;

import net.dreamlu.mica.redis.stream.DefaultRStreamTemplate;
import net.dreamlu.mica.redis.stream.RStreamListenerDetector;
import net.dreamlu.mica.redis.stream.RStreamListenerLazyFilter;
import net.dreamlu.mica.redis.stream.RStreamTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions;
import org.springframework.util.ErrorHandler;

import java.time.Duration;

/**
 * redis Stream 配置
 *
 * @author L.cm
 */
@AutoConfiguration
@ConditionalOnProperty(
	prefix = MicaRedisProperties.Stream.PREFIX,
	name = "enable",
	havingValue = "true"
)
public class RedisStreamConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public StreamMessageListenerContainerOptions<String, MapRecord<String, String, byte[]>> streamMessageListenerContainerOptions(MicaRedisProperties properties,
																																  ObjectProvider<ErrorHandler> errorHandlerObjectProvider) {
		StreamMessageListenerContainer.StreamMessageListenerContainerOptionsBuilder<String, MapRecord<String, String, byte[]>> builder = StreamMessageListenerContainerOptions
			.builder()
			.keySerializer(RedisSerializer.string())
			.hashKeySerializer(RedisSerializer.string())
			.hashValueSerializer(RedisSerializer.byteArray());
		MicaRedisProperties.Stream streamProperties = properties.getStream();
		// 批量大小
		Integer pollBatchSize = streamProperties.getPollBatchSize();
		if (pollBatchSize != null && pollBatchSize > 0) {
			builder.batchSize(pollBatchSize);
		}
		// poll 超时时间
		Duration pollTimeout = streamProperties.getPollTimeout();
		if (pollTimeout != null && !pollTimeout.isNegative()) {
			builder.pollTimeout(pollTimeout);
		}
		// errorHandler
		errorHandlerObjectProvider.ifAvailable((builder::errorHandler));
		// TODO L.cm executor
		return builder.build();
	}

	@Bean
	@ConditionalOnMissingBean
	public StreamMessageListenerContainer<String, MapRecord<String, String, byte[]>> streamMessageListenerContainer(RedisConnectionFactory redisConnectionFactory,
																													StreamMessageListenerContainerOptions<String, MapRecord<String, String, byte[]>> streamMessageListenerContainerOptions) {
		// 根据配置对象创建监听容器
		return StreamMessageListenerContainer.create(redisConnectionFactory, streamMessageListenerContainerOptions);
	}

	@Bean
	@ConditionalOnMissingBean
	public static RStreamListenerDetector streamListenerDetector(ApplicationContext applicationContext) {
		return new RStreamListenerDetector(applicationContext);
	}

	@Bean
	public RStreamListenerLazyFilter streamListenerLazyFilter() {
		return new RStreamListenerLazyFilter();
	}

	@Bean
	public RStreamTemplate streamTemplate(RedisTemplate<String, Object> redisTemplate) {
		return new DefaultRStreamTemplate(redisTemplate);
	}

}
