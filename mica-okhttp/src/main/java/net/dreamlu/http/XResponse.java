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

import java.io.IOException;
import java.util.List;

/**
 * ok http 封装，相应结构体
 *
 * @author L.cm
 */
public class XResponse {
	private final Response response;
	private final ResponseBody body;

	XResponse(final Response response) {
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

	public JsonNode asJsonNode() {
		return JsonUtil.readTree(this.asBytes());
	}

	public <T> T asObject(Class<T> valueType) {
		return JsonUtil.readValue(this.asBytes(), valueType);
	}

	public <T> T asObject(TypeReference<?> typeReference) {
		return JsonUtil.readValue(this.asBytes(), typeReference);
	}

	public <T> List<T> asList(Class<T> valueType) {
		return JsonUtil.readList(this.asBytes(), valueType);
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
