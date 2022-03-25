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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.ReadOffset;

/**
 * stream read offset model
 *
 * @author L.cm
 */
@Getter
@RequiredArgsConstructor
public enum ReadOffsetModel {

	/**
	 * 从开始的地方读
	 */
	START(ReadOffset.from("0-0")),
	/**
	 * 从最近的偏移量读取。
	 */
	LATEST(ReadOffset.latest()),
	/**
	 * 读取所有新到达的元素，这些元素的id大于最后一个消费组的id。
	 */
	LAST_CONSUMED(ReadOffset.lastConsumed());

	/**
	 * readOffset
	 */
	private final ReadOffset readOffset;

}
