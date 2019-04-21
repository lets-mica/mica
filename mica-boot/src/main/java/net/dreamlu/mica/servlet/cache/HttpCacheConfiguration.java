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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashSet;
import java.util.Set;

/**
 * Http Cache 配置
 *
 * @author L.cm
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(MicaHttpCacheProperties.class)
@ConditionalOnProperty(value = "mica.http.cache.enabled")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class HttpCacheConfiguration implements WebMvcConfigurer {
	private static final String DEFAULT_STATIC_PATH_PATTERN = "/**";
	private final WebMvcProperties webMvcProperties;
	private final MicaHttpCacheProperties properties;
	private final CacheManager cacheManager;

	@Bean
	public HttpCacheService httpCacheService() {
		return new HttpCacheService(properties, cacheManager);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		Set<String> excludePatterns = new HashSet<>(properties.getExcludePatterns());
		String staticPathPattern = webMvcProperties.getStaticPathPattern();
		// 如果静态 目录 不为 /**
		if (!DEFAULT_STATIC_PATH_PATTERN.equals(staticPathPattern.trim())) {
			excludePatterns.add(staticPathPattern);
		}
		HttpCacheInterceptor httpCacheInterceptor = new HttpCacheInterceptor(httpCacheService());
		registry.addInterceptor(httpCacheInterceptor)
			.addPathPatterns(properties.getIncludePatterns().toArray(new String[0]))
			.excludePathPatterns(excludePatterns.toArray(new String[0]));
	}
}
