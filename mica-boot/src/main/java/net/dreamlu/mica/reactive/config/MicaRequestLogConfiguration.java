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

package net.dreamlu.mica.reactive.config;

import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.utils.StringPool;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.launcher.MicaLogLevel;
import net.dreamlu.mica.props.MicaRequestLogProperties;
import net.dreamlu.mica.reactive.logger.ReactiveRequestLogFilter;
import net.dreamlu.mica.reactive.logger.RequestLogExclusiveRule;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

/**
 * mica webflux 日志拦截器
 *
 * @author L.cm
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(value = MicaLogLevel.REQ_LOG_PROPS_PREFIX + ".enabled", havingValue = "true", matchIfMissing = true)
public class MicaRequestLogConfiguration {
	private final MicaRequestLogProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public ReactiveRequestLogFilter reactiveRequestLogFilter(RequestLogExclusiveRule exclusiveRule) {
		return new ReactiveRequestLogFilter(properties, exclusiveRule);
	}

	/**
	 * 默认的请求日志排除规则
	 *
	 * @author L.cm
	 */
	@Configuration
	@ConditionalOnMissingBean(RequestLogExclusiveRule.class)
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	public static class DefaultRequestLogExclusiveRule implements RequestLogExclusiveRule {

		@Override
		public boolean excluded(String path) {
			return path.contains(StringPool.DOT) && !path.toLowerCase().endsWith(".json");
		}
	}

	/**
	 * 排除 /actuator
	 *
	 * @author L.cm
	 */
	@Configuration
	@ConditionalOnClass(WebEndpointProperties.class)
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	@RequiredArgsConstructor
	public static class WebEndpointRequestLogExclusiveRule extends DefaultRequestLogExclusiveRule {
		private static final String STATIC_PATH_PATTERN = "/**";
		private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
		private final WebEndpointProperties properties;
		private final WebFluxProperties webFluxProperties;

		@Override
		public boolean excluded(String path) {
			// 对 /actuator 排除
			String actuatorBasePath = properties.getBasePath();
			if (StringUtil.isNotBlank(actuatorBasePath) && path.startsWith(actuatorBasePath)) {
				return true;
			}
			// 如果设置了静态目录 例如：/static, 对静态文件排除
			String staticPathPattern = webFluxProperties.getStaticPathPattern();
			if (!STATIC_PATH_PATTERN.equals(staticPathPattern) &&
				PATH_MATCHER.match(staticPathPattern, path)) {
				return true;
			}
			return super.excluded(path);
		}
	}
}
