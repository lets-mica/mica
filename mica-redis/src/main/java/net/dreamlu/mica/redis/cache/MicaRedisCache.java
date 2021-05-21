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

package net.dreamlu.mica.redis.cache;

import lombok.Getter;
import net.dreamlu.mica.core.utils.CollectionUtil;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * redis 工具
 *
 * @author L.cm
 */
@Getter
@SuppressWarnings("unchecked")
public class MicaRedisCache {
	private final RedisTemplate<String, Object> redisTemplate;
	private final ValueOperations<String, Object> valueOps;
	private final HashOperations<String, Object, Object> hashOps;
	private final ListOperations<String, Object> listOps;
	private final SetOperations<String, Object> setOps;
	private final ZSetOperations<String, Object> zSetOps;

	public MicaRedisCache(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.valueOps = redisTemplate.opsForValue();
		this.hashOps = redisTemplate.opsForHash();
		this.listOps = redisTemplate.opsForList();
		this.setOps = redisTemplate.opsForSet();
		this.zSetOps = redisTemplate.opsForZSet();
	}

	/**
	 * 设置缓存
	 *
	 * @param cacheKey 缓存key
	 * @param value    缓存value
	 */
	public void set(CacheKey cacheKey, Object value) {
		String key = cacheKey.getKey();
		Duration expire = cacheKey.getExpire();
		if (expire == null) {
			set(key, value);
		} else {
			setEx(key, value, expire);
		}
	}

	/**
	 * 存放 key value 对到 redis。
	 */
	public void set(String key, Object value) {
		valueOps.set(key, value);
	}

	/**
	 * 存放 key value 对到 redis，并将 key 的生存时间设为 seconds (以秒为单位)。
	 * 如果 key 已经存在， SETEX 命令将覆写旧值。
	 */
	public void setEx(String key, Object value, Duration timeout) {
		valueOps.set(key, value, timeout);
	}

	/**
	 * 存放 key value 对到 redis，并将 key 的生存时间设为 seconds (以秒为单位)。
	 * 如果 key 已经存在， SETEX 命令将覆写旧值。
	 */
	public void setEx(String key, Object value, Long seconds) {
		valueOps.set(key, value, seconds, TimeUnit.SECONDS);
	}

	/**
	 * Set the {@code value} and expiration {@code timeout} for {@code key}.
	 *
	 * @param key     must not be {@literal null}.
	 * @param value   must not be {@literal null}.
	 * @param timeout the key expiration timeout.
	 * @param unit    must not be {@literal null}.
	 * @see <a href="https://redis.io/commands/setex">Redis Documentation: SETEX</a>
	 */
	public void setEx(String key, Object value, long timeout, TimeUnit unit) {
		valueOps.set(key, value, timeout, unit);
	}

	/**
	 * 返回 key 所关联的 value 值
	 * 如果 key 不存在那么返回特殊值 nil 。
	 */
	@Nullable
	public <T> T get(String key) {
		return (T) valueOps.get(key);
	}

	/**
	 * 获取cache 为 null 时使用加载器，然后设置缓存
	 *
	 * @param key    cacheKey
	 * @param loader cache loader
	 * @param <T>    泛型
	 * @return 结果
	 */
	@Nullable
	public <T> T get(String key, Supplier<T> loader) {
		T value = this.get(key);
		if (value != null) {
			return value;
		}
		value = loader.get();
		if (value == null) {
			return null;
		}
		this.set(key, value);
		return value;
	}

	/**
	 * 返回 key 所关联的 value 值
	 * 如果 key 不存在那么返回特殊值 nil 。
	 */
	@Nullable
	public <T> T get(CacheKey cacheKey) {
		return (T) valueOps.get(cacheKey.getKey());
	}

	/**
	 * 获取cache 为 null 时使用加载器，然后设置缓存
	 *
	 * @param cacheKey cacheKey
	 * @param loader   cache loader
	 * @param <T>      泛型
	 * @return 结果
	 */
	@Nullable
	public <T> T get(CacheKey cacheKey, Supplier<T> loader) {
		String key = cacheKey.getKey();
		T value = this.get(key);
		if (value != null) {
			return value;
		}
		value = loader.get();
		if (value == null) {
			return null;
		}
		this.set(cacheKey, value);
		return value;
	}

	/**
	 * 删除给定的一个 key
	 * 不存在的 key 会被忽略。
	 */
	public Boolean del(String key) {
		return redisTemplate.delete(key);
	}

	/**
	 * 删除给定的一个 key
	 * 不存在的 key 会被忽略。
	 */
	public Boolean del(CacheKey key) {
		return redisTemplate.delete(key.getKey());
	}

	/**
	 * 删除给定的多个 key
	 * 不存在的 key 会被忽略。
	 */
	public Long del(String... keys) {
		return del(Arrays.asList(keys));
	}

	/**
	 * 删除给定的多个 key
	 * 不存在的 key 会被忽略。
	 */
	public Long del(Collection<String> keys) {
		return redisTemplate.delete(keys);
	}

	/**
	 * 查找所有符合给定模式 pattern 的 key 。
	 * KEYS * 匹配数据库中所有 key 。
	 * KEYS h?llo 匹配 hello ， hallo 和 hxllo 等。
	 * KEYS h*llo 匹配 hllo 和 heeeeello 等。
	 * KEYS h[ae]llo 匹配 hello 和 hallo ，但不匹配 hillo 。
	 * 特殊符号用 \ 隔开
	 */
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	/**
	 * redis scan，count 默认 100
	 *
	 * @param pattern 匹配表达式
	 * @return 扫描结果
	 */
	public Set<String> scan(String pattern) {
		return scan(pattern, 100L);
	}

	/**
	 * redis scan
	 *
	 * @param pattern 匹配表达式
	 * @param count   一次扫描的数量, redis 默认为 10
	 * @return 扫描结果
	 */
	public Set<String> scan(String pattern, @Nullable Long count) {
		final Set<String> keySet = new HashSet<>();
		scan(pattern, count, keySet::add);
		return keySet;
	}

	/**
	 * redis scan, count 默认 100
	 *
	 * @param pattern  匹配表达式
	 * @param consumer 消费者
	 * @return 扫描结果
	 */
	public void scan(String pattern, Consumer<String> consumer) {
		scan(pattern, 100L, consumer);
	}

	/**
	 * redis scan
	 *
	 * @param pattern  匹配表达式
	 * @param count    一次扫描的数量
	 * @param consumer 消费者
	 * @return 扫描结果
	 */
	public void scan(String pattern, @Nullable Long count, Consumer<String> consumer) {
		RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
		scanBytes(pattern, count, (bytes) -> consumer.accept(keySerializer.deserialize(bytes)));
	}

	/**
	 * redis scan
	 *
	 * @param pattern  匹配表达式
	 * @param count    一次扫描的数量
	 * @param consumer 消费者
	 * @return 扫描结果
	 */
	public void scanBytes(String pattern, @Nullable Long count, Consumer<byte[]> consumer) {
		redisTemplate.execute((RedisCallback<Object>) action -> {
			ScanOptions.ScanOptionsBuilder builder = ScanOptions.scanOptions()
				.match(pattern);
			if (count != null) {
				builder.count(count);
			}
			try (Cursor<byte[]> cursor = action.scan(builder.build())) {
				cursor.forEachRemaining(consumer);
			}
			return null;
		});
	}

	/**
	 * redis sscan，count 默认 100
	 *
	 * @param key     key
	 * @param pattern 匹配表达式
	 * @return 扫描结果
	 */
	public Set<String> sScan(String key, String pattern) {
		return sScan(key, pattern, 100L);
	}

	/**
	 * redis sscan
	 *
	 * @param key     key
	 * @param pattern 匹配表达式
	 * @param count   一次扫描的数量
	 * @return 扫描结果
	 */
	public Set<String> sScan(String key, String pattern, @Nullable Long count) {
		final Set<String> keySet = new HashSet<>();
		sScan(key, pattern, count, keySet::add);
		return keySet;
	}

	/**
	 * redis sscan
	 *
	 * @param key      key
	 * @param pattern  匹配表达式
	 * @param consumer consumer
	 * @return 扫描结果
	 */
	public void sScan(String key, String pattern, Consumer<String> consumer) {
		sScan(key, pattern, 100L, consumer);
	}

	/**
	 * redis sscan
	 *
	 * @param key      key
	 * @param pattern  匹配表达式
	 * @param count    一次扫描的数量
	 * @param consumer consumer
	 * @return 扫描结果
	 */
	public void sScan(String key, String pattern, @Nullable Long count, Consumer<String> consumer) {
		RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
		sScanBytes(key, pattern, count, (bytes) -> consumer.accept(keySerializer.deserialize(bytes)));
	}

	/**
	 * redis sscan
	 *
	 * @param key      key
	 * @param pattern  匹配表达式
	 * @param count    一次扫描的数量
	 * @param consumer consumer
	 * @return 扫描结果
	 */
	public void sScanBytes(String key, String pattern, @Nullable Long count, Consumer<byte[]> consumer) {
		RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
		redisTemplate.execute((RedisCallback<Object>) action -> {
			ScanOptions.ScanOptionsBuilder builder = ScanOptions.scanOptions()
				.match(pattern);
			if (count != null) {
				builder.count(count);
			}
			try (Cursor<byte[]> cursor = action.sScan(keySerializer.serialize(key), builder.build())) {
				cursor.forEachRemaining(consumer);
			}
			return null;
		});
	}

	/**
	 * 同时设置一个或多个 key-value 对。
	 * 如果某个给定 key 已经存在，那么 MSET 会用新值覆盖原来的旧值，如果这不是你所希望的效果，请考虑使用 MSETNX 命令：它只会在所有给定 key 都不存在的情况下进行设置操作。
	 * MSET 是一个原子性(atomic)操作，所有给定 key 都会在同一时间内被设置，某些给定 key 被更新而另一些给定 key 没有改变的情况，不可能发生。
	 * <pre>
	 * 例子：
	 * Cache cache = RedisKit.use();			// 使用 Redis 的 cache
	 * cache.mset("k1", "v1", "k2", "v2");		// 放入多个 key value 键值对
	 * List list = cache.mget("k1", "k2");		// 利用多个键值得到上面代码放入的值
	 * </pre>
	 */
	public void mSet(Object... keysValues) {
		valueOps.multiSet(CollectionUtil.toMap(keysValues));
	}

	/**
	 * 返回所有(一个或多个)给定 key 的值。
	 * 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 nil 。因此，该命令永不失败。
	 */
	public List<Object> mGet(String... keys) {
		return mGet(Arrays.asList(keys));
	}

	/**
	 * 返回所有(一个或多个)给定 key 的值。
	 * 如果给定的 key 里面，有某个 key 不存在，那么这个 key 返回特殊值 nil 。因此，该命令永不失败。
	 */
	public List<Object> mGet(Collection<String> keys) {
		return valueOps.multiGet(keys);
	}

	/**
	 * 将 key 中储存的数字值减一。
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 关于递增(increment) / 递减(decrement)操作的更多信息，请参见 INCR 命令。
	 */
	public Long decr(String key) {
		return valueOps.decrement(key);
	}

	/**
	 * 将 key 所储存的值减去减量 decrement 。
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 关于更多递增(increment) / 递减(decrement)操作的更多信息，请参见 INCR 命令。
	 */
	public Long decrBy(String key, long longValue) {
		return valueOps.decrement(key, longValue);
	}

	/**
	 * 将 key 所储存的值减去减量 decrement 。
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 关于更多递增(increment) / 递减(decrement)操作的更多信息，请参见 INCR 命令。
	 */
	public Long decrBy(String key, long longValue, long seconds) {
		RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
		byte[] serializedKey = keySerializer.serialize(key);
		List<Object> result = redisTemplate.executePipelined((RedisCallback<Long>) action -> {
			Long data = action.decrBy(serializedKey, longValue);
			action.expire(serializedKey, seconds);
			return data;
		});
		return (Long) result.get(0);
	}

	/**
	 * 将 key 中储存的数字值增一。
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 */
	public Long incr(String key) {
		return valueOps.increment(key);
	}

	/**
	 * 将 key 所储存的值加上增量 increment 。
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 关于递增(increment) / 递减(decrement)操作的更多信息，参见 INCR 命令。
	 */
	public Long incrBy(String key, long longValue) {
		return valueOps.increment(key, longValue);
	}

	/**
	 * 将 key 所储存的值加上增量 increment 。
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 关于递增(increment) / 递减(decrement)操作的更多信息，参见 INCR 命令。
	 */
	public Long incrBy(String key, long longValue, long seconds) {
		RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
		byte[] serializedKey = keySerializer.serialize(key);
		List<Object> result = redisTemplate.executePipelined((RedisCallback<Long>) action -> {
			Long data = action.incrBy(serializedKey, longValue);
			action.expire(serializedKey, seconds);
			return data;
		});
		return (Long) result.get(0);
	}

	/**
	 * 获取记数器的值，用于获取 incr、incrBy 的值
	 *
	 * @param key key
	 */
	public Long getCounter(String key) {
		RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
		return redisTemplate.execute((RedisCallback<Long>) action -> {
			byte[] value = action.get(keySerializer.serialize(key));
			return Long.valueOf(new String(value));
		});
	}

	/**
	 * 获取记数器的值，用于初始化获取 incr、incrBy 的值
	 *
	 * @param key     key
	 * @param seconds 超时时间
	 * @param loader  加载器
	 */
	public Long getCounter(String key, long seconds, Supplier<Long> loader) {
		RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
		return redisTemplate.execute((RedisCallback<Long>) action -> {
			byte[] keyBytes = keySerializer.serialize(key);
			byte[] value = action.get(keyBytes);
			long longValue;
			if (value != null) {
				longValue = Long.valueOf(new String(value));
			} else {
				Long loaderValue = loader.get();
				longValue = loaderValue == null ? 0 : loaderValue;
				action.setEx(keyBytes, seconds, String.valueOf(longValue).getBytes());
			}
			return longValue;
		});
	}

	/**
	 * 检查给定 key 是否存在。
	 */
	public Boolean exists(String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 检查给定 key 是否存在。
	 */
	public Boolean exists(CacheKey cacheKey) {
		return exists(cacheKey.getKey());
	}

	/**
	 * 从当前数据库中随机返回(不删除)一个 key 。
	 */
	public String randomKey() {
		return redisTemplate.randomKey();
	}

	/**
	 * 将 key 改名为 newkey 。
	 * 当 key 和 newkey 相同，或者 key 不存在时，返回一个错误。
	 * 当 newkey 已经存在时， RENAME 命令将覆盖旧值。
	 */
	public void rename(String oldkey, String newkey) {
		redisTemplate.rename(oldkey, newkey);
	}

	/**
	 * 将当前数据库的 key 移动到给定的数据库 db 当中。
	 * 如果当前数据库(源数据库)和给定数据库(目标数据库)有相同名字的给定 key ，或者 key 不存在于当前数据库，那么 MOVE 没有任何效果。
	 * 因此，也可以利用这一特性，将 MOVE 当作锁(locking)原语(primitive)。
	 */
	public Boolean move(String key, int dbIndex) {
		return redisTemplate.move(key, dbIndex);
	}

	/**
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
	 * 在 Redis 中，带有生存时间的 key 被称为『易失的』(volatile)。
	 */
	public Boolean expire(String key, long seconds) {
		return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
	}

	/**
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
	 * 在 Redis 中，带有生存时间的 key 被称为『易失的』(volatile)。
	 */
	public Boolean expire(String key, Duration timeout) {
		return expire(key, timeout.getSeconds());
	}

	/**
	 * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置生存时间。不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。
	 */
	public Boolean expireAt(String key, Date date) {
		return redisTemplate.expireAt(key, date);
	}

	/**
	 * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置生存时间。不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。
	 */
	public Boolean expireAt(String key, long unixTime) {
		return expireAt(key, new Date(unixTime));
	}

	/**
	 * 这个命令和 EXPIRE 命令的作用类似，但是它以毫秒为单位设置 key 的生存时间，而不像 EXPIRE 命令那样，以秒为单位。
	 */
	public Boolean pExpire(String key, long milliseconds) {
		return redisTemplate.expire(key, milliseconds, TimeUnit.MILLISECONDS);
	}

	/**
	 * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
	 * 当 key 存在但不是字符串类型时，返回一个错误。
	 */
	public <T> T getSet(String key, Object value) {
		return (T) valueOps.getAndSet(key, value);
	}

	/**
	 * 移除给定 key 的生存时间，将这个 key 从『易失的』(带生存时间 key )转换成『持久的』(一个不带生存时间、永不过期的 key )。
	 */
	public Boolean persist(String key) {
		return redisTemplate.persist(key);
	}

	/**
	 * 返回 key 所储存的值的类型。
	 */
	public String type(String key) {
		return redisTemplate.type(key).code();
	}

	/**
	 * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
	 */
	public Long ttl(String key) {
		return redisTemplate.getExpire(key);
	}

	/**
	 * 这个命令类似于 TTL 命令，但它以毫秒为单位返回 key 的剩余生存时间，而不是像 TTL 命令那样，以秒为单位。
	 */
	public Long pttl(String key) {
		return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
	}

	/**
	 * 将哈希表 key 中的域 field 的值设为 value 。
	 * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。
	 * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
	 */
	public void hSet(String key, Object field, Object value) {
		hashOps.put(key, field, value);
	}

	/**
	 * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
	 * 此命令会覆盖哈希表中已存在的域。
	 * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
	 */
	public void hMset(String key, Map<Object, Object> hash) {
		hashOps.putAll(key, hash);
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 */
	public <T> T hGet(String key, Object field) {
		return (T) hashOps.get(key, field);
	}

	/**
	 * 返回哈希表 key 中，一个或多个给定域的值。
	 * 如果给定的域不存在于哈希表，那么返回一个 nil 值。
	 * 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表。
	 */
	public List hmGet(String key, Object... fields) {
		return hmGet(key, Arrays.asList(fields));
	}

	/**
	 * 返回哈希表 key 中，一个或多个给定域的值。
	 * 如果给定的域不存在于哈希表，那么返回一个 nil 值。
	 * 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表。
	 */
	public List hmGet(String key, Collection<Object> hashKeys) {
		return hashOps.multiGet(key, hashKeys);
	}

	/**
	 * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
	 */
	public Long hDel(String key, Object... fields) {
		return hashOps.delete(key, fields);
	}

	/**
	 * 查看哈希表 key 中，给定域 field 是否存在。
	 */
	public Boolean hExists(String key, Object field) {
		return hashOps.hasKey(key, field);
	}

	/**
	 * 返回哈希表 key 中，所有的域和值。
	 * 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
	 */
	public Map hGetAll(String key) {
		return hashOps.entries(key);
	}

	/**
	 * 返回哈希表 key 中所有域的值。
	 */
	public List hVals(String key) {
		return hashOps.values(key);
	}

	/**
	 * 返回哈希表 key 中的所有域。
	 * 底层实现此方法取名为 hfields 更为合适，在此仅为与底层保持一致
	 */
	public Set<Object> hKeys(String key) {
		return hashOps.keys(key);
	}

	/**
	 * 返回哈希表 key 中域的数量。
	 */
	public Long hLen(String key) {
		return hashOps.size(key);
	}

	/**
	 * 为哈希表 key 中的域 field 的值加上增量 increment 。
	 * 增量也可以为负数，相当于对给定域进行减法操作。
	 * 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
	 * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
	 * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
	 * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
	 */
	public Long hIncrBy(String key, Object field, long value) {
		return hashOps.increment(key, field, value);
	}

	/**
	 * 为哈希表 key 中的域 field 加上浮点数增量 increment 。
	 * 如果哈希表中没有域 field ，那么 HINCRBYFLOAT 会先将域 field 的值设为 0 ，然后再执行加法操作。
	 * 如果键 key 不存在，那么 HINCRBYFLOAT 会先创建一个哈希表，再创建域 field ，最后再执行加法操作。
	 * 当以下任意一个条件发生时，返回一个错误：
	 * 1:域 field 的值不是字符串类型(因为 redis 中的数字和浮点数都以字符串的形式保存，所以它们都属于字符串类型）
	 * 2:域 field 当前的值或给定的增量 increment 不能解释(parse)为双精度浮点数(double precision floating point number)
	 * HINCRBYFLOAT 命令的详细功能和 INCRBYFLOAT 命令类似，请查看 INCRBYFLOAT 命令获取更多相关信息。
	 */
	public Double hIncrByFloat(String key, Object field, double value) {
		return hashOps.increment(key, field, value);
	}

	/**
	 * 返回列表 key 中，下标为 index 的元素。
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，
	 * 以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 * 如果 key 不是列表类型，返回一个错误。
	 */
	public <T> T lIndex(String key, long index) {
		return (T) listOps.index(key, index);
	}

	/**
	 * 返回列表 key 的长度。
	 * 如果 key 不存在，则 key 被解释为一个空列表，返回 0 .
	 * 如果 key 不是列表类型，返回一个错误。
	 */
	public Long lLen(String key) {
		return listOps.size(key);
	}

	/**
	 * 移除并返回列表 key 的头元素。
	 */
	public <T> T lPop(String key) {
		return (T) listOps.leftPop(key);
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表头
	 * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，
	 * 对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，
	 * 这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令。
	 * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
	 * 当 key 存在但不是列表类型时，返回一个错误。
	 */
	public Long lPush(String key, Object... values) {
		return listOps.leftPush(key, values);
	}

	/**
	 * 将列表 key 下标为 index 的元素的值设置为 value 。
	 * 当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，返回一个错误。
	 * 关于列表下标的更多信息，请参考 LINDEX 命令。
	 */
	public void lSet(String key, long index, Object value) {
		listOps.set(key, index, value);
	}

	/**
	 * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
	 * count 的值可以是以下几种：
	 * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
	 * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
	 * count = 0 : 移除表中所有与 value 相等的值。
	 */
	public Long lRem(String key, long count, Object value) {
		return listOps.remove(key, count, value);
	}

	/**
	 * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 * <pre>
	 * 例子：
	 * 获取 list 中所有数据：cache.lrange(listKey, 0, -1);
	 * 获取 list 中下标 1 到 3 的数据： cache.lrange(listKey, 1, 3);
	 * </pre>
	 */
	public List lRange(String key, long start, long end) {
		return listOps.range(key, start, end);
	}

	/**
	 * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
	 * 举个例子，执行命令 LTRIM list 0 2 ，表示只保留列表 list 的前三个元素，其余元素全部删除。
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 * 当 key 不是列表类型时，返回一个错误。
	 */
	public void lTrim(String key, long start, long end) {
		listOps.trim(key, start, end);
	}

	/**
	 * 移除并返回列表 key 的尾元素。
	 */
	public <T> T rPop(String key) {
		return (T) listOps.rightPop(key);
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
	 * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表尾：比如
	 * 对一个空列表 mylist 执行 RPUSH mylist a b c ，得出的结果列表为 a b c ，
	 * 等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、 RPUSH mylist c 。
	 * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。
	 * 当 key 存在但不是列表类型时，返回一个错误。
	 */
	public Long rPush(String key, Object... values) {
		return listOps.rightPush(key, values);
	}

	/**
	 * 命令 RPOPLPUSH 在一个原子时间内，执行以下两个动作：
	 * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端。
	 * 将 source 弹出的元素插入到列表 destination ，作为 destination 列表的的头元素。
	 */
	public <T> T rPopLPush(String srcKey, String dstKey) {
		return (T) listOps.rightPopAndLeftPush(srcKey, dstKey);
	}

	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
	 * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。
	 * 当 key 不是集合类型时，返回一个错误。
	 */
	public Long sAdd(String key, Object... members) {
		return setOps.add(key, members);
	}

	/**
	 * 移除并返回集合中的一个随机元素。
	 * 如果只想获取一个随机元素，但不想该元素从集合中被移除的话，可以使用 SRANDMEMBER 命令。
	 */
	public <T> T sPop(String key) {
		return (T) setOps.pop(key);
	}

	/**
	 * 返回集合 key 中的所有成员。
	 * 不存在的 key 被视为空集合。
	 */
	public Set sMembers(String key) {
		return setOps.members(key);
	}

	/**
	 * 判断 member 元素是否集合 key 的成员。
	 */
	public boolean sIsMember(String key, Object member) {
		return setOps.isMember(key, member);
	}

	/**
	 * 返回多个集合的交集，多个集合由 keys 指定
	 */
	public Set sInter(String key, String otherKey) {
		return setOps.intersect(key, otherKey);
	}

	/**
	 * 返回多个集合的交集，多个集合由 keys 指定
	 */
	public Set sInter(String key, Collection<String> otherKeys) {
		return setOps.intersect(key, otherKeys);
	}

	/**
	 * 返回集合中的一个随机元素。
	 */
	public <T> T sRandMember(String key) {
		return (T) setOps.randomMember(key);
	}

	/**
	 * 返回集合中的 count 个随机元素。
	 * 从 Redis 2.6 版本开始， SRANDMEMBER 命令接受可选的 count 参数：
	 * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。
	 * 如果 count 大于等于集合基数，那么返回整个集合。
	 * 如果 count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。
	 * 该操作和 SPOP 相似，但 SPOP 将随机元素从集合中移除并返回，而 SRANDMEMBER 则仅仅返回随机元素，而不对集合进行任何改动。
	 */
	public List sRandMember(String key, int count) {
		return setOps.randomMembers(key, count);
	}

	/**
	 * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
	 */
	public Long sRem(String key, Object... members) {
		return setOps.remove(key, members);
	}

	/**
	 * 返回多个集合的并集，多个集合由 keys 指定
	 * 不存在的 key 被视为空集。
	 */
	public Set sUnion(String key, String otherKey) {
		return setOps.union(key, otherKey);
	}

	/**
	 * 返回多个集合的并集，多个集合由 keys 指定
	 * 不存在的 key 被视为空集。
	 */
	public Set sUnion(String key, Collection<String> otherKeys) {
		return setOps.union(key, otherKeys);
	}

	/**
	 * 返回一个集合的全部成员，该集合是所有给定集合之间的差集。
	 * 不存在的 key 被视为空集。
	 */
	public Set sDiff(String key, String otherKey) {
		return setOps.difference(key, otherKey);
	}

	/**
	 * 返回一个集合的全部成员，该集合是所有给定集合之间的差集。
	 * 不存在的 key 被视为空集。
	 */
	public Set sDiff(String key, Collection<String> otherKeys) {
		return setOps.difference(key, otherKeys);
	}

	/**
	 * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
	 * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，
	 * 并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。
	 */
	public Boolean zAdd(String key, Object member, double score) {
		return zSetOps.add(key, member, score);
	}

	/**
	 * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
	 * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，
	 * 并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。
	 */
	public Long zAdd(String key, Map<Object, Double> scoreMembers) {
		Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
		scoreMembers.forEach((k, v) -> {
			tuples.add(new DefaultTypedTuple<>(k, v));
		});
		return zSetOps.add(key, tuples);
	}

	/**
	 * 返回有序集 key 的基数。
	 */
	public Long zCard(String key) {
		return zSetOps.zCard(key);
	}

	/**
	 * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
	 * 关于参数 min 和 max 的详细使用方法，请参考 ZRANGEBYSCORE 命令。
	 */
	public Long zCount(String key, double min, double max) {
		return zSetOps.count(key, min, max);
	}

	/**
	 * 为有序集 key 的成员 member 的 score 值加上增量 increment 。
	 */
	public Double zIncrBy(String key, Object member, double score) {
		return zSetOps.incrementScore(key, member, score);
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。
	 * 其中成员的位置按 score 值递增(从小到大)来排序。
	 * 具有相同 score 值的成员按字典序(lexicographical order )来排列。
	 * 如果你需要成员按 score 值递减(从大到小)来排列，请使用 ZREVRANGE 命令。
	 */
	public Set zRange(String key, long start, long end) {
		return zSetOps.range(key, start, end);
	}

	/**
	 * 返回有序集 key 中，指定区间内的成员。
	 * 其中成员的位置按 score 值递减(从大到小)来排列。
	 * 具有相同 score 值的成员按字典序的逆序(reverse lexicographical order)排列。
	 * 除了成员按 score 值递减的次序排列这一点外， ZREVRANGE 命令的其他方面和 ZRANGE 命令一样。
	 */
	public Set zRevrange(String key, long start, long end) {
		return zSetOps.reverseRange(key, start, end);
	}

	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
	 * 有序集成员按 score 值递增(从小到大)次序排列。
	 */
	public Set zRangeByScore(String key, double min, double max) {
		return zSetOps.rangeByScore(key, min, max);
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
	 * 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
	 * 使用 ZREVRANK 命令可以获得成员按 score 值递减(从大到小)排列的排名。
	 */
	public Long zRank(String key, Object member) {
		return zSetOps.rank(key, member);
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序。
	 * 排名以 0 为底，也就是说， score 值最大的成员排名为 0 。
	 * 使用 ZRANK 命令可以获得成员按 score 值递增(从小到大)排列的排名。
	 */
	public Long zRevrank(String key, Object member) {
		return zSetOps.reverseRank(key, member);
	}

	/**
	 * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
	 * 当 key 存在但不是有序集类型时，返回一个错误。
	 */
	public Long zRem(String key, Object... members) {
		return zSetOps.remove(key, members);
	}

	/**
	 * 返回有序集 key 中，成员 member 的 score 值。
	 * 如果 member 元素不是有序集 key 的成员，或 key 不存在，返回 nil 。
	 */
	public Double zScore(String key, Object member) {
		return zSetOps.score(key, member);
	}

}
