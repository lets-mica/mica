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

package net.dreamlu.mica.reactive.version;

import net.dreamlu.mica.annotation.ApiVersion;
import net.dreamlu.mica.annotation.UrlVersion;
import net.dreamlu.mica.common.version.MicaMediaType;
import net.dreamlu.mica.core.utils.StringPool;
import net.dreamlu.mica.core.utils.StringUtil;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * url版本号处理
 *
 * @author L.cm
 */
public class MicaRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		RequestMappingInfo mappinginfo = super.getMappingForMethod(method, handlerType);
		if (mappinginfo != null) {
			RequestMappingInfo apiVersionMappingInfo = getApiVersionMappingInfo(method, handlerType);
			return apiVersionMappingInfo == null ? mappinginfo : apiVersionMappingInfo.combine(mappinginfo);
		}
		return mappinginfo;
	}

	@Nullable
	private RequestMappingInfo getApiVersionMappingInfo(Method method, Class<?> handlerType) {
		// url 上的版本，优先获取方法上的版本
		UrlVersion urlVersion = AnnotatedElementUtils.findMergedAnnotation(method, UrlVersion.class);
		// 再次尝试类上的版本
		if (urlVersion == null || StringUtil.isBlank(urlVersion.value())) {
			urlVersion = AnnotatedElementUtils.findMergedAnnotation(handlerType, UrlVersion.class);
		}
		// Media Types 版本信息
		ApiVersion apiVersion = AnnotatedElementUtils.findMergedAnnotation(method, ApiVersion.class);
		// 再次尝试类上的版本
		if (apiVersion == null || StringUtil.isBlank(apiVersion.value())) {
			apiVersion = AnnotatedElementUtils.findMergedAnnotation(handlerType, ApiVersion.class);
		}
		boolean nonUrlVersion = urlVersion == null || StringUtil.isBlank(urlVersion.value());
		boolean nonApiVersion = apiVersion == null || StringUtil.isBlank(apiVersion.value());
		// 先判断同时不纯在
		if (nonUrlVersion && nonApiVersion) {
			return null;
		}
		// 如果 header 版本不存在
		RequestMappingInfo.Builder mappingInfoBuilder;
		if (nonApiVersion) {
			mappingInfoBuilder = RequestMappingInfo.paths(urlVersion.value());
		} else {
			mappingInfoBuilder = RequestMappingInfo.paths(StringPool.EMPTY);
		}
		// 如果url版本不存在
		if (nonUrlVersion) {
			String vsersionMediaTypes = new MicaMediaType(apiVersion.value()).toString();
			mappingInfoBuilder.produces(vsersionMediaTypes);
		}
		return mappingInfoBuilder.build();
	}

	@Override
	protected void handlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
		// 打印路由信息 spring boot 2.1 去掉了这个 日志的打印
		if (logger.isInfoEnabled()) {
			for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
				RequestMappingInfo mapping = entry.getKey();
				HandlerMethod handlerMethod = entry.getValue();
				logger.info("Mapped \"" + mapping + "\" onto " + handlerMethod);
			}
		}
		super.handlerMethodsInitialized(handlerMethods);
	}
}
