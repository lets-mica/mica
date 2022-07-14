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
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

/**
 * 基于 redis Stream 的消息发布器
 *
 * @author L.cm
 */
public interface RStreamTemplate {

	/**
	 * 自定义 pojo 类型 key
	 */
	String OBJECT_PAYLOAD_KEY = "@payload";

	/**
	 * 发布消息
	 *
	 * @param name  队列名
	 * @param value 消息
	 * @return 消息id
	 */
	default RecordId send(String name, Object value) {
		return send(ObjectRecord.create(name, value));
	}

	/**
	 * 发布消息
	 *
	 * @param name  队列名
	 * @param key   消息key
	 * @param value 消息
	 * @return 消息id
	 */
	default RecordId send(String name, String key, Object value) {
		return send(name, Collections.singletonMap(key, value));
	}

	/**
	 * 发布消息
	 *
	 * @param name 队列名
	 * @param key  消息key
	 * @param data 消息
	 * @return 消息id
	 */
	default RecordId send(String name, String key, byte[] data) {
		return send(name, key, data, RedisStreamCommands.XAddOptions.none());
	}

	/**
	 * 发布消息
	 *
	 * @param name   队列名
	 * @param key    消息key
	 * @param data   消息
	 * @param maxLen 限制 stream 最大长度
	 * @return 消息id
	 */
	default RecordId send(String name, String key, byte[] data, long maxLen) {
		return send(name, key, data, RedisStreamCommands.XAddOptions.maxlen(maxLen));
	}

	/**
	 * 发布消息
	 *
	 * @param name    队列名
	 * @param key     消息key
	 * @param data    消息
	 * @param options XAddOptions
	 * @return 消息id
	 */
	RecordId send(String name, String key, byte[] data, RedisStreamCommands.XAddOptions options);

	/**
	 * 发布消息
	 *
	 * @param name   队列名
	 * @param key    消息key
	 * @param data   消息
	 * @param mapper mapper
	 * @param <T>    泛型
	 * @return 消息id
	 */
	default <T> RecordId send(String name, String key, T data, Function<T, byte[]> mapper, long maxLen) {
		return send(name, key, mapper.apply(data), maxLen);
	}

	/**
	 * 发布消息
	 *
	 * @param name    队列名
	 * @param key     消息key
	 * @param data    消息
	 * @param mapper  mapper
	 * @param options XAddOptions
	 * @param <T>     泛型
	 * @return 消息id
	 */
	default <T> RecordId send(String name, String key, T data, Function<T, byte[]> mapper, RedisStreamCommands.XAddOptions options) {
		return send(name, key, mapper.apply(data), options);
	}

	/**
	 * 发布消息
	 *
	 * @param name   队列名
	 * @param key    消息key
	 * @param data   消息
	 * @param mapper 消息转换
	 * @param <T>    泛型
	 * @return 消息id
	 */
	default <T> RecordId send(String name, String key, T data, Function<T, byte[]> mapper) {
		return send(name, key, mapper.apply(data));
	}

	/**
	 * 批量发布
	 *
	 * @param name     队列名
	 * @param messages 消息
	 * @return 消息id
	 */
	default RecordId send(String name, Map<String, Object> messages) {
		return send(MapRecord.create(name, messages));
	}

	/**
	 * 发送消息
	 *
	 * @param record Record
	 * @return 消息id
	 */
	RecordId send(Record<String, ?> record);

	/**
	 * 删除消息
	 *
	 * @param name      stream name
	 * @param recordIds recordIds
	 * @return Long
	 */
	@Nullable
	Long delete(String name, String... recordIds);

	/**
	 * 删除消息
	 *
	 * @param name      stream name
	 * @param recordIds recordIds
	 * @return Long
	 */
	@Nullable
	Long delete(String name, RecordId... recordIds);

	/**
	 * 删除消息
	 *
	 * @param record Record
	 * @return Long
	 */
	@Nullable
	default Long delete(Record<String, ?> record) {
		return delete(record.getStream(), record.getId());
	}

	/**
	 * 对流进行修剪，限制长度
	 *
	 * @param name  name
	 * @param count count
	 * @return Long
	 */
	@Nullable
	default Long trim(String name, long count) {
		return trim(name, count, false);
	}

	/**
	 * 对流进行修剪，限制长度
	 *
	 * @param name                name
	 * @param count               count
	 * @param approximateTrimming approximateTrimming
	 * @return Long
	 */
	@Nullable
	Long trim(String name, long count, boolean approximateTrimming);

	/**
	 * 手动 ack
	 *
	 * @param name      name
	 * @param group     group
	 * @param recordIds recordIds
	 * @return Long
	 */
	@Nullable
	Long acknowledge(String name, String group, String... recordIds);

	/**
	 * 手动 ack
	 *
	 * @param name      name
	 * @param group     group
	 * @param recordIds recordIds
	 * @return Long
	 */
	@Nullable
	Long acknowledge(String name, String group, RecordId... recordIds);

	/**
	 * 手动 ack
	 *
	 * @param group  group
	 * @param record record
	 * @return Long
	 */
	@Nullable
	Long acknowledge(String group, Record<String, ?> record);

}
