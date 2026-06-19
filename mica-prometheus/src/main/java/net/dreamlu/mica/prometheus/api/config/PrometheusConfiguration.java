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

package net.dreamlu.mica.prometheus.api.config;

import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.prometheus.api.core.BasicAuthHandlerInterceptor;
import net.dreamlu.mica.prometheus.api.core.BasicAuthWebFilter;
import net.dreamlu.mica.prometheus.api.core.PrometheusApi;
import net.dreamlu.mica.prometheus.api.core.ReactivePrometheusApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnReactiveDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * prometheus http sd
 *
 * @author L.cm
 */
@AutoConfiguration
@EnableConfigurationProperties(MicaPrometheusProperties.class)
public class PrometheusConfiguration {

	@AutoConfiguration
	@RequiredArgsConstructor
	@ConditionalOnBean(DiscoveryClient.class)
	@ConditionalOnDiscoveryEnabled
	// @ConditionalOnBlockingDiscoveryEnabled
	@ConditionalOnProperty(value = "spring.cloud.discovery.blocking.enabled")
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	public static class PrometheusApiConfiguration implements WebMvcConfigurer {
		private final MicaPrometheusProperties properties;

		@Bean
		public PrometheusApi prometheusApi(Environment environment,
										   DiscoveryClient discoveryClient,
										   ApplicationEventPublisher eventPublisher) {
			String[] activeProfiles = environment.getActiveProfiles();
			String activeProfile = activeProfiles.length > 0 ? activeProfiles[0] : null;
			return new PrometheusApi(activeProfile, discoveryClient, eventPublisher);
		}

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			// basic auth 已开启才注册拦截器，仅作用于 sd 端点
			if (properties.getBasicAuth().isEnabled()) {
				registry.addInterceptor(new BasicAuthHandlerInterceptor(properties.getBasicAuth()))
					.addPathPatterns("/actuator/prometheus/sd")
					.order(Ordered.HIGHEST_PRECEDENCE);
			}
		}

	}

	@AutoConfiguration
	@RequiredArgsConstructor
	@ConditionalOnBean(ReactiveDiscoveryClient.class)
	@ConditionalOnDiscoveryEnabled
	@ConditionalOnReactiveDiscoveryEnabled
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	public static class ReactivePrometheusApiConfiguration {
		private final MicaPrometheusProperties properties;

		@Bean
		public ReactivePrometheusApi reactivePrometheusApi(Environment environment,
														   ReactiveDiscoveryClient discoveryClient,
														   ApplicationEventPublisher eventPublisher) {
			String[] activeProfiles = environment.getActiveProfiles();
			String activeProfile = activeProfiles.length > 0 ? activeProfiles[0] : null;
			return new ReactivePrometheusApi(activeProfile, discoveryClient, eventPublisher);
		}

		@Bean
		@ConditionalOnProperty(value = MicaPrometheusProperties.PREFIX + ".basic-auth.enabled", havingValue = "true")
		public BasicAuthWebFilter prometheusBasicAuthWebFilter() {
			return new BasicAuthWebFilter(properties.getBasicAuth());
		}

	}

}
