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

package net.dreamlu.mica.prometheus.api.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.prometheus.api.config.MicaPrometheusProperties;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * prometheus http sd basic auth 拦截器
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class BasicAuthHandlerInterceptor implements AsyncHandlerInterceptor {
	private static final String BASIC_PREFIX = "Basic ";
	private static final String REALM = "Basic realm=\"Prometheus SD\"";
	private final MicaPrometheusProperties.BasicAuth basicAuth;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (!basicAuth.isEnabled()) {
			return true;
		}
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith(BASIC_PREFIX)) {
			try {
				String credentials = new String(Base64.getDecoder()
					.decode(authHeader.substring(BASIC_PREFIX.length())), StandardCharsets.UTF_8);
				int colonIndex = credentials.indexOf(':');
				if (colonIndex > 0) {
					String username = credentials.substring(0, colonIndex);
					String password = credentials.substring(colonIndex + 1);
					if (basicAuth.getUsername().equals(username) && basicAuth.getPassword().equals(password)) {
						return true;
					}
				}
			} catch (IllegalArgumentException ignored) {
				// 非法的 base64 走未授权分支
			}
		}
		response.setHeader("WWW-Authenticate", REALM);
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		return false;
	}

}