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
import lombok.AllArgsConstructor;
import net.dreamlu.mica.core.function.CheckedFunction;
import net.dreamlu.mica.core.utils.Exceptions;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 异常时的 Response 响应体
 *
 * @author dream.lu
 */
@AllArgsConstructor
public class ErrorResponse implements ResponseSpec {
	private final Request request;
	private final IOException exception;

	@Override
	public int code() {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public String message() {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public boolean isRedirect() {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public ResponseSpec onFailed(BiConsumer<Request, IOException> consumer) {
		consumer.accept(request, exception);
		return this;
	}

	@Override
	public Headers headers() {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public List<Cookie> cookies() {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public String asString() {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public byte[] asBytes() {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public <T> T onStream(CheckedFunction<InputStream, T> function) {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public JsonNode asJsonNode() {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public <T> T asValue(Class<T> valueType) {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public <T> T asValue(TypeReference<?> typeReference) {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public <T> List<T> asList(Class<T> valueType) {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public <K, V> Map<K, V> asMap(Class<?> keyClass, Class<?> valueType) {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public <V> Map<String, V> asMap(Class<?> valueType) {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public <T> T asDomValue(Class<T> valueType) {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public <T> List<T> asDomList(Class<T> valueType) {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public void toFile(File file) {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public void toFile(Path path) {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public MediaType contentType() {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public long contentLength() {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public Request rawRequest() {
		return this.request;
	}

	@Override
	public Response rawResponse() {
		throw Exceptions.unchecked(exception);
	}

	@Override
	public ResponseBody rawBody() {
		throw Exceptions.unchecked(exception);
	}

}
