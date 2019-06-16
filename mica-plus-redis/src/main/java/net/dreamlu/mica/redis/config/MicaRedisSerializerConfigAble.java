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

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis 序列化
 *
 * @author L.cm
 */
public interface MicaRedisSerializerConfigAble {

	/**
	 * 序列化接口
	 *
	 * @param properties 配置
	 * @return RedisSerializer
	 */
	RedisSerializer<Object> redisSerializer(MicaRedisProperties properties);

	/**
	 * 默认的序列化方式
	 *
	 * @param properties 配置
	 * @return RedisSerializer
	 */
	default RedisSerializer<Object> defaultRedisSerializer(MicaRedisProperties properties) {
		MicaRedisProperties.SerializerType serializerType = properties.getSerializerType();
		if (MicaRedisProperties.SerializerType.JDK == serializerType) {
			return new JdkSerializationRedisSerializer();
		}
		return new GenericJackson2JsonRedisSerializer();
	}
}
