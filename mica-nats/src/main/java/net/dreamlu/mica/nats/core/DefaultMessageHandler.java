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

import io.nats.client.Message;
import io.nats.client.MessageHandler;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 默认的 nats 消息处理器
 *
 * @author L.cm
 */
public class DefaultMessageHandler implements MessageHandler {
	private final Object bean;
	private final Method method;

	public DefaultMessageHandler(Object bean, Method method) {
		this.bean = bean;
		this.method = makeAccessible(method);
	}

	private static Method makeAccessible(Method method) {
		ReflectionUtils.makeAccessible(method);
		return method;
	}

	@Override
	public void onMessage(Message msg) throws InterruptedException {
		ReflectionUtils.invokeMethod(method, bean, msg);
	}
}
