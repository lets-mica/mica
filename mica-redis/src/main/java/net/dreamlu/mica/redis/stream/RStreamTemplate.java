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

import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.RecordId;

import java.util.Collections;
import java.util.Map;

/**
 * 基于 redis Stream 的消息发布器
 *
 * @author L.cm
 */
public interface RStreamTemplate {

	/**
	 * 发布消息
	 *
	 * @param name  队列名
	 * @param value 消息
	 * @return 消息id
	 */
	default RecordId send(String name, Object value) {
		return send(name, "payload", value);
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

}
