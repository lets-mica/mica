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

import net.dreamlu.mica.core.utils.JsonUtil;
import org.springframework.data.redis.connection.RedisStreamCommands;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Collections;
import java.util.Objects;

/**
 * 默认的 RStreamTemplate
 *
 * @author L.cm
 */
@SuppressWarnings("unchecked")
public class DefaultRStreamTemplate implements RStreamTemplate {
	private static final RedisCustomConversions customConversions = new RedisCustomConversions();
	private final RedisTemplate<String, Object> redisTemplate;
	private final StreamOperations<String, String, Object> streamOperations;
	private final Jackson2HashMapper hashMapper;

	public DefaultRStreamTemplate(RedisTemplate<String, Object> redisTemplate, Jackson2HashMapper hashMapper) {
		this.redisTemplate = redisTemplate;
		this.streamOperations = redisTemplate.opsForStream();
		this.hashMapper = hashMapper;
	}

	@Override
	public RecordId send(Record<String, ?> record) {
		// 1. MapRecord
		if (record instanceof MapRecord) {
			return streamOperations.add(record);
		}
		String stream = Objects.requireNonNull(record.getStream(), "RStreamTemplate send stream name is null.");
		Object recordValue = Objects.requireNonNull(record.getValue(), "RStreamTemplate send stream: " + stream + " value is null.");
		Class<?> valueClass = recordValue.getClass();
		// 2. 普通类型的 ObjectRecord
		if (customConversions.isSimpleType(valueClass)) {
			return streamOperations.add(record);
		}
		// 3. 自定义类型，手动序列化
		ObjectRecord<String, Object> objectRecord = (ObjectRecord) record;
		// 3.1 value 类型转换
		MapRecord<String, String, String> objectMapRecord = objectRecord.toMapRecord(this.hashMapper).mapEntries(entry -> {
			String key = entry.getKey();
			String value = JsonUtil.convertValue(entry.getValue(), String.class);
			return Collections.singletonMap(key, value).entrySet().iterator().next();
		});
		// 3.2 序列化
		ByteRecord byteRecord = objectMapRecord.serialize(RedisSerializer.string());
		return redisTemplate.execute((RedisCallback<RecordId>) redis -> {
			RedisStreamCommands commands = redis.streamCommands();
			return commands.xAdd(byteRecord);
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

}
