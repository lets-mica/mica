/*
 * Copyright (c) 2019-2029, Dreamlu (596392912@qq.com & www.dreamlu.net).
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
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.io.IOException;
import java.util.function.Predicate;

/**
 * 重试拦截器，应对代理问题
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class RetryInterceptor implements Interceptor {
	private final RetryPolicy retryPolicy;

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		RetryTemplate template = createRetryTemplate(retryPolicy);
		return template.execute(context -> {
			Response response = chain.proceed(request);
			// 结果集校验
			Predicate<ResponseSpec> respPredicate = retryPolicy.getRespPredicate();
			if (respPredicate == null) {
				return response;
			}
			// copy 一份 body
			ResponseBody body = response.peekBody(Long.MAX_VALUE);
			try (HttpResponse httpResponse = new HttpResponse(response)) {
				if (respPredicate.test(httpResponse)) {
					throw new IOException("Http Retry ResponsePredicate test Failure.");
				}
			}
			return response.newBuilder().body(body).build();
		});
	}

	private static RetryTemplate createRetryTemplate(RetryPolicy policy) {
		RetryTemplate template = new RetryTemplate();
		// 重试策略
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(policy.getMaxAttempts());
		// 设置间隔策略
		FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
		backOffPolicy.setBackOffPeriod(policy.getSleepMillis());
		template.setRetryPolicy(retryPolicy);
		template.setBackOffPolicy(backOffPolicy);
		return template;
	}
}
