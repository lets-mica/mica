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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dreamlu.mica.core.utils.JsonUtil;
import net.dreamlu.mica.redis.cache.MicaRedisCache;
import net.dreamlu.mica.redis.resolver.DefaultRedisKeyResolver;
import net.dreamlu.mica.redis.resolver.RedisKeyResolver;
import net.dreamlu.mica.redis.resolver.RedisKeyResolverSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.KotlinDetector;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * RedisTemplate  配置
 *
 * @author L.cm
 */
@AutoConfiguration(after = RedisAutoConfiguration.class)
@EnableConfigurationProperties(MicaRedisProperties.class)
public class RedisTemplateConfiguration {
	public static final String REDIS_TEMPLATE_BEAN_NAME = "micaRedisTemplate";

	/**
	 * 默认的 redis key 处理
	 * @param properties MicaRedisProperties
	 * @return RedisKeyResolver
	 */
	@Bean
	@ConditionalOnMissingBean(RedisKeyResolver.class)
	public RedisKeyResolver redisKeyResolver(MicaRedisProperties properties) {
		return new DefaultRedisKeyResolver(properties);
	}

	/**
	 * value 值 序列化
	 *
	 * @return RedisSerializer
	 */
	@Bean
	@ConditionalOnMissingBean(RedisSerializer.class)
	public RedisSerializer<Object> redisSerializer(MicaRedisProperties properties) {
		MicaRedisProperties.SerializerType serializerType = properties.getSerializerType();
		if (MicaRedisProperties.SerializerType.JDK == serializerType) {
			ClassLoader classLoader = this.getClass().getClassLoader();
			return new JdkSerializationRedisSerializer(classLoader);
		}
		// jackson findAndRegisterModules，use copy
		ObjectMapper objectMapper = JsonUtil.getInstance().copy();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		// findAndRegisterModules
		objectMapper.findAndRegisterModules();
		// class type info to json
		GenericJackson2JsonRedisSerializer.registerNullValueSerializer(objectMapper, null);
		if (KotlinDetector.isKotlinPresent() && properties.isEnableKotlinJson()) {
			objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL_AND_ENUMS, As.PROPERTY);
		} else {
			objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, As.PROPERTY);
		}
		return new GenericJackson2JsonRedisSerializer(objectMapper);
	}

	@Bean
	@ConditionalOnMissingBean(name = REDIS_TEMPLATE_BEAN_NAME)
	@ConditionalOnSingleCandidate(RedisConnectionFactory.class)
	public RedisTemplate<String, Object> micaRedisTemplate(RedisConnectionFactory redisConnectionFactory,
														   RedisKeyResolver redisKeyResolver,
														   RedisSerializer<Object> redisSerializer) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		// key 序列化
		RedisKeyResolverSerializer keySerializer = new RedisKeyResolverSerializer(RedisSerializer.string(), redisKeyResolver);
		redisTemplate.setKeySerializer(keySerializer);
		redisTemplate.setHashKeySerializer(keySerializer);
		// value 序列化
		redisTemplate.setValueSerializer(redisSerializer);
		redisTemplate.setHashValueSerializer(redisSerializer);
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}

	@Bean
	@ConditionalOnMissingBean(ValueOperations.class)
	public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> micaRedisTemplate) {
		return micaRedisTemplate.opsForValue();
	}

	@Bean
	public MicaRedisCache micaRedisCache(RedisTemplate<String, Object> micaRedisTemplate) {
		return new MicaRedisCache(micaRedisTemplate);
	}
}
