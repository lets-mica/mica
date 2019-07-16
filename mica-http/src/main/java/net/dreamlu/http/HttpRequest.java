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
import net.dreamlu.mica.core.utils.StringPool;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import okhttp3.logging.HttpLoggingInterceptor;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ok http 封装，请求结构体
 *
 * @author L.cm
 */
public class HttpRequest {
	private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
	private static OkHttpClient httpClient = new OkHttpClient();
	private static HttpLoggingInterceptor globalLoggingInterceptor = null;
	private final Request.Builder requestBuilder;
	private final HttpUrl.Builder uriBuilder;
	private final String httpMethod;
	private String userAgent;
	private RequestBody requestBody;
	private Boolean followRedirects;
	private HttpLoggingInterceptor.Level level;
	private CookieJar cookieJar;
	private Interceptor interceptor;
	private Authenticator authenticator;
	private Duration connectTimeout;
	private Duration readTimeout;
	private Duration writeTimeout;
	private Proxy proxy;

	public static HttpRequest get(final String url) {
		return new HttpRequest(new Request.Builder(), url, Method.GET);
	}

	public static HttpRequest get(final URI uri) {
		return get(uri.toString());
	}

	public static HttpRequest post(final String url) {
		return new HttpRequest(new Request.Builder(), url, Method.POST);
	}

	public static HttpRequest post(final URI uri) {
		return post(uri.toString());
	}

	public static HttpRequest patch(final String url) {
		return new HttpRequest(new Request.Builder(), url, Method.PATCH);
	}

	public static HttpRequest patch(final URI uri) {
		return patch(uri.toString());
	}

	public static HttpRequest put(final String url) {
		return new HttpRequest(new Request.Builder(), url, Method.PUT);
	}

	public static HttpRequest put(final URI uri) {
		return put(uri.toString());
	}

	public static HttpRequest delete(final String url) {
		return new HttpRequest(new Request.Builder(), url, Method.DELETE);
	}

	public static HttpRequest delete(final URI uri) {
		return delete(uri.toString());
	}

	private static RequestBody emptyBody() {
		return RequestBody.create(null, new byte[0]);
	}

	public HttpRequest query(String query) {
		this.uriBuilder.query(query);
		return this;
	}

	public HttpRequest queryEncoded(String encodedQuery) {
		this.uriBuilder.encodedQuery(encodedQuery);
		return this;
	}

	public HttpRequest query(String name, @Nullable Object value) {
		this.uriBuilder.addQueryParameter(name, value == null ? null : String.valueOf(value));
		return this;
	}

	public HttpRequest queryEncoded(String encodedName, @Nullable Object encodedValue) {
		this.uriBuilder.addEncodedQueryParameter(encodedName, encodedValue == null ? null : String.valueOf(encodedValue));
		return this;
	}

	HttpRequest form(FormBody formBody) {
		this.requestBody = formBody;
		return this;
	}

	HttpRequest multipartForm(MultipartBody multipartBody) {
		this.requestBody = multipartBody;
		return this;
	}

	public FormBuilder formBuilder() {
		return new FormBuilder(this);
	}

	public MultipartFormBuilder multipartFormBuilder() {
		return new MultipartFormBuilder(this);
	}

	public HttpRequest body(RequestBody requestBody) {
		this.requestBody = requestBody;
		return this;
	}

	public HttpRequest bodyString(String body) {
		this.requestBody = RequestBody.create(null, body);
		return this;
	}

	public HttpRequest bodyJson(Object body) {
		return bodyString(JsonUtil.toJson(body));
	}

	private HttpRequest(final Request.Builder requestBuilder, String url, String httpMethod) {
		HttpUrl httpUrl = HttpUrl.parse(url);
		if (httpUrl == null) {
			throw new IllegalArgumentException(String.format("Url 不能解析: %s: [%s]。", httpMethod.toLowerCase(), url));
		}
		this.requestBuilder = requestBuilder;
		this.uriBuilder = httpUrl.newBuilder();
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
			builder.addInterceptor(getLoggingInterceptor(level));
		} else if (globalLoggingInterceptor != null) {
			builder.addInterceptor(globalLoggingInterceptor);
		}
		if (this.authenticator != null) {
			builder.authenticator(authenticator);
		}
		if (this.interceptor != null) {
			builder.addInterceptor(this.interceptor);
		}
		if (this.cookieJar != null) {
			builder.cookieJar(cookieJar);
		}
		if (this.followRedirects != null) {
			builder.followSslRedirects(this.followRedirects);
		}
		// 设置 User-Agent
		this.requestBuilder.header("User-Agent", userAgent);
		// url
		requestBuilder.url(uriBuilder.build());
		String method = this.httpMethod;
		Request request;
		if (HttpMethod.requiresRequestBody(method) && requestBody == null) {
			request = this.requestBuilder.method(method, emptyBody()).build();
		} else {
			request = this.requestBuilder.method(method, requestBody).build();
		}
		return builder.build().newCall(request).execute();
	}

	public HttpResponse execute() {
		try {
			return new HttpResponse(internalExecute(httpClient));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public HttpRequest baseAuth(String userName, String password) {
		this.authenticator = new BaseAuthenticator(userName, password);
		return this;
	}

	//// HTTP header operations
	public HttpRequest addHeader(final Map<String, String> headers) {
		this.requestBuilder.headers(Headers.of(headers));
		return this;
	}

	public HttpRequest addHeader(final String... namesAndValues) {
		Headers headers = Headers.of(namesAndValues);
		this.requestBuilder.headers(headers);
		return this;
	}

	public HttpRequest addHeader(final String name, final String value) {
		this.requestBuilder.addHeader(name, value);
		return this;
	}

	public HttpRequest setHeader(final String name, final String value) {
		this.requestBuilder.header(name, value);
		return this;
	}

	public HttpRequest removeHeader(final String name) {
		this.requestBuilder.removeHeader(name);
		return this;
	}

	public HttpRequest cacheControl(final CacheControl cacheControl) {
		this.requestBuilder.cacheControl(cacheControl);
		return this;
	}

	public HttpRequest userAgent(final String userAgent) {
		this.userAgent = userAgent;
		return this;
	}

	public HttpRequest followRedirects(boolean followRedirects) {
		this.followRedirects = followRedirects;
		return this;
	}

	private static HttpLoggingInterceptor getLoggingInterceptor(HttpLoggingInterceptor.Level level) {
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(Slf4jLogger.INSTANCE);
		loggingInterceptor.setLevel(level);
		return loggingInterceptor;
	}

	public HttpRequest log() {
		this.level = HttpLoggingInterceptor.Level.BODY;
		return this;
	}

	public HttpRequest log(LogLevel logLevel) {
		this.level = logLevel.getLevel();
		return this;
	}

	public HttpRequest authenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
		return this;
	}

	public HttpRequest interceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
		return this;
	}

	public HttpRequest cookieManager(CookieJar cookieJar) {
		this.cookieJar = cookieJar;
		return this;
	}

	//// HTTP connection parameter operations
	public HttpRequest connectTimeout(final Duration timeout) {
		this.connectTimeout = timeout;
		return this;
	}

	public HttpRequest readTimeout(Duration readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}

	public HttpRequest writeTimeout(Duration writeTimeout) {
		this.writeTimeout = writeTimeout;
		return this;
	}

	public HttpRequest viaProxy(final InetSocketAddress address) {
		this.proxy = new Proxy(Proxy.Type.HTTP, address);
		return this;
	}

	@Override
	public String toString() {
		return requestBuilder.toString();
	}

	public static void setHttpClient(OkHttpClient httpClient) {
		HttpRequest.httpClient = httpClient;
	}

	public static void setGlobalLog(LogLevel logLevel) {
		HttpRequest.globalLoggingInterceptor = getLoggingInterceptor(logLevel.getLevel());
	}

	static String handleValue(@Nullable Object value) {
		if (value == null) {
			return StringPool.EMPTY;
		}
		if (value instanceof String) {
			return (String) value;
		}
		return String.valueOf(value);
	}
}
