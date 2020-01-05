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
import okhttp3.*;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 相应接口
 *
 * @author L.cm
 */
public interface ResponseSpec {

	/**
	 * Returns the HTTP code.
	 *
	 * @return code
	 */
	int code();

	/**
	 * Returns the HTTP status message.
	 *
	 * @return message
	 */
	String message();

	/**
	 * Returns the HTTP isSuccessful.
	 *
	 * @return boolean
	 */
	default boolean isOk() {
		return false;
	}

	/**
	 * Returns the is Redirect.
	 *
	 * @return is Redirect
	 */
	boolean isRedirect();

	/**
	 * Returns the Headers.
	 *
	 * @return Headers
	 */
	Headers headers();

	/**
	 * Headers Consumer.
	 *
	 * @param consumer Consumer
	 * @return Headers
	 */
	default ResponseSpec headers(Consumer<Headers> consumer) {
		consumer.accept(this.headers());
		return this;
	}

	/**
	 * Returns the Cookies.
	 *
	 * @return Cookie List
	 */
	List<Cookie> cookies();

	/**
	 * 读取消费 cookie
	 *
	 * @param consumer Consumer
	 * @return ResponseSpec
	 */
	default ResponseSpec cookies(Consumer<List<Cookie>> consumer) {
		consumer.accept(this.cookies());
		return this;
	}

	/**
	 * Returns body String.
	 *
	 * @return body String
	 */
	String asString();

	/**
	 * Returns body to byte arrays.
	 *
	 * @return byte arrays
	 */
	byte[] asBytes();

	/**
	 * Returns body to InputStream.
	 *
	 * @return InputStream
	 */
	InputStream asStream();

	/**
	 * Returns body to JsonNode.
	 *
	 * @return JsonNode
	 */
	JsonNode asJsonNode();

	/**
	 * Returns body to Object.
	 *
	 * @param valueType value value type
	 * @return Object
	 */
	@Nullable
	<T> T asValue(Class<T> valueType);

	/**
	 * Returns body to Object.
	 *
	 * @param typeReference value Type Reference
	 * @return Object
	 */
	@Nullable
	<T> T asValue(TypeReference<T> typeReference);

	/**
	 * Returns body to List.
	 *
	 * @param valueType value type
	 * @return List
	 */
	<T> List<T> asList(Class<T> valueType);

	/**
	 * Returns body to Map.
	 *
	 * @param keyClass  key type
	 * @param valueType value type
	 * @return Map
	 */
	<K, V> Map<K, V> asMap(Class<?> keyClass, Class<?> valueType);

	/**
	 * Returns body to Map.
	 *
	 * @param valueType value 类型
	 * @return Map
	 */
	<V> Map<String, V> asMap(Class<?> valueType);

	/**
	 * 将 xml、heml 转成对象
	 *
	 * @param valueType 对象类
	 * @param <T>       泛型
	 * @return 对象
	 */
	<T> T asDomValue(Class<T> valueType);

	/**
	 * 将 xml、heml 转成对象
	 *
	 * @param valueType 对象类
	 * @param <T>       泛型
	 * @return 对象集合
	 */
	<T> List<T> asDomList(Class<T> valueType);

	/**
	 * toFile.
	 *
	 * @param file File
	 */
	File toFile(File file);

	/**
	 * toFile.
	 *
	 * @param path Path
	 */
	Path toFile(Path path);

	/**
	 * Returns contentType.
	 *
	 * @return contentType
	 */
	@Nullable
	MediaType contentType();

	/**
	 * Returns contentLength.
	 *
	 * @return contentLength
	 */
	long contentLength();

	/**
	 * Returns rawRequest.
	 *
	 * @return Request
	 */
	Request rawRequest();

	/**
	 * rawRequest Consumer.
	 *
	 * @param consumer Consumer
	 * @return ResponseSpec
	 */
	@Nullable
	default ResponseSpec rawRequest(Consumer<Request> consumer) {
		consumer.accept(this.rawRequest());
		return this;
	}

	/**
	 * Returns rawResponse.
	 *
	 * @return Response
	 */
	Response rawResponse();

	/**
	 * rawResponse Consumer.
	 *
	 * @param consumer Consumer
	 * @return Response
	 */
	default ResponseSpec rawResponse(Consumer<Response> consumer) {
		consumer.accept(this.rawResponse());
		return this;
	}

	/**
	 * Returns rawBody.
	 *
	 * @return ResponseBody
	 */
	@Nullable
	ResponseBody rawBody();

	/**
	 * rawBody Consumer.
	 *
	 * @param consumer Consumer
	 * @return ResponseBody
	 */
	@Nullable
	default ResponseSpec rawBody(Consumer<ResponseBody> consumer) {
		consumer.accept(this.rawBody());
		return this;
	}

}
