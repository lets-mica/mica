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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.mica.core.function.CheckedFunction;
import net.dreamlu.mica.core.utils.Exceptions;
import net.dreamlu.mica.core.utils.JsonUtil;
import okhttp3.*;
import okhttp3.internal.Util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * ok http 封装，相应结构体
 *
 * @author L.cm
 */
public class HttpResponse implements ResponseSpec {
	private final Request request;
	private final Response response;

	HttpResponse(final Response response) {
		this.request = response.request();
		this.response = response;
	}

	@Override
	public int code() {
		return response.code();
	}

	@Override
	public String message() {
		return response.message();
	}

	@Override
	public boolean isOk() {
		return response.isSuccessful();
	}

	@Override
	public boolean isRedirect() {
		return response.isRedirect();
	}

	@Override
	public Headers headers() {
		return response.headers();
	}

	@Override
	public List<Cookie> cookies() {
		HttpUrl httpUrl = response.request().url();
		return Cookie.parseAll(httpUrl, this.headers());
	}

	@Override
	public Request rawRequest() {
		return this.request;
	}

	@Override
	public Response rawResponse() {
		return this.response;
	}

	@Nonnull
	@Override
	public ResponseBody rawBody() {
		return ifNullBodyToEmpty(this.response.body());
	}

	@Override
	public String asString() {
		try (ResponseBody body = rawBody()) {
			return body.string();
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public byte[] asBytes() {
		try (ResponseBody body = rawBody()) {
			return body.bytes();
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public <T> T onStream(CheckedFunction<InputStream, T> function) {
		try (
			ResponseBody body = rawBody();
			InputStream input = body.byteStream()
		) {
			return function.apply(input);
		} catch (Throwable e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public JsonNode asJsonNode() {
		return JsonUtil.readTree(asBytes());
	}

	@Override
	public <T> T asValue(Class<T> valueType) {
		return JsonUtil.readValue(asBytes(), valueType);
	}

	@Override
	public <T> T asValue(TypeReference<?> typeReference) {
		return JsonUtil.readValue(asBytes(), typeReference);
	}

	@Override
	public <T> List<T> asList(Class<T> valueType) {
		return JsonUtil.readList(asBytes(), valueType);
	}

	@Override
	public <K, V> Map<K, V> asMap(Class<?> keyClass, Class<?> valueType) {
		return JsonUtil.readMap(asBytes(), keyClass, valueType);
	}

	@Override
	public <V> Map<String, V> asMap(Class<?> valueType) {
		return this.asMap(String.class, valueType);
	}

	@Override
	public <T> T asDomValue(Class<T> valueType) {
		return this.onStream(input -> DomMapper.readValue(input, valueType));
	}

	@Override
	public <T> List<T> asDomList(Class<T> valueType) {
		return this.onStream(input -> DomMapper.readList(input, valueType));
	}

	@Override
	public void toFile(File file) {
		toFile(file.toPath());
	}

	@Override
	public void toFile(Path path) {
		this.onStream(input -> Files.copy(input, path));
	}

	@Override
	public MediaType contentType() {
		return this.rawBody().contentType();
	}

	@Override
	public long contentLength() {
		return this.rawBody().contentLength();
	}

	@Override
	public String toString() {
		return response.toString();
	}

	private static ResponseBody ifNullBodyToEmpty(@Nullable ResponseBody body) {
		return body == null ? Util.EMPTY_RESPONSE : body;
	}

	@Override
	public void close() {
		Util.closeQuietly(this.rawBody());
	}
}
