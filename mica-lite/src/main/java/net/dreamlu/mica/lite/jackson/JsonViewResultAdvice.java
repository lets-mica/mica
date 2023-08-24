/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
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

package net.dreamlu.mica.lite.jackson;

import com.fasterxml.jackson.annotation.JsonView;
import net.dreamlu.mica.core.result.R;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 使 jackson view 在使用 R 了之后生效
 *
 * @author L.cm
 */
@RestControllerAdvice
public class JsonViewResultAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.hasMethodAnnotation(JsonView.class);
	}

	@Override
	public Object beforeBodyWrite(Object body,
								  MethodParameter returnType,
								  MediaType selectedContentType,
								  Class<? extends HttpMessageConverter<?>> selectedConverterType,
								  ServerHttpRequest request,
								  ServerHttpResponse response) {
		// 如果返回的是 mica 中的 R
		if (body instanceof R<?>) {
			JsonView jsonView = returnType.getMethodAnnotation(JsonView.class);
			// 理论上是不会出现 null 的，为了编辑器的代码检查添加了判断
			if (jsonView == null) {
				return body;
			}
			// 没有标记 jsonView class
			Class<?>[] jsonViewClassArray = jsonView.value();
			if (jsonViewClassArray == null || jsonViewClassArray.length == 0) {
				return body;
			}
			// 处理 json view
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(body);
			mappingJacksonValue.setSerializationView(jsonView.value()[0]);
			return mappingJacksonValue;
		}
		return body;
	}
}
