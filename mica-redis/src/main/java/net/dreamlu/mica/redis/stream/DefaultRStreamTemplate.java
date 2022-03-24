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

import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;

/**
 * 默认的 RStreamTemplate
 *
 * @author L.cm
 */
public class DefaultRStreamTemplate implements RStreamTemplate {
	private final StreamOperations<String, Object, Object> streamOperations;

	public DefaultRStreamTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.streamOperations = redisTemplate.opsForStream();
	}

	@Override
	public RecordId send(Record<String, ?> record) {
		return streamOperations.add(record);
	}

}
