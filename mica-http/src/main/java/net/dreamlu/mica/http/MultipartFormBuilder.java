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

import net.dreamlu.mica.core.utils.Exceptions;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.Buffer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 表单构造器
 *
 * @author L.cm
 */
public class MultipartFormBuilder {
	private final HttpRequest request;
	private final MultipartBody.Builder formBuilder;

	MultipartFormBuilder(HttpRequest request) {
		this.request = request;
		this.formBuilder = new MultipartBody.Builder();
	}

	public MultipartFormBuilder add(String name, @Nullable Object value) {
		this.formBuilder.addFormDataPart(name, HttpRequest.handleValue(value));
		return this;
	}

	public MultipartFormBuilder addMap(@Nullable Map<String, Object> formMap) {
		if (formMap != null && !formMap.isEmpty()) {
			formMap.forEach(this::add);
		}
		return this;
	}

	public MultipartFormBuilder add(String name, File file) {
		String fileName = file.getName();
		return add(name, fileName, file);
	}

	public MultipartFormBuilder add(String name, @Nullable String filename, File file) {
		RequestBody fileBody = RequestBody.create(file, null);
		return add(name, filename, fileBody);
	}

	public MultipartFormBuilder add(String name, @Nonnull String filename, byte[] bytes) {
		RequestBody fileBody = RequestBody.create(bytes, null);
		return add(name, filename, fileBody);
	}

	public MultipartFormBuilder add(String name, @Nonnull String filename, InputStream stream) {
		try (Buffer buffer = new Buffer()) {
			buffer.readFrom(stream);
			return add(name, filename, buffer.readByteArray());
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		} finally {
			Util.closeQuietly(stream);
		}
	}

	public MultipartFormBuilder add(String name, @Nullable String filename, RequestBody fileBody) {
		this.formBuilder.addFormDataPart(name, filename, fileBody);
		return this;
	}

	public MultipartFormBuilder add(RequestBody body) {
		this.formBuilder.addPart(body);
		return this;
	}

	public MultipartFormBuilder add(@Nullable Headers headers, RequestBody body) {
		this.formBuilder.addPart(headers, body);
		return this;
	}

	public MultipartFormBuilder add(MultipartBody.Part part) {
		this.formBuilder.addPart(part);
		return this;
	}

	public MultipartFormBuilder addList(List<MultipartBody.Part> partList) {
		for (MultipartBody.Part part : partList) {
			this.formBuilder.addPart(part);
		}
		return this;
	}

	public MultipartFormBuilder add(Consumer<MultipartBody.Builder> consumer) {
		consumer.accept(this.formBuilder);
		return this;
	}

	public HttpRequest build() {
		return this.request.multipartForm(this.formBuilder.setType(MultipartBody.FORM).build());
	}

	public Exchange execute() {
		return this.build().execute();
	}

	public AsyncExchange async() {
		return this.build().async();
	}

}
