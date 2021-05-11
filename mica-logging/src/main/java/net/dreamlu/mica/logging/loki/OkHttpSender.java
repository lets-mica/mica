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

package net.dreamlu.mica.logging.loki;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import com.github.loki4j.common.HttpHeaders;
import com.github.loki4j.common.LokiResponse;
import com.github.loki4j.logback.AbstractHttpSender;
import okhttp3.*;
import okhttp3.internal.Util;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Loki sender that is backed by OkHttp
 *
 * @author L.cm
 */
@NoAutoStart
public class OkHttpSender extends AbstractHttpSender {
	private OkHttpClient httpClient;
	private MediaType mediaType;
	/**
	 * Buffer is needed for turning ByteBuffer into byte array
	 * only if direct ByteBuffer arrived.
	 */
	private byte[] bodyBuffer = new byte[0];

	@Override
	public void start() {
		super.start();
		httpClient = new OkHttpClient();
		mediaType = MediaType.get(contentType);
	}

	@Override
	public void stop() {
		super.stop();
		httpClient.dispatcher().executorService().shutdown();
		httpClient.connectionPool().evictAll();
		Util.closeQuietly(httpClient.cache());
	}

	@Override
	public LokiResponse send(ByteBuffer batch) {
		Request.Builder request = new Request.Builder()
			.url(getUrl())
			.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
		tenantId.ifPresent(tenant -> request.addHeader(HttpHeaders.X_SCOPE_ORGID, tenant));
		basicAuthToken.ifPresent(token -> request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + token));
		if (batch.hasArray()) {
			request.post(RequestBody.create(mediaType, batch.array(), batch.position(), batch.remaining()));
		} else {
			int len = batch.remaining();
			if (len > bodyBuffer.length) {
				bodyBuffer = new byte[len];
			}
			batch.get(bodyBuffer, 0, len);
			request.post(RequestBody.create(mediaType, bodyBuffer, 0, len));
		}
		Call call = httpClient.newCall(request.build());
		try (Response response = call.execute()) {
			String body = response.body() != null ? response.body().string() : "";
			return new LokiResponse(response.code(), body);
		} catch (IOException e) {
			throw new RuntimeException("Error while sending batch to Loki", e);
		}
	}

}
