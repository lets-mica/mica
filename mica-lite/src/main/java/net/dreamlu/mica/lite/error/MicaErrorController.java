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

package net.dreamlu.mica.lite.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webmvc.autoconfigure.error.BasicErrorController;
import org.springframework.boot.webmvc.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.JacksonJsonView;
import tools.jackson.databind.json.JsonMapper;

/**
 * 更改 html 请求异常为 ajax，对前后端分离更加友好
 *
 * @author L.cm
 */
public class MicaErrorController extends BasicErrorController {
	private final JsonMapper jsonMapper;

	public MicaErrorController(JsonMapper jsonMapper,
							   ErrorAttributes errorAttributes,
							   ErrorProperties errorProperties) {
		super(errorAttributes, errorProperties);
		this.jsonMapper = jsonMapper;
	}

	@Override
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status = getStatus(request);
		response.setStatus(status.value());
		JacksonJsonView view = new JacksonJsonView(jsonMapper);
		view.setContentType(MediaType.APPLICATION_JSON_VALUE);
		return new ModelAndView(view, getErrorAttributes(request, ErrorAttributeOptions.defaults()));
	}
}
