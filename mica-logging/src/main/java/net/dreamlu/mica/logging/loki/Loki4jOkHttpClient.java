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
import com.github.loki4j.client.http.HttpHeader;
import com.github.loki4j.client.http.Loki4jHttpClient;
import com.github.loki4j.client.http.LokiResponse;
import net.dreamlu.mica.core.utils.IoUtil;
import okhttp3.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

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
		this.httpClient = okHttpClientBuilder(conf);
		this.mediaType = MediaType.get(conf.contentType);
		this.requestBuilder = requestBuilder(conf);
	}

	private static OkHttpClient okHttpClientBuilder(HttpConfig conf) {
		return new OkHttpClient.Builder()
			.connectTimeout(conf.connectionTimeoutMs, TimeUnit.MICROSECONDS)
			.writeTimeout(conf.requestTimeoutMs, TimeUnit.MICROSECONDS)
			.readTimeout(conf.requestTimeoutMs, TimeUnit.MICROSECONDS)
			.build();
	}

	private static Request requestBuilder(HttpConfig conf) {
		Request.Builder request = new Request.Builder()
			.url(conf.pushUrl)
			.addHeader(HttpHeader.CONTENT_TYPE, conf.contentType);
		conf.tenantId.ifPresent(tenant -> request.addHeader(HttpHeader.X_SCOPE_ORGID, tenant));
		conf.basicAuthToken().ifPresent(token -> request.addHeader(HttpHeader.AUTHORIZATION, "Basic " + token));
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
			request.post(RequestBody.create(batch.array(), mediaType, batch.position(), batch.remaining()));
		} else {
			int len = batch.remaining();
			if (len > bodyBuffer.length) {
				bodyBuffer = new byte[len];
			}
			batch.get(bodyBuffer, 0, len);
			request.post(RequestBody.create(bodyBuffer, mediaType,  0, len));
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
		Cache cache = httpClient.cache();
		if (cache != null) {
			IoUtil.closeQuietly(cache);
		}
	}
}
