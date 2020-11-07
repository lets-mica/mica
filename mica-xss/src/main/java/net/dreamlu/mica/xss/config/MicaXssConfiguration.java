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

package net.dreamlu.mica.xss.config;

import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.xss.core.FormXssClean;
import net.dreamlu.mica.xss.core.JacksonXssClean;
import net.dreamlu.mica.xss.core.XssCleanInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * jackson xss 配置
 *
 * @author L.cm
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MicaXssProperties.class)
@ConditionalOnProperty(value = "mica.xss.enabled", havingValue = "true", matchIfMissing = true)
public class MicaXssConfiguration implements WebMvcConfigurer {
	private final MicaXssProperties xssProperties;

	@Bean
	public FormXssClean formXssClean() {
		return new FormXssClean();
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer xssJacksonCustomizer() {
		return builder -> builder.deserializerByType(String.class, new JacksonXssClean());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> patterns = xssProperties.getPathPatterns();
		if (patterns.isEmpty()) {
			patterns.add("/**");
		}
		XssCleanInterceptor interceptor = new XssCleanInterceptor(xssProperties);
		registry.addInterceptor(interceptor)
			.addPathPatterns(patterns)
			.excludePathPatterns(xssProperties.getExcludePatterns())
			.order(Ordered.LOWEST_PRECEDENCE);
	}

}
