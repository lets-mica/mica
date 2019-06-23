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

package net.dreamlu.mica.http.logger;

import net.dreamlu.mica.launcher.MicaLogLevel;
import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * {@linkplain OkHttpClient#interceptors() application interceptor} or as a {@linkplain
 * OkHttpClient#networkInterceptors() network interceptor}. <p> The format of the logs created by
 * this class should not be considered stable and may change slightly between releases. If you need
 * a stable logging format, use your own interceptor.
 *
 * @author L.cm
 */
public final class HttpLoggingInterceptor implements Interceptor {
	private static final Charset UTF8 = StandardCharsets.UTF_8;
	private final Logger logger;
	private volatile MicaLogLevel level = MicaLogLevel.NONE;

	public interface Logger {
		/**
		 * log
		 * @param message message
		 */
		void log(String message);
	}

	public HttpLoggingInterceptor(Logger logger) {
		this.logger = logger;
	}

	/**
	 * Change the level at which this interceptor logs.
	 * @param level log Level
	 * @return HttpLoggingInterceptor
	 */
	public HttpLoggingInterceptor setLevel(MicaLogLevel level) {
		Objects.requireNonNull(level, "level == null. Use Level.NONE instead.");
		this.level = level;
		return this;
	}

	public MicaLogLevel getLevel() {
		return level;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		MicaLogLevel level = this.level;

		Request request = chain.request();
		if (level == MicaLogLevel.NONE) {
			return chain.proceed(request);
		}

		boolean logBody = level == MicaLogLevel.BODY;
		boolean logHeaders = logBody || level == MicaLogLevel.HEADERS;

		RequestBody requestBody = request.body();
		boolean hasRequestBody = requestBody != null;

		Connection connection = chain.connection();
		String requestStartMessage = "--> "
			+ request.method()
			+ ' ' + request.url()
			+ (connection != null ? " " + connection.protocol() : "");
		if (!logHeaders && hasRequestBody) {
			requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
		}
		logger.log(requestStartMessage);

		if (logHeaders) {
			if (hasRequestBody) {
				// Request body headers are only present when installed as a network interceptor. Force
				// them to be included (when available) so there values are known.
				if (requestBody.contentType() != null) {
					logger.log("Content-Type: " + requestBody.contentType());
				}
				if (requestBody.contentLength() != -1) {
					logger.log("Content-Length: " + requestBody.contentLength());
				}
			}

			Headers headers = request.headers();
			for (int i = 0, count = headers.size(); i < count; i++) {
				String name = headers.name(i);
				// Skip headers from the request body as they are explicitly logged above.
				if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
					logger.log(name + ": " + headers.value(i));
				}
			}

			if (!logBody || !hasRequestBody) {
				logger.log("--> END " + request.method());
			} else if (bodyHasUnknownEncoding(request.headers())) {
				logger.log("--> END " + request.method() + " (encoded body omitted)");
			} else {
				Buffer buffer = new Buffer();
				requestBody.writeTo(buffer);

				Charset charset = UTF8;
				MediaType contentType = requestBody.contentType();
				if (contentType != null) {
					charset = contentType.charset(UTF8);
				}

				logger.log("");
				if (isPlaintext(buffer)) {
					logger.log(buffer.readString(charset));
					logger.log("--> END " + request.method()
						+ " (" + requestBody.contentLength() + "-byte body)");
				} else {
					logger.log("--> END " + request.method() + " (binary "
						+ requestBody.contentLength() + "-byte body omitted)");
				}
			}
		}

		long startNs = System.nanoTime();
		Response response;
		try {
			response = chain.proceed(request);
		} catch (Exception e) {
			logger.log("<-- HTTP FAILED: " + e);
			throw e;
		}
		long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

		ResponseBody responseBody = response.body();
		long contentLength = responseBody.contentLength();
		String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
		logger.log("<-- "
			+ response.code()
			+ (response.message().isEmpty() ? "" : ' ' + response.message())
			+ ' ' + response.request().url()
			+ " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');

		if (logHeaders) {
			Headers headers = response.headers();
			int count = headers.size();
			for (int i = 0; i < count; i++) {
				logger.log(headers.name(i) + ": " + headers.value(i));
			}

			if (!logBody || !HttpHeaders.hasBody(response)) {
				logger.log("<-- END HTTP");
			} else if (bodyHasUnknownEncoding(response.headers())) {
				logger.log("<-- END HTTP (encoded body omitted)");
			} else {
				BufferedSource source = responseBody.source();
				// Buffer the entire body.
				source.request(Long.MAX_VALUE);
				Buffer buffer = source.buffer();

				Long gzippedLength = null;
				if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
					gzippedLength = buffer.size();
					GzipSource gzippedResponseBody = null;
					try {
						gzippedResponseBody = new GzipSource(buffer.clone());
						buffer = new Buffer();
						buffer.writeAll(gzippedResponseBody);
					} finally {
						if (gzippedResponseBody != null) {
							gzippedResponseBody.close();
						}
					}
				}

				Charset charset = UTF8;
				MediaType contentType = responseBody.contentType();
				if (contentType != null) {
					charset = contentType.charset(UTF8);
				}

				if (!isPlaintext(buffer)) {
					logger.log("");
					logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
					return response;
				}

				if (contentLength != 0) {
					logger.log("");
					logger.log(buffer.clone().readString(charset));
				}

				if (gzippedLength != null) {
					logger.log("<-- END HTTP (" + buffer.size() + "-byte, "
						+ gzippedLength + "-gzipped-byte body)");
				} else {
					logger.log("<-- END HTTP (" + buffer.size() + "-byte body)");
				}
			}
		}

		return response;
	}

	/**
	 * Returns true if the body in question probably contains human readable text. Uses a small sample
	 * of code points to detect unicode control characters commonly used in binary file signatures.
	 */
	private static boolean isPlaintext(Buffer buffer) {
		try {
			Buffer prefix = new Buffer();
			long byteCount = buffer.size() < 64 ? buffer.size() : 64;
			buffer.copyTo(prefix, 0, byteCount);
			for (int i = 0; i < 16; i++) {
				if (prefix.exhausted()) {
					break;
				}
				int codePoint = prefix.readUtf8CodePoint();
				if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
					return false;
				}
			}
			return true;
		} catch (EOFException e) {
			// Truncated UTF-8 sequence.
			return false;
		}
	}

	private boolean bodyHasUnknownEncoding(Headers headers) {
		String contentEncoding = headers.get("Content-Encoding");
		return contentEncoding != null
			&& !"identity".equalsIgnoreCase(contentEncoding)
			&& !"gzip".equalsIgnoreCase(contentEncoding);
	}
}
