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

import java.lang.annotation.*;

/**
 * 基于 redis 的 stream 监听
 *
 * @author L.cm
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RStreamListener {

	/**
	 * Queue name
	 *
	 * @return String
	 */
	String name();

	/**
	 * consumer group，默认为服务名 + 环境
	 *
	 * @return String
	 */
	String group() default "";

	/**
	 * 消息方式，集群模式和广播模式，如果想让所有订阅者收到所有消息，广播是一个不错的选择。
	 *
	 * @return MessageModel
	 */
	MessageModel messageModel() default MessageModel.CLUSTERING;

	/**
	 * offsetModel，默认：LAST_CONSUMED
	 *
	 * <p>
	 * 0-0 : 从开始的地方读。
	 * $ ：表示从尾部开始消费，只接受新消息，当前 Stream 消息会全部忽略。
	 * > : 读取所有新到达的元素，这些元素的id大于消费组使用的最后一个元素。
	 * </p>
	 *
	 * @return ReadOffsetModel
	 */
	ReadOffsetModel offsetModel() default ReadOffsetModel.LAST_CONSUMED;

	/**
	 * 自动 ack
	 *
	 * @return boolean
	 */
	boolean autoAcknowledge() default false;

	/**
	 * 读取原始的 bytes 数据
	 *
	 * @return boolean
	 */
	boolean readRawBytes() default false;

}
