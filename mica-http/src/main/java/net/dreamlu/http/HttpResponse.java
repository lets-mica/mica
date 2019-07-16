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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.mica.core.utils.JsonUtil;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * ok http 封装，相应结构体
 *
 * @author L.cm
 */
public class HttpResponse {
	private final Response response;
	private final ResponseBody body;

	HttpResponse(final Response response) {
		this.response = response;
		this.body = response.body();
	}

	public boolean isOk() {
		return response.isSuccessful();
	}

	public String asString() {
		try {
			return body.string();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] asBytes() {
		try {
			return body.bytes();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public InputStream asStream() {
		return body.byteStream();
	}

	public JsonNode asJsonNode() {
		return JsonUtil.readTree(this.asStream());
	}

	public <T> T asObject(Class<T> valueType) {
		return JsonUtil.readValue(this.asStream(), valueType);
	}

	public <T> T asObject(TypeReference<?> typeReference) {
		return JsonUtil.readValue(this.asStream(), typeReference);
	}

	public <T> List<T> asList(Class<T> valueType) {
		return JsonUtil.readList(this.asStream(), valueType);
	}

	public <K, V> Map<K, V> asMap(Class<?> keyClass, Class<?> valueClass) {
		return JsonUtil.readMap(this.asStream(), keyClass, valueClass);
	}

	public <V> Map<String, V> asMap(Class<?> valueClass) {
		return JsonUtil.readMap(this.asStream(), String.class, valueClass);
	}

	public Document asDocument() {
		try {
			return DataUtil.load(this.asStream(), StandardCharsets.UTF_8.name(), "");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void toFile(File file) {
		toFile(file.toPath());
	}

	public void toFile(Path path) {
		try {
			Files.copy(this.asStream(), path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public MediaType contentType() {
		return body.contentType();
	}

	public long contentLength() {
		return body.contentLength();
	}

	public Response rawResponse() {
		return response;
	}

	public ResponseBody rawBody() {
		return body;
	}

	@Override
	public String toString() {
		return response.toString();
	}
}
