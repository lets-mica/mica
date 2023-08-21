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

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.nats.annotation.NatsListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * nats 监听器处理
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class NatsListenerDetector implements BeanPostProcessor {
    private final Connection natsConnection;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> userClass = ClassUtils.getUserClass(bean);
        ReflectionUtils.doWithMethods(userClass, method -> {
            NatsListener listener = AnnotationUtils.findAnnotation(method, NatsListener.class);
            if (listener != null) {
                Dispatcher connectionDispatcher = natsConnection.createDispatcher();
                DefaultMessageHandler messageHandler = new DefaultMessageHandler(bean, method);
                String subject = listener.value();
                Assert.hasText(subject, "@NatsListener value(subject) must not be empty.");
                String queue = listener.queue();
                if (StringUtils.hasText(queue)) {
                    connectionDispatcher.subscribe(subject, queue, messageHandler);
                } else {
                    connectionDispatcher.subscribe(subject, messageHandler);
                }
            }
        }, ReflectionUtils.USER_DECLARED_METHODS);
        return bean;
    }

}
