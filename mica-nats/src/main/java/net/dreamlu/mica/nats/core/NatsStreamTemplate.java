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
import io.nats.client.PublishOptions;
import io.nats.client.api.PublishAck;
import io.nats.client.impl.Headers;

import java.util.concurrent.CompletableFuture;

/**
 * nats stream Template
 *
 * @author L.cm
 */
public interface NatsStreamTemplate {

	/**
	 * Send a message to the specified subject and waits for a response from
	 * Jetstream. The default publish options will be used.
	 * The message body <strong>will not</strong> be copied. The expected
	 * usage with string content is something like:
	 *
	 * <pre>
	 * nc = Nats.connect()
	 * JetStream js = nc.JetStream()
	 * js.publish("destination", "message".getBytes("UTF-8"))
	 * </pre>
	 *
	 * where the sender creates a byte array immediately before calling publish.
	 *
	 * @param subject the subject to send the message to
	 * @param body the message body
	 * @return The acknowledgement of the publish
	 */
	PublishAck publish(String subject, byte[] body);

	/**
	 * Send a message to the specified subject and waits for a response from
	 * Jetstream. The default publish options will be used.
	 * The message body <strong>will not</strong> be copied. The expected
	 * usage with string content is something like:
	 *
	 * <pre>
	 * nc = Nats.connect()
	 * JetStream js = nc.JetStream()
	 * Headers h = new Headers().put("foo", "bar");
	 * js.publish("destination", h, "message".getBytes("UTF-8"))
	 * </pre>
	 *
	 * where the sender creates a byte array immediately before calling publish.
	 * See {@link #publish(String, byte[]) publish()} for more details on
	 * publish during reconnect.
	 *
	 * @param subject the subject to send the message to
	 * @param headers Optional headers to publish with the message.
	 * @param body the message body
	 * @return The acknowledgement of the publish
	 */
	PublishAck publish(String subject, Headers headers, byte[] body);

	/**
	 * Send a message to the specified subject and waits for a response from
	 * Jetstream. The message body <strong>will not</strong> be copied. The expected
	 * usage with string content is something like:
	 *
	 * <pre>
	 * nc = Nats.connect()
	 * JetStream js = nc.JetStream()
	 * js.publish("destination", "message".getBytes("UTF-8"), publishOptions)
	 * </pre>
	 *
	 * where the sender creates a byte array immediately before calling publish.
	 * See {@link #publish(String, byte[]) publish()} for more details on
	 * publish during reconnect.
	 *
	 * @param subject the subject to send the message to
	 * @param body the message body
	 * @param options publisher options
	 * @return The acknowledgement of the publish
	 */
	PublishAck publish(String subject, byte[] body, PublishOptions options);

	/**
	 * Send a message to the specified subject and waits for a response from
	 * Jetstream. The message body <strong>will not</strong> be copied. The expected
	 * usage with string content is something like:
	 *
	 * <pre>
	 * nc = Nats.connect()
	 * JetStream js = nc.JetStream()
	 * Headers h = new Headers().put("foo", "bar");
	 * js.publish("destination", h, "message".getBytes("UTF-8"), publishOptions)
	 * </pre>
	 *
	 * where the sender creates a byte array immediately before calling publish.
	 * See {@link #publish(String, byte[]) publish()} for more details on
	 * publish during reconnect.
	 *
	 * @param subject the subject to send the message to
	 * @param headers Optional headers to publish with the message.
	 * @param body the message body
	 * @param options publisher options
	 * @return The acknowledgement of the publish
	 */
	PublishAck publish(String subject, Headers headers, byte[] body, PublishOptions options);

	/**
	 * Send a message to the specified subject and waits for a response from
	 * Jetstream. The default publish options will be used.
	 * The message body <strong>will not</strong> be copied. The expected
	 * usage with string content is something like:
	 *
	 * <pre>
	 * nc = Nats.connect()
	 * JetStream js = nc.JetStream()
	 * js.publish(message)
	 * </pre>
	 *
	 * where the sender creates a byte array immediately before calling publish.
	 *
	 * <p>The Message object allows you to set a replyTo, but in publish requests,
	 * the replyTo is reserved for internal use as the address for the
	 * server to respond to the client with the PublishAck.</p>
	 *
	 * See {@link #publish(String, byte[]) publish()} for more details on
	 * publish during reconnect.
	 *
	 * @param message the message to send
	 * @return The acknowledgement of the publish
	 */
	PublishAck publish(Message message);

	/**
	 * Send a message to the specified subject and waits for a response from
	 * Jetstream. The message body <strong>will not</strong> be copied. The expected
	 * usage with string content is something like:
	 *
	 * <pre>
	 * nc = Nats.connect()
	 * JetStream js = nc.JetStream()
	 * js.publish(message, publishOptions)
	 * </pre>
	 *
	 * where the sender creates a byte array immediately before calling publish.
	 *
	 * <p>The Message object allows you to set a replyTo, but in publish requests,
	 * the replyTo is reserved for internal use as the address for the
	 * server to respond to the client with the PublishAck.</p>
	 *
	 * See {@link #publish(String, byte[]) publish()} for more details on
	 * publish during reconnect.
	 *
	 * @param message the message to send
	 * @param options publisher options
	 * @return The acknowledgement of the publish
	 */
	PublishAck publish(Message message, PublishOptions options);

	/**
	 * Send a message to the specified subject but does not wait for a response from
	 * Jetstream. The default publish options will be used.
	 * The message body <strong>will not</strong> be copied. The expected
	 * usage with string content is something like:
	 *
	 * <pre>
	 * nc = Nats.connect()
	 * JetStream js = nc.JetStream()
	 * CompletableFuture&lt;PublishAck&gt; future =
	 *     js.publishAsync("destination", "message".getBytes("UTF-8"),)
	 * </pre>
	 *
	 * where the sender creates a byte array immediately before calling publish.
	 * See {@link #publish(String, byte[]) publish()} for more details on
	 * publish during reconnect.
	 * The future me be completed with an exception, either
	 * an IOException covers various communication issues with the NATS server such as timeout or interruption
	 * - or - a JetStreamApiException the request had an error related to the data
	 *
	 * @param subject the subject to send the message to
	 * @param body the message body
	 * @return The future
	 */
	CompletableFuture<PublishAck> publishAsync(String subject, byte[] body);

	/**
	 * Send a message to the specified subject but does not wait for a response from
	 * Jetstream. The default publish options will be used.
	 * The message body <strong>will not</strong> be copied. The expected
	 * usage with string content is something like:
	 *
	 * <pre>
	 * nc = Nats.connect()
	 * JetStream js = nc.JetStream()
	 * Headers h = new Headers().put("foo", "bar");
	 * CompletableFuture&lt;PublishAck&gt; future =
	 *     js.publishAsync("destination", h, "message".getBytes("UTF-8"),)
	 * </pre>
	 *
	 * where the sender creates a byte array immediately before calling publish.
	 * See {@link #publish(String, byte[]) publish()} for more details on
	 * publish during reconnect.
	 * The future me be completed with an exception, either
	 * an IOException covers various communication issues with the NATS server such as timeout or interruption
	 * - or - a JetStreamApiException the request had an error related to the data
	 *
	 * @param subject the subject to send the message to
	 * @param headers Optional headers to publish with the message.
	 * @param body the message body
	 * @return The future
	 */
	CompletableFuture<PublishAck> publishAsync(String subject, Headers headers, byte[] body);

	/**
	 * Send a message to the specified subject but does not wait for a response from
	 * Jetstream. The message body <strong>will not</strong> be copied. The expected
	 * usage with string content is something like:
	 *
	 * <pre>
	 * nc = Nats.connect()
	 * JetStream js = nc.JetStream()
	 * CompletableFuture&lt;PublishAck&gt; future =
	 *     js.publishAsync("destination", "message".getBytes("UTF-8"), publishOptions)
	 * </pre>
	 *
	 * where the sender creates a byte array immediately before calling publish.
	 * See {@link #publish(String, byte[]) publish()} for more details on
	 * publish during reconnect.
	 * The future me be completed with an exception, either
	 * an IOException covers various communication issues with the NATS server such as timeout or interruption
	 * - or - a JetStreamApiException the request had an error related to the data
	 *
	 * @param subject the subject to send the message to
	 * @param body the message body
	 * @param options publisher options
	 * @return The future
	 */
	CompletableFuture<PublishAck> publishAsync(String subject, byte[] body, PublishOptions options);

	/**
	 * Send a message to the specified subject but does not wait for a response from
	 * Jetstream. The message body <strong>will not</strong> be copied. The expected
	 * usage with string content is something like:
	 *
	 * <pre>
	 * nc = Nats.connect()
	 * JetStream js = nc.JetStream()
	 * Headers h = new Headers().put("foo", "bar");
	 * CompletableFuture&lt;PublishAck&gt; future =
	 *     js.publishAsync("destination", h, "message".getBytes("UTF-8"), publishOptions)
	 * </pre>
	 *
	 * where the sender creates a byte array immediately before calling publish.
	 * See {@link #publish(String, byte[]) publish()} for more details on
	 * publish during reconnect.
	 * The future me be completed with an exception, either
	 * an IOException covers various communication issues with the NATS server such as timeout or interruption
	 * - or - a JetStreamApiException the request had an error related to the data
	 *
	 * @param subject the subject to send the message to
	 * @param headers Optional headers to publish with the message.
	 * @param body the message body
	 * @param options publisher options
	 * @return The future
	 */
	CompletableFuture<PublishAck> publishAsync(String subject, Headers headers, byte[] body, PublishOptions options);

	/**
	 * Send a message to the specified subject but does not wait for a response from
	 * Jetstream. The default publish options will be used.
	 * The message body <strong>will not</strong> be copied. The expected
	 * usage with string content is something like:
	 *
	 * <pre>
	 * nc = Nats.connect()
	 * JetStream js = nc.JetStream()
	 * CompletableFuture&lt;PublishAck&gt; future = js.publishAsync(message)
	 * </pre>
	 *
	 * where the sender creates a byte array immediately before calling publish.
	 * See {@link #publish(String, byte[]) publish()} for more details on
	 * publish during reconnect.
	 * The future me be completed with an exception, either
	 * an IOException covers various communication issues with the NATS server such as timeout or interruption
	 * - or - a JetStreamApiException the request had an error related to the data
	 *
	 * <p>The Message object allows you to set a replyTo, but in publish requests,
	 * the replyTo is reserved for internal use as the address for the
	 * server to respond to the client with the PublishAck.</p>
	 *
	 * @param message the message to send
	 * @return The future
	 */
	CompletableFuture<PublishAck> publishAsync(Message message);

	/**
	 * Send a message to the specified subject but does not wait for a response from
	 * Jetstream. The message body <strong>will not</strong> be copied. The expected
	 * usage with string content is something like:
	 *
	 * <pre>
	 * nc = Nats.connect()
	 * JetStream js = nc.JetStream()
	 * CompletableFuture&lt;PublishAck&gt; future = js.publishAsync(message, publishOptions)
	 * </pre>
	 *
	 * where the sender creates a byte array immediately before calling publish.
	 * See {@link #publish(String, byte[]) publish()} for more details on
	 * publish during reconnect.
	 * The future me be completed with an exception, either
	 * an IOException covers various communication issues with the NATS server such as timeout or interruption
	 * - or - a JetStreamApiException the request had an error related to the data
	 *
	 * <p>The Message object allows you to set a replyTo, but in publish requests,
	 * the replyTo is reserved for internal use as the address for the
	 * server to respond to the client with the PublishAck.</p>
	 *
	 * @param message the message to publish
	 * @param options publisher options
	 * @return The future
	 */
	CompletableFuture<PublishAck> publishAsync(Message message, PublishOptions options);

}
