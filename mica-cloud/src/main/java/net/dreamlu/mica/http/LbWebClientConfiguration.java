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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.context.reactive.HttpHeadersFilterFunction;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.launcher.MicaLogLevel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

/**
 * 负载均衡的 WebClient
 *
 * @author dream.lu
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "mica.http.enabled", matchIfMissing = true)
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class LbWebClientConfiguration {
	private final LoadBalancerExchangeFilterFunction lbFunction;
	private final HttpHeadersFilterFunction filterFunction;
	private final MicaHttpProperties properties;

	/**
	 * 负载均衡的 WebClient
	 *
	 * @return WebClient
	 */
	@Bean("lbWebClient")
	@ConditionalOnMissingBean(name = "lbWebClient")
	public WebClient lbWebClient() {
		return WebClient.builder()
			.filters((functions) -> {
				functions.add(lbFunction);
				functions.add(filterFunction);
				functions.add(logFunction());
			})
			.build();
	}

	/**
	 * WebClient 400 或者 500 会走异常
	 *
	 * @param ex WebClientResponseException
	 * @return ResponseEntity
	 */
	@ExceptionHandler(WebClientResponseException.class)
	public ResponseEntity<String> handleException(WebClientResponseException ex) {
		int statusCode = ex.getRawStatusCode();
		String bodyAsString = ex.getResponseBodyAsString();
		log.error("Error from WebClient - Status {}, Body {}", statusCode, bodyAsString, ex);
		return ResponseEntity.status(statusCode).body(bodyAsString);
	}

	private ExchangeFilterFunction logFunction() {
		return (request, next) -> {
			MicaLogLevel level = properties.getLevel();
			// 日志没有开启直接放行
			if (MicaLogLevel.NONE == level) {
				return next.exchange(request);
			}

			// 记录日志
			String requestUrl = request.url().toString();
			// 构建成一条长 日志，避免并发下日志错乱
			StringBuilder beforeReqLog = new StringBuilder(300);
			// 日志参数
			List<Object> beforeReqArgs = new ArrayList<>();
			beforeReqLog.append("\n\n================ WebClient Request Start  ================\n");
			// 打印路由
			beforeReqLog.append("===> {}: {}\n");
			// 参数
			String requestMethod = request.method().name();
			beforeReqArgs.add(requestMethod);
			beforeReqArgs.add(requestUrl);

			// 打印请求头
			if (MicaLogLevel.HEADERS.lte(level)) {
				HttpHeaders newHeaders = request.headers();
				newHeaders.forEach((headerName, headerValue) -> {
					beforeReqLog.append("===Headers===  {}: {}\n");
					beforeReqArgs.add(headerName);
					beforeReqArgs.add(StringUtil.join(headerValue));
				});
			}

			beforeReqLog.append("================  WebClient Request End  =================\n");

			log.info(beforeReqLog.toString(), beforeReqArgs.toArray());

			return next.exchange(request);
		};
	}
}
