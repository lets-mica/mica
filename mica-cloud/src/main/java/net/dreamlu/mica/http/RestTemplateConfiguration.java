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

package net.dreamlu.mica.http;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.context.MicaHttpHeadersGetter;
import net.dreamlu.mica.core.utils.Charsets;
import net.dreamlu.mica.http.logger.HttpLoggingInterceptor;
import net.dreamlu.mica.http.logger.OkHttpSlf4jLogger;
import net.dreamlu.mica.props.MicaRequestLogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Http RestTemplateHeaderInterceptor 配置
 *
 * @author L.cm
 */
@Configuration
@ConditionalOnClass(okhttp3.OkHttpClient.class)
@RequiredArgsConstructor
public class RestTemplateConfiguration {
	private final ObjectMapper objectMapper;
	private final MicaRequestLogProperties properties;

	/**
	 * okhttp3 请求日志拦截器
	 *
	 * @return HttpLoggingInterceptor
	 */
	@Bean
	public HttpLoggingInterceptor loggingInterceptor() {
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new OkHttpSlf4jLogger());
		interceptor.setLevel(properties.getLevel());
		return interceptor;
	}

	/**
	 * okhttp3 链接池配置
	 *
	 * @param connectionPoolFactory 链接池配置
	 * @param httpClientProperties  httpClient配置
	 * @return okhttp3.ConnectionPool
	 */
	@Bean
	@ConditionalOnMissingBean(okhttp3.ConnectionPool.class)
	public okhttp3.ConnectionPool httpClientConnectionPool(
		FeignHttpClientProperties httpClientProperties,
		OkHttpClientConnectionPoolFactory connectionPoolFactory) {
		Integer maxTotalConnections = httpClientProperties.getMaxConnections();
		Long timeToLive = httpClientProperties.getTimeToLive();
		TimeUnit ttlUnit = httpClientProperties.getTimeToLiveUnit();
		return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
	}

	/**
	 * 配置OkHttpClient
	 *
	 * @param httpClientFactory    httpClient 工厂
	 * @param connectionPool       链接池配置
	 * @param httpClientProperties httpClient配置
	 * @param interceptor          拦截器
	 * @return OkHttpClient
	 */
	@Bean
	@ConditionalOnMissingBean
	public okhttp3.OkHttpClient okHttpClient(
		OkHttpClientFactory httpClientFactory,
		okhttp3.ConnectionPool connectionPool,
		FeignHttpClientProperties httpClientProperties,
		HttpLoggingInterceptor interceptor) {
		Boolean followRedirects = httpClientProperties.isFollowRedirects();
		Integer connectTimeout = httpClientProperties.getConnectionTimeout();
		return httpClientFactory.createBuilder(httpClientProperties.isDisableSslValidation())
			.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
			.writeTimeout(30, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS)
			.followRedirects(followRedirects)
			.connectionPool(connectionPool)
			.addInterceptor(interceptor)
			.build();
	}

	@Bean
	public RestTemplateHeaderInterceptor requestHeaderInterceptor(
		@Autowired(required = false) @Nullable MicaHttpHeadersGetter headersGetter) {
		return new RestTemplateHeaderInterceptor(headersGetter);
	}

	/**
	 * 普通的 RestTemplate，不透传请求头，一般只做外部 http 调用
	 *
	 * @param okHttpClient OkHttpClient
	 * @return RestTemplate
	 */
	@Bean
	@ConditionalOnMissingBean
	public RestTemplate restTemplate(okhttp3.OkHttpClient okHttpClient) {
		RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory(okHttpClient));
		configMessageConverters(restTemplate.getMessageConverters());
		return restTemplate;
	}

	/**
	 * 支持负载均衡的 LbRestTemplate
	 *
	 * @param okHttpClient  OkHttpClient
	 * @param interceptor RestTemplateHeaderInterceptor
	 * @return LbRestTemplate
	 */
	@Bean
	@LoadBalanced
	@ConditionalOnMissingBean
	public LbRestTemplate lbRestTemplate(okhttp3.OkHttpClient okHttpClient, RestTemplateHeaderInterceptor interceptor) {
		LbRestTemplate lbRestTemplate = new LbRestTemplate(new OkHttp3ClientHttpRequestFactory(okHttpClient));
		lbRestTemplate.setInterceptors(Collections.singletonList(interceptor));
		configMessageConverters(lbRestTemplate.getMessageConverters());
		return lbRestTemplate;
	}

	private void configMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.removeIf(x -> x instanceof StringHttpMessageConverter || x instanceof MappingJackson2HttpMessageConverter);
		converters.add(new StringHttpMessageConverter(Charsets.UTF_8));
		converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
	}
}
