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
import net.dreamlu.mica.core.utils.JsonUtil;
import okhttp3.*;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * ok http 封装，相应结构体
 *
 * @author L.cm
 */
public class HttpResponse implements ResponseSpec {
	private final Request request;
	private final Response response;
	private final ResponseBody body;
	@Nullable
	private IOException exception;

	HttpResponse(final Response response) {
		this.request = response.request();
		this.response = response;
		this.body = response.body();
	}

	HttpResponse(Request request, IOException exception) {
		this.request = request;
		this.response = null;
		this.body = null;
		this.exception = exception;
	}

	private void checkIfException() {
		if (exception != null) {
			throw new MicaHttpException(exception);
		}
	}

	@Override
	public int code() {
		checkIfException();
		return response.code();
	}

	@Override
	public String message() {
		checkIfException();
		return response.message();
	}

	@Override
	public boolean isOk() {
		return response != null && response.isSuccessful();
	}

	@Override
	public boolean isRedirect() {
		checkIfException();
		return response.isRedirect();
	}

	@Nullable
	public <T> T onResponse(Function<ResponseSpec, T> function) {
		if (this.response != null) {
			return function.apply(this);
		}
		return null;
	}

	public HttpResponse onSuccessful(Consumer<ResponseSpec> consumer) {
		if (this.isOk()) {
			consumer.accept(this);
		}
		return this;
	}

	@Nullable
	public <T> T onSuccess(Function<ResponseSpec, T> function) {
		if (this.isOk()) {
			return function.apply(this);
		}
		return null;
	}

	public <T> Optional<T> onSuccessOpt(Function<ResponseSpec, T> function) {
		if (this.isOk()) {
			return Optional.ofNullable(function.apply(this));
		}
		return Optional.empty();
	}

	public HttpResponse onFailed(BiConsumer<Request, IOException> consumer) {
		if (this.exception != null) {
			consumer.accept(this.request, this.exception);
		}
		return this;
	}

	@Override
	public Headers headers() {
		checkIfException();
		return response.headers();
	}

	public HttpResponse headers(Consumer<Headers> consumer) {
		consumer.accept(this.headers());
		return this;
	}

	@Override
	public List<Cookie> cookies() {
		checkIfException();
		HttpUrl httpUrl = response.request().url();
		return Cookie.parseAll(httpUrl, this.headers());
	}

	public HttpResponse cookies(Consumer<List<Cookie>> consumer) {
		consumer.accept(this.cookies());
		return this;
	}

	public Request request() {
		return this.request;
	}

	@Override
	public Response rawResponse() {
		checkIfException();
		return this.response;
	}

	@Override
	public ResponseBody rawBody() {
		checkIfException();
		return this.body;
	}

	public HttpResponse rawResponse(Consumer<Response> consumer) {
		checkIfException();
		consumer.accept(this.response);
		return this;
	}

	public HttpResponse rawBody(Consumer<ResponseBody> consumer) {
		checkIfException();
		consumer.accept(this.body);
		return this;
	}

	@Override
	public String asString() {
		checkIfException();
		try {
			return body.string();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] asBytes() {
		checkIfException();
		try {
			return body.bytes();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public InputStream asStream() {
		checkIfException();
		return body.byteStream();
	}

	@Override
	public JsonNode asJsonNode() {
		return JsonUtil.readTree(this.asStream());
	}

	@Override
	public <T> T asValue(Class<T> valueType) {
		return JsonUtil.readValue(this.asStream(), valueType);
	}

	@Override
	public <T> T asValue(TypeReference<?> typeReference) {
		return JsonUtil.readValue(this.asStream(), typeReference);
	}

	@Override
	public <T> List<T> asList(Class<T> valueType) {
		return JsonUtil.readList(this.asStream(), valueType);
	}

	@Override
	public <K, V> Map<K, V> asMap(Class<?> keyClass, Class<?> valueType) {
		return JsonUtil.readMap(this.asStream(), keyClass, valueType);
	}

	@Override
	public <V> Map<String, V> asMap(Class<?> valueType) {
		return JsonUtil.readMap(this.asStream(), String.class, valueType);
	}

	@Override
	public Document asDocument() {
		try {
			return DataUtil.load(this.asStream(), StandardCharsets.UTF_8.name(), "");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T asDomValue(Class<T> valueType) {
		return DomMapper.readValue(this.asDocument(), valueType);
	}

	@Override
	public <T> List<T> asDomList(Class<T> valueType) {
		return DomMapper.readList(this.asDocument(), valueType);
	}


	@Override
	public void toFile(File file) {
		toFile(file.toPath());
	}

	@Override
	public void toFile(Path path) {
		try {
			Files.copy(this.asStream(), path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public MediaType contentType() {
		checkIfException();
		return body.contentType();
	}

	@Override
	public long contentLength() {
		checkIfException();
		return body.contentLength();
	}

	@Override
	public String toString() {
		checkIfException();
		return response.toString();
	}

}
