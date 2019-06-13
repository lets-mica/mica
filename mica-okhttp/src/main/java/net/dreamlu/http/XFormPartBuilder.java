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

import okhttp3.*;

import javax.annotation.Nullable;
import java.io.File;

/**
 * 表单构造器
 *
 * @author L.cm
 */
public class XFormPartBuilder {
	private final XRequest request;
	private final MultipartBody.Builder formBuilder;

	XFormPartBuilder(XRequest request) {
		this.request = request;
		this.formBuilder = new MultipartBody.Builder();
	}

	public XFormPartBuilder add(String name, String value) {
		this.formBuilder.addFormDataPart(name, value);
		return this;
	}

	public XFormPartBuilder add(String name, File file) {
		String fileName = file.getName();
		return add(name, fileName, file);
	}

	public XFormPartBuilder add(String name, @Nullable String filename, File file) {
		RequestBody fileBody = RequestBody.create(null, file);
		return add(name, filename, fileBody);
	}

	public XFormPartBuilder add(String name, @Nullable String filename, RequestBody fileBody) {
		this.formBuilder.addFormDataPart(name, filename, fileBody);
		return this;
	}

	public XFormPartBuilder add(RequestBody body) {
		this.formBuilder.addPart(body);
		return this;
	}

	public XFormPartBuilder add(@Nullable Headers headers, RequestBody body) {
		this.formBuilder.addPart(headers, body);
		return this;
	}

	public XFormPartBuilder add(MultipartBody.Part part) {
		this.formBuilder.addPart(part);
		return this;
	}

	public XRequest build() {
		formBuilder.setType(MultipartBody.FORM);
		MultipartBody formBody = formBuilder.build();
		this.request.formPart(formBody);
		return this.request;
	}

	public XResponse execute() {
		return this.build().execute();
	}
}
