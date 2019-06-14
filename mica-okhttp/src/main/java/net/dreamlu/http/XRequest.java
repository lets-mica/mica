/*
 * Copyright (c) 2019-2029, Dreamlu (596392912@qq.com & www.dreamlu.net).
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

package net.dreamlu.http;

import net.dreamlu.mica.core.utils.JsonUtil;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.Proxy;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * ok http 封装，请求结构体
 *
 * @author L.cm
 */
public class XRequest {
	private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
	private static OkHttpClient httpClient = new OkHttpClient();
	private final Request.Builder requestBuilder;
	private final UriComponentsBuilder uriBuilder;
	private final String httpMethod;
	private String userAgent;
	private RequestBody requestBody;
	private Boolean followRedirects;
	private HttpLoggingInterceptor.Level level;
	private Interceptor interceptor;
	private Duration connectTimeout;
	private Duration readTimeout;
	private Duration writeTimeout;
	private Proxy proxy;

	public static XRequest get(final String url) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
		return new XRequest(new Request.Builder(), uriBuilder, XMethod.GET);
	}

	public static XRequest get(final URI uri) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(uri);
		return new XRequest(new Request.Builder(), uriBuilder, XMethod.GET);
	}

	public static XRequest post(final String url) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
		return new XRequest(new Request.Builder(), uriBuilder, XMethod.POST);
	}

	public static XRequest post(final URI uri) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(uri);
		return new XRequest(new Request.Builder(), uriBuilder, XMethod.POST);
	}

	public static XRequest patch(final String url) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
		return new XRequest(new Request.Builder(), uriBuilder, XMethod.PATCH);
	}

	public static XRequest patch(final URI uri) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(uri);
		return new XRequest(new Request.Builder(), uriBuilder, XMethod.PATCH);
	}

	public static XRequest put(final String url) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
		return new XRequest(new Request.Builder(), uriBuilder, XMethod.PUT);
	}

	public static XRequest put(final URI uri) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(uri);
		return new XRequest(new Request.Builder(), uriBuilder, XMethod.PUT);
	}

	public static XRequest delete(final String url) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
		return new XRequest(new Request.Builder(), uriBuilder, XMethod.DELETE);
	}

	public static XRequest delete(final URI uri) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(uri);
		return new XRequest(new Request.Builder(), uriBuilder, XMethod.DELETE);
	}

	private static RequestBody emptyBody() {
		return RequestBody.create(null, new byte[0]);
	}

	public XRequest query(String name, Object... values) {
		this.uriBuilder.queryParam(name, values);
		return this;
	}

	XRequest form(FormBody formBody) {
		this.requestBody = formBody;
		return this;
	}

	XRequest formPart(MultipartBody multipartBody) {
		this.requestBody = multipartBody;
		return this;
	}

	public XFormBuilder formBuilder() {
		return new XFormBuilder(this);
	}

	public XFormPartBuilder formPartBuilder() {
		return new XFormPartBuilder(this);
	}

	public XRequest body(RequestBody requestBody) {
		this.requestBody = requestBody;
		return this;
	}

	public XRequest bodyString(String body) {
		this.requestBody = RequestBody.create(null, body);
		return this;
	}

	public XRequest bodyJson(Object body) {
		return bodyString(JsonUtil.toJson(body));
	}

	private XRequest(final Request.Builder requestBuilder, UriComponentsBuilder uriBuilder, String httpMethod) {
		this.requestBuilder = requestBuilder;
		this.uriBuilder = uriBuilder;
		this.httpMethod = httpMethod;
		this.userAgent = DEFAULT_USER_AGENT;
	}

	private Response internalExecute(final OkHttpClient client) throws IOException {
		OkHttpClient.Builder builder = client.newBuilder();
		if (this.connectTimeout != null) {
			builder.connectTimeout(this.connectTimeout.toMillis(), TimeUnit.MILLISECONDS);
		}
		if (this.readTimeout != null) {
			builder.readTimeout(this.readTimeout.toMillis(), TimeUnit.MILLISECONDS);
		}
		if (this.writeTimeout != null) {
			builder.writeTimeout(this.writeTimeout.toMillis(), TimeUnit.MILLISECONDS);
		}
		if (this.proxy != null) {
			builder.proxy(this.proxy);
		}
		if (this.level != null && HttpLoggingInterceptor.Level.NONE != this.level) {
			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(Slf4jLogger.INSTANCE);
			loggingInterceptor.setLevel(level);
			builder.addInterceptor(loggingInterceptor);
		}
		if (this.interceptor != null) {
			builder.addInterceptor(this.interceptor);
		}
		if (this.followRedirects != null) {
			builder.followSslRedirects(this.followRedirects);
		}
		// 设置 User-Agent
		this.requestBuilder.header("User-Agent", userAgent);
		// url
		requestBuilder.url(uriBuilder.toUriString());
		String method = this.httpMethod;
		Request request;
		if (HttpMethod.requiresRequestBody(method) && requestBody == null) {
			request = this.requestBuilder.method(method, emptyBody()).build();
		} else {
			request = this.requestBuilder.method(method, requestBody).build();
		}
		return builder.build().newCall(request).execute();
	}

	public XResponse execute() {
		try {
			return new XResponse(internalExecute(httpClient));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	//// HTTP header operations
	public XRequest addHeader(final String... namesAndValues) {
		Headers headers = Headers.of(namesAndValues);
		this.requestBuilder.headers(headers);
		return this;
	}

	public XRequest addHeader(final String name, final String value) {
		this.requestBuilder.addHeader(name, value);
		return this;
	}

	public XRequest setHeader(final String name, final String value) {
		this.requestBuilder.header(name, value);
		return this;
	}

	public XRequest removeHeader(final String name) {
		this.requestBuilder.removeHeader(name);
		return this;
	}

	public XRequest cacheControl(final CacheControl cacheControl) {
		this.requestBuilder.cacheControl(cacheControl);
		return this;
	}

	public XRequest userAgent(final String userAgent) {
		this.userAgent = userAgent;
		return this;
	}

	public XRequest followRedirects(boolean followRedirects) {
		this.followRedirects = followRedirects;
		return this;
	}

	public XRequest log() {
		this.level = HttpLoggingInterceptor.Level.BODY;
		return this;
	}

	public XRequest log(HttpLoggingInterceptor.Level level) {
		this.level = level;
		return this;
	}

	public XRequest interceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
		return this;
	}

	//// HTTP connection parameter operations
	public XRequest connectTimeout(final Duration timeout) {
		this.connectTimeout = timeout;
		return this;
	}

	public XRequest readTimeout(Duration readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}

	public XRequest writeTimeout(Duration writeTimeout) {
		this.writeTimeout = writeTimeout;
		return this;
	}

	public XRequest viaProxy(final Proxy proxy) {
		this.proxy = proxy;
		return this;
	}

	@Override
	public String toString() {
		return requestBuilder.toString();
	}

	public static void setHttpClient(OkHttpClient httpClient) {
		XRequest.httpClient = httpClient;
	}
}
