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

package net.dreamlu.mica.nats.annotation;

import io.nats.client.Consumer;

import java.lang.annotation.*;

/**
 * nats stream 监听器注解
 *
 * @author L.cm
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NatsStreamListener {

    /**
     * 主题 subject
     *
     * @return subject
     */
    String value();

    /**
     * 队列
     *
     * @return 队列名称
     */
    String queue() default "";

    /**
     * Stream（消息流）
     *
     * @return Stream name
     */
    String stream() default "";

    /**
     * 自动 ack
     *
     * @return 是否自动 ack
     */
    boolean autoAck() default false;

    /**
     * 是否按顺序消费
     *
     * @return 顺序消费
     */
    boolean ordered() default false;

    /**
     * 设置非调度推送订阅在内部（挂起）消息队列中所能容纳的最大消息数量。
     *
     * @return 最大消息数量
     */
    long pendingMessageLimit() default Consumer.DEFAULT_MAX_MESSAGES;

    /**
     * 设置非调度推送订阅在内部（挂起）消息队列中所能容纳的最大字节数。
     *
     * @return 最大字节数
     */
    long pendingByteLimit() default Consumer.DEFAULT_MAX_BYTES;

}
