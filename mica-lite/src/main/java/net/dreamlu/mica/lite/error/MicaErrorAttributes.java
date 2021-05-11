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

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.exception.ServiceException;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.result.SystemCode;
import net.dreamlu.mica.core.utils.BeanUtil;
import net.dreamlu.mica.core.utils.StringUtil;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.RequestDispatcher;
import java.util.Map;
import java.util.Optional;

/**
 * 全局异常处理
 *
 * @author L.cm
 */
@Slf4j
@SuppressWarnings("unchecked")
public class MicaErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		// 请求地址
		String requestUrl = this.getAttr(webRequest, RequestDispatcher.ERROR_REQUEST_URI);
		if (StringUtil.isBlank(requestUrl)) {
			requestUrl = this.getAttr(webRequest, RequestDispatcher.FORWARD_REQUEST_URI);
		}
		// status code
		Integer status = this.getAttr(webRequest, RequestDispatcher.ERROR_STATUS_CODE);
		// error
		Throwable error = getError(webRequest);
		log.error("URL:{} error status:{}", requestUrl, status, error);
		R<Object> result;
		if (error instanceof ServiceException) {
			result = ((ServiceException) error).getResult();
			result = Optional.ofNullable(result).orElse(R.fail(SystemCode.FAILURE));
		} else {
			result = R.fail(SystemCode.FAILURE, "System error status:" + status);
		}
		return BeanUtil.toMap(result);
	}

	@Nullable
	private <T> T getAttr(WebRequest webRequest, String name) {
		return (T) webRequest.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
	}

}
