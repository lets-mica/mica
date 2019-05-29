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

package net.dreamlu.mica.context.reactive;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.context.MicaAccountGetter;
import net.dreamlu.mica.context.MicaHeadersProperties;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.reactive.context.ReactiveRequestContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * HttpHeaders 透传
 *
 * @author L.cm
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class HttpHeadersFilterFunction implements ExchangeFilterFunction {
	@Autowired
	private MicaHeadersProperties properties;
	@Nullable
	@Autowired(required = false)
	private MicaAccountGetter accountGetter;

	/**
	 * 全局透传请求头：X-Real-IP x-forwarded-for 请求和转发的ip
	 */
	private static final String[] ALLOW_HEADS = new String[]{
		"X-Real-IP", "x-forwarded-for"
	};

	@Override
	public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
		return ReactiveRequestContextHolder.getRequest().flatMap(req -> {
			ClientRequest clientRequest = ClientRequest.from(request)
				// 透传 header
				.headers(headers -> filterHeaders(headers, req.getHeaders()))
				.build();
			return next.exchange(clientRequest);
		});
	}

	/**
	 * 透传 header
	 *
	 * @param newHeaders 传递到下层的 header
	 * @param oldHeaders 网关上层的 header
	 */
	private void filterHeaders(HttpHeaders newHeaders, HttpHeaders oldHeaders) {
		String accountHeaderName = properties.getAccountHeaderName();
		// 如果配置有 account 读取器
		if (accountGetter != null) {
			String xAccountHeader = accountGetter.get();
			if (StringUtil.isNotBlank(xAccountHeader)) {
				newHeaders.add(accountHeaderName, xAccountHeader);
			}
		}

		List<String> allowHeadsList = new ArrayList<>(Arrays.asList(ALLOW_HEADS));
		// 如果有传递 account header 继续往下层传递
		allowHeadsList.add(accountHeaderName);
		// 配置的下传头
		allowHeadsList.addAll(properties.getAllowed());

		for (String headerName : allowHeadsList) {
			String headerValue = oldHeaders.getFirst(headerName);
			if (StringUtil.isNotBlank(headerValue)) {
				newHeaders.add(headerName, headerValue);
			}
		}
	}
}
