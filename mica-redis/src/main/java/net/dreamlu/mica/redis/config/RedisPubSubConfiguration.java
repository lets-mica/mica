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

import net.dreamlu.mica.redis.cache.MicaRedisCache;
import net.dreamlu.mica.redis.pubsub.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redisson pub/sub 发布配置
 *
 * @author L.cm
 */
@AutoConfiguration(after = RedisTemplateConfiguration.class)
public class RedisPubSubConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
																	   ObjectProvider<RPubSubMessageListener> customizers) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		customizers.orderedStream().forEach(listener -> {
			listener.getTopics().forEach(topic -> {
				container.addMessageListener(listener, topic);
			});
		});
		return container;
	}

	@Bean
	public RPubSubPublisher topicEventPublisher(MicaRedisCache redisCache,
												RedisSerializer<Object> redisSerializer) {
		return new RedisPubSubPublisher(redisCache, redisSerializer);
	}

	@Bean
	@ConditionalOnBean(RedisSerializer.class)
	public static RPubSubListenerDetector topicListenerDetector(ApplicationContext applicationContext) {
		return new RPubSubListenerDetector(applicationContext);
	}

	@Bean
	public RPubSubListenerLazyFilter pubSubListenerLazyFilter() {
		return new RPubSubListenerLazyFilter();
	}

}
