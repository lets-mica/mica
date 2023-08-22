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

package net.dreamlu.mica.nats.core;

import io.nats.client.*;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.utils.Exceptions;
import net.dreamlu.mica.nats.annotation.NatsStreamListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * nats JetStream 监听器处理
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class NatsStreamListenerDetector implements BeanPostProcessor {
    private final Connection natsConnection;
    private final JetStream jetStream;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> userClass = ClassUtils.getUserClass(bean);
        ReflectionUtils.doWithMethods(userClass, method -> {
            NatsStreamListener listener = AnnotationUtils.findAnnotation(method, NatsStreamListener.class);
            if (listener != null) {
                String subject = listener.value();
                Assert.hasText(subject, "@NatsStreamListener value(subject) must not be empty.");
                // 消息处理器
                MessageHandler messageHandler = new DefaultMessageHandler(bean, method);
                try {
                    jetStreamSubscribe(listener, messageHandler);
                } catch (JetStreamApiException | IOException e) {
                    throw Exceptions.unchecked(e);
                }
            }
        }, ReflectionUtils.USER_DECLARED_METHODS);
        return bean;
    }

    /**
     * JetStream 订阅
     *
     * @param listener       NatsStreamListener
     * @param messageHandler MessageHandler
     */
    private void jetStreamSubscribe(NatsStreamListener listener, MessageHandler messageHandler)
            throws JetStreamApiException, IOException {
        String subject = listener.value();
        // 调度器
        Dispatcher dispatcher = natsConnection.createDispatcher(messageHandler);
        // 订阅策略
        PushSubscribeOptions.Builder optionsBuilder = PushSubscribeOptions.builder()
                .deliverSubject(listener.deliverSubject())
                .deliverGroup(listener.deliverGroup())
                .pendingByteLimit(listener.pendingByteLimit())
                .pendingMessageLimit(listener.pendingMessageLimit())
                .ordered(listener.ordered());
        // stream 流名称
        String stream = listener.stream();
        if (StringUtils.hasText(stream)) {
            optionsBuilder.stream(stream);
        }
        // 队列
        String queue = listener.queue();
        // 是否自动 ack
        boolean autoAck = listener.autoAck();
        if (StringUtils.hasText(queue)) {
            jetStream.subscribe(subject, queue, dispatcher, messageHandler, autoAck, optionsBuilder.build());
        } else {
            jetStream.subscribe(subject, dispatcher, messageHandler, autoAck, optionsBuilder.build());
        }
    }

}
