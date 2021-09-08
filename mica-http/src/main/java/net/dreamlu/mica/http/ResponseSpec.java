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
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import net.dreamlu.mica.core.utils.JsonUtil;
import okhttp3.*;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
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
	 * Returns the HTTP is not successful.
	 *
	 * @return boolean
	 */
	default boolean isNotOk() {
		return !isOk();
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
	 * Returns body String.
	 *
	 * @param charset Charset
	 * @return body String
	 */
	String asString(Charset charset);

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
	 * jackson json path 语法读取节点
	 *
	 * @param jsonPtrExpr json path 表达式
	 * @return JsonNode
	 */
	default JsonNode atJsonPath(String jsonPtrExpr) {
		return this.asJsonNode().at(jsonPtrExpr);
	}

	/**
	 * jackson json path 语法读取节点
	 *
	 * @param jsonPtrExpr json path 表达式
	 * @param valueType   value value type
	 * @return JsonNode
	 */
	default <T> T atJsonPathValue(String jsonPtrExpr, Class<T> valueType) {
		return JsonUtil.convertValue(atJsonPath(jsonPtrExpr), valueType);
	}

	/**
	 * jackson json path 语法读取节点
	 *
	 * @param jsonPtrExpr   json path 表达式
	 * @param typeReference value Type Reference
	 * @return JsonNode
	 */
	default <T> T atJsonPathValue(String jsonPtrExpr, TypeReference<T> typeReference) {
		return JsonUtil.convertValue(atJsonPath(jsonPtrExpr), typeReference);
	}

	/**
	 * jackson json path 语法读取节点
	 *
	 * @param jsonPtrExpr json path 表达式
	 * @param valueType   value value type
	 * @return List
	 */
	default <T> List<T> atJsonPathList(String jsonPtrExpr, Class<T> valueType) {
		CollectionLikeType collectionLikeType = JsonUtil.getListType(valueType);
		return JsonUtil.convertValue(atJsonPath(jsonPtrExpr), collectionLikeType);
	}

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
	 * toFile.
	 *
	 * @param file File
	 * @return File
	 */
	File toFile(File file);

	/**
	 * toFile.
	 *
	 * @param path Path
	 * @return Path
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
