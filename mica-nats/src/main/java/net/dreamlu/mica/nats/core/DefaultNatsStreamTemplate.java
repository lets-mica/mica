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

import io.nats.client.JetStream;
import io.nats.client.Message;
import io.nats.client.PublishOptions;
import io.nats.client.api.PublishAck;
import io.nats.client.impl.Headers;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.utils.Exceptions;

import java.util.concurrent.CompletableFuture;

/**
 * nats JetStream 封装
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class DefaultNatsStreamTemplate implements NatsStreamTemplate {
	private final JetStream jetStream;


	@Override
	public PublishAck publish(String subject, byte[] body) {
		try {
			return jetStream.publish(subject, body);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public PublishAck publish(String subject, Headers headers, byte[] body) {
		try {
			return jetStream.publish(subject, headers, body);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public PublishAck publish(String subject, byte[] body, PublishOptions options) {
		try {
			return jetStream.publish(subject, body, options);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public PublishAck publish(String subject, Headers headers, byte[] body, PublishOptions options) {
		try {
			return jetStream.publish(subject, headers, body, options);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public PublishAck publish(Message message) {
		try {
			return jetStream.publish(message);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public PublishAck publish(Message message, PublishOptions options) {
		try {
			return jetStream.publish(message, options);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public CompletableFuture<PublishAck> publishAsync(String subject, byte[] body) {
		return jetStream.publishAsync(subject, body);
	}

	@Override
	public CompletableFuture<PublishAck> publishAsync(String subject, Headers headers, byte[] body) {
		return jetStream.publishAsync(subject, headers, body);
	}

	@Override
	public CompletableFuture<PublishAck> publishAsync(String subject, byte[] body, PublishOptions options) {
		return jetStream.publishAsync(subject, body, options);
	}

	@Override
	public CompletableFuture<PublishAck> publishAsync(String subject, Headers headers, byte[] body, PublishOptions options) {
		return jetStream.publishAsync(subject, headers, body, options);
	}

	@Override
	public CompletableFuture<PublishAck> publishAsync(Message message) {
		return jetStream.publishAsync(message);
	}

	@Override
	public CompletableFuture<PublishAck> publishAsync(Message message, PublishOptions options) {
		return jetStream.publishAsync(message, options);
	}

}
