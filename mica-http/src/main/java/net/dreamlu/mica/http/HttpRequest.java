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

package net.dreamlu.mica.http;

import net.dreamlu.mica.core.retry.IRetry;
import net.dreamlu.mica.core.retry.SimpleRetry;
import net.dreamlu.mica.core.ssl.DisableValidationTrustManager;
import net.dreamlu.mica.core.ssl.TrustAllHostNames;
import net.dreamlu.mica.core.tuple.Pair;
import net.dreamlu.mica.core.utils.JsonUtil;
import net.dreamlu.mica.core.utils.ResourceUtil;
import net.dreamlu.mica.core.utils.StringPool;
import okhttp3.*;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpMethod;
import org.jspecify.annotations.Nullable;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * ok http 封装，请求结构体
 *
 * @author L.cm
 */
public class HttpRequest {
	private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0";
	private static final MediaType APPLICATION_JSON = MediaType.get("application/json");
	private static volatile OkHttpClient httpClient = new OkHttpClient();
	@Nullable
	private static HttpLoggingInterceptor globalLoggingInterceptor = null;
	private final Request.Builder requestBuilder;
	private final HttpUrl.Builder uriBuilder;
	private final String httpMethod;
	private String userAgent;
	@Nullable
	private RequestBody requestBody;
	@Nullable
	private Boolean followRedirects;
	@Nullable
	private Boolean followSslRedirects;
	private HttpLoggingInterceptor.@Nullable Logger logger;
	@Nullable
	private LogLevel logLevel;
	@Nullable
	private CookieJar cookieJar;
	@Nullable
	private EventListener eventListener;
	private final List<Interceptor> interceptors = new ArrayList<>();
	@Nullable
	private Authenticator authenticator;
	@Nullable
	private Duration connectTimeout;
	@Nullable
	private Duration readTimeout;
	@Nullable
	private Duration writeTimeout;
	@Nullable
	private List<Protocol> protocols;
	@Nullable
	private Proxy proxy;
	@Nullable
	private ProxySelector proxySelector;
	@Nullable
	private Authenticator proxyAuthenticator;
	@Nullable
	private IRetry retry;
	@Nullable
	private Predicate<ResponseSpec> respPredicate;
	@Nullable
	private HostnameVerifier hostnameVerifier;
	@Nullable
	private SSLSocketFactory sslSocketFactory;
	@Nullable
	private X509TrustManager trustManager;

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

	public HttpRequest pathParam(String name, Object value) {
		Objects.requireNonNull(name, "name == null");
		List<String> segments = this.uriBuilder.build().pathSegments();
		String pathParamName = '{' + name.trim() + '}';
		for (int i = 0; i < segments.size(); i++) {
			if (pathParamName.equalsIgnoreCase(segments.get(i))) {
				uriBuilder.setPathSegment(i, handleValue(value));
			}
		}
		return this;
	}

	public HttpRequest query(String query) {
		this.uriBuilder.query(query);
		return this;
	}

	public HttpRequest queryEncoded(String encodedQuery) {
		this.uriBuilder.encodedQuery(encodedQuery);
		return this;
	}

	public HttpRequest queryMap(@Nullable Map<String, Object> queryMap) {
		if (queryMap != null && !queryMap.isEmpty()) {
			queryMap.forEach(this::query);
		}
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

	public HttpRequest body(byte[] body) {
		return body(RequestBody.create(body));
	}

	public HttpRequest body(byte[] body, MediaType contentType) {
		return body(RequestBody.create(body, contentType));
	}

	public HttpRequest body(File body, MediaType contentType) {
		return body(RequestBody.create(body, contentType));
	}

	public HttpRequest body(String body, MediaType contentType) {
		return body(RequestBody.create(body, contentType));
	}

	public HttpRequest bodyString(String body) {
		return body(RequestBody.create(body, APPLICATION_JSON));
	}

	public HttpRequest bodyString(MediaType contentType, String body) {
		return body(RequestBody.create(body, contentType));
	}

	public HttpRequest bodyJson(Object body) {
		return bodyString(JsonUtil.toJson(body));
	}

	public HttpRequest bodyJson(Object body, MediaType contentType) {
		return body(JsonUtil.toJsonAsBytes(body), contentType);
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

	private Call internalCall(final OkHttpClient client) {
		OkHttpClient.Builder builder = client.newBuilder();
		if (connectTimeout != null) {
			builder.connectTimeout(connectTimeout.toMillis(), TimeUnit.MILLISECONDS);
		}
		if (readTimeout != null) {
			builder.readTimeout(readTimeout.toMillis(), TimeUnit.MILLISECONDS);
		}
		if (writeTimeout != null) {
			builder.writeTimeout(writeTimeout.toMillis(), TimeUnit.MILLISECONDS);
		}
		if (protocols != null && !protocols.isEmpty()) {
			builder.protocols(protocols);
		}
		if (proxy != null) {
			builder.proxy(proxy);
		}
		if (proxySelector != null) {
			builder.proxySelector(proxySelector);
		}
		if (proxyAuthenticator != null) {
			builder.proxyAuthenticator(proxyAuthenticator);
		}
		if (sslSocketFactory != null && trustManager != null) {
			builder.sslSocketFactory(sslSocketFactory, trustManager);
			if (hostnameVerifier == null) {
				hostnameVerifier = TrustAllHostNames.INSTANCE;
			}
		}
		if (hostnameVerifier != null) {
			builder.hostnameVerifier(hostnameVerifier);
		}
		if (authenticator != null) {
			builder.authenticator(authenticator);
		}
		if (eventListener != null) {
			builder.eventListener(eventListener);
		}
		if (!interceptors.isEmpty()) {
			builder.interceptors().addAll(interceptors);
		}
		if (cookieJar != null) {
			builder.cookieJar(cookieJar);
		}
		if (followRedirects != null) {
			builder.followRedirects(followRedirects);
		}
		if (followSslRedirects != null) {
			builder.followSslRedirects(followSslRedirects);
		}
		if (retry != null) {
			builder.addInterceptor(new RetryInterceptor(retry, respPredicate));
		}
		if (logger != null && logLevel != null && LogLevel.NONE != logLevel) {
			builder.addInterceptor(getLoggingInterceptor(logger, logLevel));
		} else if (globalLoggingInterceptor != null) {
			builder.addInterceptor(globalLoggingInterceptor);
		}
		// 设置 User-Agent
		requestBuilder.header("User-Agent", userAgent);
		// url
		requestBuilder.url(uriBuilder.build());
		String method = httpMethod;
		Request request;
		if (HttpMethod.requiresRequestBody(method) && requestBody == null) {
			request = requestBuilder.method(method, Util.EMPTY_REQUEST).build();
		} else {
			request = requestBuilder.method(method, requestBody).build();
		}
		return builder.build().newCall(request);
	}

	public Exchange execute() {
		return new Exchange(internalCall(httpClient));
	}

	public AsyncExchange async() {
		return new AsyncExchange(internalCall(httpClient));
	}

	public CompletableFuture<ResponseSpec> executeAsync() {
		CompletableFuture<ResponseSpec> future = new CompletableFuture<>();
		Call call = internalCall(httpClient);
		call.enqueue(new CompletableCallback(future));
		return future;
	}

	public ResponseSpec executeAsyncAndJoin() {
		return executeAsync().join();
	}

	public HttpRequest baseAuth(String userName, String password) {
		return this.authenticator(new BaseAuthenticator(userName, password));
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

	public HttpRequest addCookie(final Cookie cookie) {
		this.addHeader("Cookie", cookie.toString());
		return this;
	}

	public HttpRequest addCookie(Consumer<Cookie.Builder> consumer) {
		Cookie.Builder builder = new Cookie.Builder();
		consumer.accept(builder);
		this.addHeader("Cookie", builder.build().toString());
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

	public HttpRequest followSslRedirects(boolean followSslRedirects) {
		this.followSslRedirects = followSslRedirects;
		return this;
	}

	private static HttpLoggingInterceptor getLoggingInterceptor(HttpLoggingInterceptor.Logger httpLogger,
																LogLevel level) {
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(httpLogger);
		loggingInterceptor.setLevel(level);
		return loggingInterceptor;
	}

	public HttpRequest useSlf4jLog() {
		return useSlf4jLog(LogLevel.BODY);
	}

	public HttpRequest useSlf4jLog(LogLevel logLevel) {
		return useLog(HttpLogger.Slf4j, logLevel);
	}

	public HttpRequest useConsoleLog() {
		return useConsoleLog(LogLevel.BODY);
	}

	public HttpRequest useConsoleLog(LogLevel logLevel) {
		return useLog(HttpConsoleLogger.INSTANCE, logLevel);
	}

	public HttpRequest useDefaultLog() {
		return useDefaultLog(LogLevel.BODY);
	}

	public HttpRequest useDefaultLog(LogLevel logLevel) {
		return useLog(HttpLoggingInterceptor.Logger.DEFAULT, logLevel);
	}

	public HttpRequest useLog(HttpLoggingInterceptor.Logger logger) {
		return useLog(logger, LogLevel.BODY);
	}

	public HttpRequest useLog(HttpLoggingInterceptor.Logger logger, LogLevel logLevel) {
		this.logger = logger;
		this.logLevel = logLevel;
		return this;
	}

	public HttpRequest authenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
		return this;
	}

	public HttpRequest eventListener(EventListener eventListener) {
		this.eventListener = eventListener;
		return this;
	}

	public HttpRequest interceptor(Interceptor interceptor) {
		this.interceptors.add(interceptor);
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

	public HttpRequest protocols(List<Protocol> protocols) {
		this.protocols = protocols;
		return this;
	}

	public HttpRequest proxy(Proxy proxy) {
		this.proxy = proxy;
		return this;
	}

	public HttpRequest proxy(final Proxy.Type type, final InetSocketAddress address) {
		return proxy(new Proxy(type, address));
	}

	public HttpRequest proxy(final InetSocketAddress address) {
		return proxy(Proxy.Type.HTTP, address);
	}

	public HttpRequest proxy(final String hostname, final int port) {
		return proxy(InetSocketAddress.createUnresolved(hostname, port));
	}

	public HttpRequest proxySelector(final ProxySelector proxySelector) {
		this.proxySelector = proxySelector;
		return this;
	}

	public HttpRequest proxyAuthenticator(final Authenticator proxyAuthenticator) {
		this.proxyAuthenticator = proxyAuthenticator;
		return this;
	}

	public HttpRequest retry() {
		return retry(new SimpleRetry());
	}

	public HttpRequest retryOn(Predicate<ResponseSpec> respPredicate) {
		return retry(new SimpleRetry(), respPredicate);
	}

	public HttpRequest retry(int maxAttempts, long sleepMillis) {
		return retry(new SimpleRetry(maxAttempts, sleepMillis));
	}

	public HttpRequest retry(int maxAttempts, long sleepMillis, Predicate<ResponseSpec> respPredicate) {
		return retry(new SimpleRetry(maxAttempts, sleepMillis), respPredicate);
	}

	public HttpRequest retry(IRetry retry) {
		this.retry = retry;
		return this;
	}

	public HttpRequest retry(IRetry retry, Predicate<ResponseSpec> respPredicate) {
		this.retry = retry;
		this.respPredicate = respPredicate;
		return this;
	}

	public HttpRequest hostnameVerifier(HostnameVerifier hostnameVerifier) {
		this.hostnameVerifier = hostnameVerifier;
		return this;
	}

	public HttpRequest sslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
		this.sslSocketFactory = sslSocketFactory;
		this.trustManager = trustManager == null ? DisableValidationTrustManager.INSTANCE : trustManager;
		return this;
	}

	public HttpRequest useSSL(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
		return sslSocketFactory(sslSocketFactory, trustManager);
	}

	public HttpRequest useSSL() {
		return useSSL((InputStream) null, null);
	}

	public HttpRequest useSSL(String keyStoreFile, String keyPass) {
		return useSSL(keyStoreFile, keyPass, null, null);
	}

	public HttpRequest useSSL(String keyStoreFile, String keyPass, String trustStoreFile, String trustPass) {
		Pair<SSLContext, X509TrustManager> pair = getSslContext(keyStoreFile, keyPass, trustStoreFile, trustPass);
		return sslSocketFactory(pair.getLeft().getSocketFactory(), pair.getRight());
	}

	public HttpRequest useSSL(InputStream keyStoreInputStream, String keyPass) {
		return useSSL(keyStoreInputStream, keyPass, null, null);
	}

	public HttpRequest useSSL(InputStream keyStoreInputStream, String keyPass, InputStream trustInputStream, String trustPass) {
		Pair<SSLContext, X509TrustManager> pair = getSslContext(keyStoreInputStream, keyPass, trustInputStream, trustPass);
		return sslSocketFactory(pair.getLeft().getSocketFactory(), pair.getRight());
	}

	@Override
	public String toString() {
		return requestBuilder.toString();
	}

	public static void setHttpClient(OkHttpClient httpClient) {
		HttpRequest.httpClient = httpClient;
	}

	/**
	 * 设置全局日志，平台自带日志，默认 jdk 日志
	 *
	 * @param logLevel LogLevel
	 */
	public static void setGlobalDefaultLog(LogLevel logLevel) {
		setGlobalLog(HttpLoggingInterceptor.Logger.DEFAULT, logLevel);
	}

	/**
	 * 设置全局日志，控制台日志
	 *
	 * @param logLevel LogLevel
	 */
	public static void setGlobalConsoleLog(LogLevel logLevel) {
		setGlobalLog(HttpConsoleLogger.INSTANCE, logLevel);
	}

	/**
	 * 设置全局日志，默认级别：BODY
	 *
	 * @param logger HttpLoggingInterceptor.Logger
	 */
	public static void setGlobalLog(HttpLoggingInterceptor.Logger logger) {
		setGlobalLog(logger, LogLevel.BODY);
	}

	/**
	 * 设置全局日志，默认：Slf4j
	 *
	 * @param logLevel LogLevel
	 */
	public static void setGlobalLog(LogLevel logLevel) {
		setGlobalLog(HttpLogger.Slf4j, logLevel);
	}

	/**
	 * 设置全局日志
	 *
	 * @param logger   HttpLoggingInterceptor.Logger
	 * @param logLevel LogLevel
	 */
	public static void setGlobalLog(HttpLoggingInterceptor.Logger logger, LogLevel logLevel) {
		HttpRequest.globalLoggingInterceptor = getLoggingInterceptor(logger, logLevel);
	}

	/**
	 * 设置全局的 ssl 配置
	 */
	public static OkHttpClient setGlobalSSL() {
		return setGlobalSSL((InputStream) null, null);
	}

	public static OkHttpClient setGlobalSSL(String keyStoreFile, String keyPass) {
		return setGlobalSSL(keyStoreFile, keyPass, null, null);
	}

	public static OkHttpClient setGlobalSSL(String keyStoreFile, String keyPass, String trustStoreFile, String trustPass) {
		Pair<SSLContext, X509TrustManager> pair = getSslContext(keyStoreFile, keyPass, trustStoreFile, trustPass);
		return setGlobalSSL(pair.getLeft().getSocketFactory(), pair.getRight());
	}

	public static OkHttpClient setGlobalSSL(InputStream keyStoreInputStream, String keyPass) {
		return setGlobalSSL(keyStoreInputStream, keyPass, null, null);
	}

	public static OkHttpClient setGlobalSSL(InputStream keyStoreInputStream, String keyPass, InputStream trustInputStream, String trustPass) {
		Pair<SSLContext, X509TrustManager> pair = getSslContext(keyStoreInputStream, keyPass, trustInputStream, trustPass);
		return setGlobalSSL(pair.getLeft().getSocketFactory(), pair.getRight());
	}

	public static OkHttpClient setGlobalSSL(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
		X509TrustManager tm = trustManager == null ? DisableValidationTrustManager.INSTANCE : trustManager;
		OkHttpClient okHttpClient = httpClient.newBuilder()
			.sslSocketFactory(sslSocketFactory, tm)
			.hostnameVerifier(TrustAllHostNames.INSTANCE)
			.build();
		setHttpClient(okHttpClient);
		return okHttpClient;
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

	public static Pair<SSLContext, X509TrustManager> getSslContext(String keyStoreFile, String keyPass,
																   String trustStoreFile, String trustPass) {
		InputStream keyStoreInputStream;
		if (keyStoreFile == null) {
			keyStoreInputStream = null;
		} else if (keyStoreFile.toLowerCase().startsWith("classpath:")) {
			keyStoreInputStream = getResourceAsStream(keyStoreFile);
		} else {
			keyStoreInputStream = getFileResource(keyStoreFile);
		}
		InputStream trustStoreInputStream;
		if (trustStoreFile == null) {
			trustStoreInputStream = null;
		} else if (trustStoreFile.toLowerCase().startsWith("classpath:")) {
			trustStoreInputStream = getResourceAsStream(trustStoreFile);
		} else {
			trustStoreInputStream = getFileResource(trustStoreFile);
		}
		return getSslContext(keyStoreInputStream, keyPass, trustStoreInputStream, trustPass);
	}

	public static Pair<SSLContext, X509TrustManager> getSslContext(InputStream keyStoreInputStream, String keyPass,
																   InputStream trustInputStream, String trustPass) {
		try {
			KeyManager[] kms = null;
			TrustManager[] tms = null;
			if (keyStoreInputStream != null) {
				KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				KeyStore keyStore = KeyStore.getInstance("JKS");
				char[] keyPassChars = keyPass == null ? null : keyPass.toCharArray();
				keyStore.load(keyStoreInputStream, keyPassChars);
				keyManagerFactory.init(keyStore, keyPassChars);
				kms = keyManagerFactory.getKeyManagers();
			}
			if (trustInputStream != null) {
				char[] trustPassChars = trustPass == null ? null : trustPass.toCharArray();
				tms = getTrustManagers(trustInputStream, trustPassChars);
			}
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kms, tms, new SecureRandom());
			X509TrustManager trustManager = tms == null ? null : (X509TrustManager) tms[0];
			return Pair.create(sslContext, trustManager);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static TrustManager[] getTrustManagers(InputStream trustInputStream, char[] trustPassword)
		throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
		if (trustInputStream == null) {
			return new TrustManager[]{DisableValidationTrustManager.INSTANCE};
		} else {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(trustInputStream, trustPassword);
			trustManagerFactory.init(keyStore);
			return trustManagerFactory.getTrustManagers();
		}
	}

	/**
	 * 获取ClassPath下的资源做为流
	 *
	 * @param path 相对于ClassPath路径，可以以classpath:开头
	 * @return {@link InputStream}资源
	 */
	private static InputStream getResourceAsStream(String path) {
		if (path.toLowerCase().startsWith("classpath:")) {
			path = path.substring("classpath:".length());
		}
		return getClassLoader().getResourceAsStream(path);
	}

	/**
	 * 获取 file 下的资源做为流
	 *
	 * @param file 文件
	 * @return {@link InputStream}资源
	 */
	private static InputStream getFileResource(String file) {
		try {
			return Files.newInputStream(Paths.get(file));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 获取{@link ClassLoader}<br>
	 * 获取顺序如下：<br>
	 *
	 * <pre>
	 * 1、获取当前线程的ContextClassLoader
	 * 2、获取类对应的ClassLoader
	 * 3、获取系统ClassLoader（{@link ClassLoader#getSystemClassLoader()}）
	 * </pre>
	 *
	 * @return 类加载器
	 */
	private static ClassLoader getClassLoader() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = ResourceUtil.class.getClassLoader();
			if (null == classLoader) {
				classLoader = ClassLoader.getSystemClassLoader();
			}
		}
		return classLoader;
	}

}
