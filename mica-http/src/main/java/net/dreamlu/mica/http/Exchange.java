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
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.utils.Exceptions;
import okhttp3.Call;
import okhttp3.Request;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Exchange
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class Exchange {
	private BiConsumer<Request, IOException> failedBiConsumer = (r, e) -> {};
	private final Call call;

	public Exchange onFailed(BiConsumer<Request, IOException> failConsumer) {
		this.failedBiConsumer = failConsumer;
		return this;
	}

	public <R> R onResponse(Function<ResponseSpec, R> func) {
		try (HttpResponse response = new HttpResponse(call.execute())) {
			return func.apply(response);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Nullable
	public <R> R onSuccess(Function<ResponseSpec, R> func) {
		try (HttpResponse response = new HttpResponse(call.execute())) {
			return func.apply(response);
		} catch (IOException e) {
			failedBiConsumer.accept(call.request(), e);
			return null;
		}
	}

	@Nullable
	public <R> R onSuccessful(Function<ResponseSpec, R> func) {
		try (HttpResponse response = new HttpResponse(call.execute())) {
			if (response.isOk()) {
				return func.apply(response);
			} else {
				failedBiConsumer.accept(call.request(), new IOException(response.toString()));
			}
		} catch (IOException e) {
			failedBiConsumer.accept(call.request(), e);
		}
		return null;
	}

	public <R> Optional<R> onSuccessOpt(Function<ResponseSpec, R> func) {
		return Optional.ofNullable(this.onSuccess(func));
	}

	public <R> Optional<R> onSuccessfulOpt(Function<ResponseSpec, R> func) {
		return Optional.ofNullable(this.onSuccessful(func));
	}

	/**
	 * Returns body String.
	 *
	 * @return body String
	 */
	public String asString() {
		return onResponse(ResponseSpec::asString);
	}

	/**
	 * Returns body to byte arrays.
	 *
	 * @return byte arrays
	 */
	public byte[] asBytes() {
		return onResponse(ResponseSpec::asBytes);
	}

	/**
	 * Returns body to JsonNode.
	 *
	 * @return JsonNode
	 */
	public JsonNode asJsonNode() {
		return onResponse(ResponseSpec::asJsonNode);
	}

	/**
	 * Returns body to Object.
	 *
	 * @param valueType value value type
	 * @return Object
	 */
	public <T> T asValue(Class<T> valueType) {
		return onResponse(responseSpec -> responseSpec.asValue(valueType));
	}

	/**
	 * Returns body to Object.
	 *
	 * @param typeReference value Type Reference
	 * @return Object
	 */
	public <T> T asValue(TypeReference<T> typeReference) {
		return onResponse(responseSpec -> responseSpec.asValue(typeReference));
	}

	/**
	 * Returns body to List.
	 *
	 * @param valueType value type
	 * @return List
	 */
	public <T> List<T> asList(Class<T> valueType) {
		return onResponse(responseSpec -> responseSpec.asList(valueType));
	}

	/**
	 * Returns body to Map.
	 *
	 * @param keyClass  key type
	 * @param valueType value type
	 * @return Map
	 */
	public <K, V> Map<K, V> asMap(Class<?> keyClass, Class<?> valueType) {
		return onResponse(responseSpec -> responseSpec.asMap(keyClass, valueType));
	}

	/**
	 * Returns body to Map.
	 *
	 * @param valueType value 类型
	 * @return Map
	 */
	public <V> Map<String, V> asMap(Class<?> valueType) {
		return onResponse(responseSpec -> responseSpec.asMap(valueType));
	}

	/**
	 * 将 xml、heml 转成对象
	 *
	 * @param valueType 对象类
	 * @param <T>       泛型
	 * @return 对象
	 */
	public <T> T asDomValue(Class<T> valueType) {
		return onResponse(responseSpec -> responseSpec.asDomValue(valueType));
	}

	/**
	 * 将 xml、heml 转成对象
	 *
	 * @param valueType 对象类
	 * @param <T>       泛型
	 * @return 对象集合
	 */
	public <T> List<T> asDomList(Class<T> valueType) {
		return onResponse(responseSpec -> responseSpec.asDomList(valueType));
	}

	/**
	 * toFile.
	 *
	 * @param file File
	 */
	public File toFile(File file) {
		return onResponse(responseSpec -> responseSpec.toFile(file));
	}

	/**
	 * toFile.
	 *
	 * @param path Path
	 */
	public Path toFile(Path path) {
		return onResponse(responseSpec -> responseSpec.toFile(path));
	}

}
