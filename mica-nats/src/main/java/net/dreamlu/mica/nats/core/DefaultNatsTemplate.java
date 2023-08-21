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
import io.nats.client.Message;
import io.nats.client.impl.Headers;
import lombok.RequiredArgsConstructor;

/**
 * 默认的 NatsTemplate
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class DefaultNatsTemplate implements NatsTemplate {
	private final Connection connection;

	@Override
	public void publish(String subject, byte[] body) {
		connection.publish(subject, body);
	}

	@Override
	public void publish(String subject, Headers headers, byte[] body) {
		connection.publish(subject, headers, body);
	}

	@Override
	public void publish(String subject, String replyTo, byte[] body) {
		connection.publish(subject, replyTo, body);
	}

	@Override
	public void publish(String subject, String replyTo, Headers headers, byte[] body) {
		connection.publish(subject, replyTo, headers, body);
	}

	@Override
	public void publish(Message message) {
		connection.publish(message);
	}

}
