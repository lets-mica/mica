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

package net.dreamlu.mica.redis;

import net.dreamlu.mica.redis.ser.RedisKeySerializer;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * redis 工具类
 *
 * @author L.cm
 */
public class RedisKit {

	private static RedisKeySerializer keySerializer = new RedisKeySerializer();

	/**
	 * 读取缓存，读取不到时 从数据库读取
	 * @param template RedisTemplate
	 * @param cacheKey cache key
	 * @param loader 加载器
	 * @param <T> 泛型标记
	 * @return 缓存对象
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T get(final RedisTemplate<String, Object> template,
							 final CacheKey cacheKey,
							 final Supplier<T> loader) {
		final byte[] keyBytes = keySerializer.serialize(cacheKey.getKey());
		return template.execute((RedisCallback<T>) connection -> {
			RedisSerializer serializer = template.getValueSerializer();
			byte[] valueByte = connection.get(keyBytes);
			if (valueByte == null) {
				T value = loader.get();
				valueByte = serializer.serialize(loader.get());
				Long seconds = cacheKey.getSeconds();
				if (seconds == null) {
					connection.set(keyBytes, valueByte);
				} else {
					connection.setEx(keyBytes, seconds, valueByte);
				}
				return value;
			}
			return (T) serializer.deserialize(valueByte);
		});
	}

	/**
	 * 累加
	 * @param template RedisTemplate
	 * @param cacheKey 缓存 key
	 * @return Long
	 */
	@Nullable
	public static Long incr(final RedisTemplate<String, Object> template, final CacheKey cacheKey) {
		final byte[] keyBytes = keySerializer.serialize(cacheKey.getKey());
		return template.execute((RedisCallback<Long>) connection -> connection.incr(keyBytes));
	}

	/**
	 * 递减
	 * @param template RedisTemplate
	 * @param cacheKey 缓存 key
	 * @return Long
	 */
	@Nullable
	public static Long decr(final RedisTemplate<String, Object> template, final CacheKey cacheKey) {
		final byte[] keyBytes = keySerializer.serialize(cacheKey.getKey());
		return template.execute((RedisCallback<Long>) connection -> connection.decr(keyBytes));
	}

	/**
	 * 获取数量 用于点赞 收藏类的
	 * @param template RedisTemplate
	 * @param cacheKey redis key
	 * @param loader 加载器
	 * @return 数量
	 */
	public static Long count(final RedisTemplate<String, Object> template,
							 final CacheKey cacheKey,
							 final Supplier<Long> loader) {
		final byte[] keyBytes = keySerializer.serialize(cacheKey.getKey());
		// 读取数量
		Long count = template.execute((RedisCallback<Long>) connection -> {
			byte[] value = connection.get(keyBytes);
			// 如果不存在则 从 数据库读取
			if (value == null) {
				return connection.incrBy(keyBytes, loader.get());
			}
			String values = new String(value);
			return Long.valueOf(values);
		});
		return count == null ? 0L : count;
	}

}
