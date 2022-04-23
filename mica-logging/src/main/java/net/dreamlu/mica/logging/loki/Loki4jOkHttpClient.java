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

import com.github.loki4j.client.http.HttpConfig;
import com.github.loki4j.client.http.HttpHeaders;
import com.github.loki4j.client.http.Loki4jHttpClient;
import com.github.loki4j.client.http.LokiResponse;
import okhttp3.*;
import okhttp3.internal.Util;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Loki4j OkHttpClient
 *
 * @author L.cm
 */
public class Loki4jOkHttpClient implements Loki4jHttpClient {
	private final HttpConfig conf;
	private final OkHttpClient httpClient;
	private final MediaType mediaType;
	private final Request requestBuilder;
	private byte[] bodyBuffer = new byte[0];

	public Loki4jOkHttpClient(HttpConfig conf) {
		this.conf = conf;
		this.httpClient = new OkHttpClient();
		this.mediaType = MediaType.get(conf.contentType);
		this.requestBuilder = builderRequest(conf);
	}

	private static Request builderRequest(HttpConfig conf) {
		Request.Builder request = new Request.Builder()
			.url(conf.pushUrl)
			.addHeader(HttpHeaders.CONTENT_TYPE, conf.contentType);
		conf.tenantId.ifPresent(tenant -> request.addHeader(HttpHeaders.X_SCOPE_ORGID, tenant));
		conf.basicAuthToken().ifPresent(token -> request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + token));
		return request.build();
	}

	@Override
	public HttpConfig getConfig() {
		return this.conf;
	}

	@Override
	public LokiResponse send(ByteBuffer batch) throws Exception {
		Request.Builder request = requestBuilder.newBuilder();
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

	@Override
	public void close() throws Exception {
		httpClient.dispatcher().executorService().shutdown();
		httpClient.connectionPool().evictAll();
		Util.closeQuietly(httpClient.cache());
	}
}
