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

package net.dreamlu.mica.redis.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * redis key 二次处理，例如：统一添加前缀
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class RedisKeyResolverSerializer implements RedisSerializer<String> {
	private final RedisSerializer<String> redisSerializer;
	private final RedisKeyResolver redisKeyResolver;

	@Override
	public byte[] serialize(String key) throws SerializationException {
		if (redisKeyResolver == null) {
			return redisSerializer.serialize(key);
		} else {
			return redisSerializer.serialize(redisKeyResolver.resolve(key));
		}
	}

	@Override
	public String deserialize(byte[] bytes) throws SerializationException {
		return redisSerializer.deserialize(bytes);
	}

}
