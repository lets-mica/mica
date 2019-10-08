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

import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Map;

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
		RequestBody fileBody = RequestBody.create(null, file);
		return add(name, filename, fileBody);
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

	public HttpRequest build() {
		formBuilder.setType(MultipartBody.FORM);
		MultipartBody formBody = formBuilder.build();
		this.request.multipartForm(formBody);
		return this.request;
	}

	public Exchange execute() {
		return this.build().execute();
	}

	public AsyncCall async() {
		return this.build().async();
	}
}
