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

import org.springframework.data.redis.connection.RedisStreamCommands;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 默认的 RStreamTemplate
 *
 * @author L.cm
 */
public class DefaultRStreamTemplate implements RStreamTemplate {
	private static final RedisCustomConversions CUSTOM_CONVERSIONS = new RedisCustomConversions();
	private final RedisTemplate<String, Object> redisTemplate;
	private final StreamOperations<String, String, Object> streamOperations;

	public DefaultRStreamTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.streamOperations = redisTemplate.opsForStream();
	}

	@Override
	public RecordId send(Record<String, ?> record) {
		// 1. MapRecord
		if (record instanceof MapRecord) {
			return streamOperations.add(record);
		}
		String stream = Objects.requireNonNull(record.getStream(), "RStreamTemplate send stream name is null.");
		Object recordValue = Objects.requireNonNull(record.getValue(), () -> "RStreamTemplate send stream: " + stream + " value is null.");
		Class<?> valueClass = recordValue.getClass();
		// 2. 普通类型的 ObjectRecord
		if (CUSTOM_CONVERSIONS.isSimpleType(valueClass)) {
			return streamOperations.add(record);
		}
		// 3. 自定义类型处理
		Map<String, Object> payload = new HashMap<>();
		payload.put(RStreamTemplate.OBJECT_PAYLOAD_KEY, recordValue);
		MapRecord<String, String, Object> mapRecord = MapRecord.create(stream, payload);
		return streamOperations.add(mapRecord);
	}

	@Override
	public RecordId send(String name, String key, byte[] data, RedisStreamCommands.XAddOptions options) {
		RedisSerializer<String> stringSerializer = StringRedisSerializer.UTF_8;
		byte[] nameBytes = Objects.requireNonNull(stringSerializer.serialize(name), "redis stream name is null.");
		byte[] keyBytes = Objects.requireNonNull(stringSerializer.serialize(key), "redis stream key is null.");
		Map<byte[], byte[]> mapDate = Collections.singletonMap(keyBytes, data);
		return redisTemplate.execute((RedisCallback<RecordId>) redis -> {
			RedisStreamCommands streamCommands = redis.streamCommands();
			return streamCommands.xAdd(MapRecord.create(nameBytes, mapDate), options);
		});
	}

	@Override
	public Long delete(String name, String... recordIds) {
		return streamOperations.delete(name, recordIds);
	}

	@Override
	public Long delete(String name, RecordId... recordIds) {
		return streamOperations.delete(name, recordIds);
	}

	@Override
	public Long trim(String name, long count, boolean approximateTrimming) {
		return streamOperations.trim(name, count, approximateTrimming);
	}

	@Override
	public Long acknowledge(String name, String group, String... recordIds) {
		return streamOperations.acknowledge(name, group, recordIds);
	}

	@Override
	public Long acknowledge(String name, String group, RecordId... recordIds) {
		return streamOperations.acknowledge(name, group, recordIds);
	}

	@Override
	public Long acknowledge(String group, Record<String, ?> record) {
		return streamOperations.acknowledge(group, record);
	}

}
