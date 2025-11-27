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
import net.dreamlu.mica.core.spring.SpringContextUtil;
import net.dreamlu.mica.xss.core.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tools.jackson.databind.module.SimpleModule;

import java.util.ArrayList;
import java.util.List;

/**
 * jackson xss 配置
 *
 * @author L.cm
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(MicaXssProperties.class)
@ConditionalOnProperty(
	prefix = MicaXssProperties.PREFIX,
	name = "enabled",
	havingValue = "true",
	matchIfMissing = true
)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MicaXssConfiguration implements WebMvcConfigurer {
	private final MicaXssProperties xssProperties;

	@Bean
	@ConditionalOnMissingBean
	public SpringContextUtil springContextUtil() {
		return new SpringContextUtil();
	}

	@Bean
	@ConditionalOnMissingBean
	public XssCleaner xssCleaner(MicaXssProperties properties) {
		return new DefaultXssCleaner(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	public FormXssClean formXssClean(MicaXssProperties properties,
									 XssCleaner xssCleaner) {
		return new FormXssClean(properties, xssCleaner);
	}

	@Bean
	public JsonMapperBuilderCustomizer xssJacksonCustomizer(MicaXssProperties properties,
															XssCleaner xssCleaner) {
		return builder -> {
			SimpleModule module = new SimpleModule();
			module.addDeserializer(String.class, new JacksonXssClean(properties, xssCleaner));
			builder.addModule(module);
		};
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> patterns = new ArrayList<>();
		// 拦截路由和排除的路由
		patterns.addAll(xssProperties.getPathPatterns());
		patterns.addAll(xssProperties.getPathExcludePatterns());
		if (patterns.isEmpty()) {
			patterns.add("/**");
		}
		// 拦截所有
		XssCleanInterceptor interceptor = new XssCleanInterceptor(xssProperties);
		registry.addInterceptor(interceptor)
			.addPathPatterns(patterns)
			.order(Ordered.LOWEST_PRECEDENCE);
	}

}
