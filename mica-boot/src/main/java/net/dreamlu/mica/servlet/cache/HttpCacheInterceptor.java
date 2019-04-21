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

package net.dreamlu.mica.servlet.cache;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.StringPool;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Clock;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Http cache拦截器
 *
 * @author L.cm
 */
@Slf4j
@AllArgsConstructor
public class HttpCacheInterceptor extends HandlerInterceptorAdapter {
	private final HttpCacheService httpCacheService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 非控制器请求直接跳出
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		// http cache 针对 HEAD 和 GET 请求
		String method = request.getMethod();
		HttpMethod httpMethod = HttpMethod.resolve(method);
		if (httpMethod == null) {
			return true;
		}
		List<HttpMethod> allowList = Arrays.asList(HttpMethod.HEAD, HttpMethod.GET);
		if (allowList.indexOf(httpMethod) == -1) {
			return true;
		}
		// 处理HttpCacheAble
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		HttpCacheAble cacheAble = handlerMethod.getMethodAnnotation(HttpCacheAble.class);
		if (cacheAble == null) {
			return true;
		}

		// 最后修改时间
		long ims = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
		long now = Clock.systemUTC().millis();
		// 缓存时间,秒
		long maxAge = cacheAble.maxAge();
		// 缓存时间,毫秒
		long maxAgeMicros = TimeUnit.SECONDS.toMillis(maxAge);
		String cacheKey = request.getRequestURI() + StringPool.QUESTION_MARK + request.getQueryString();
		// 后端可控制http-cache超时
		boolean hasCache = httpCacheService.get(cacheKey);
		// 如果header头没有过期
		if (hasCache && ims + maxAgeMicros > now) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			response.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=" + maxAge);
			response.addDateHeader(HttpHeaders.EXPIRES, ims + maxAgeMicros);
			response.addDateHeader(HttpHeaders.LAST_MODIFIED, ims);
			log.info("{} 304 {}", method, request.getRequestURI());
			return false;
		}
		response.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=" + maxAge);
		response.addDateHeader(HttpHeaders.EXPIRES, now + maxAgeMicros);
		response.addDateHeader(HttpHeaders.LAST_MODIFIED, now);
		httpCacheService.set(cacheKey);
		return super.preHandle(request, response, handler);
	}

}
