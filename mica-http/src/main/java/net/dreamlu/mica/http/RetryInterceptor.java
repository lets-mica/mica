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
import net.dreamlu.mica.core.retry.IRetry;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.function.Predicate;

/**
 * 重试拦截器，应对代理问题
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class RetryInterceptor implements Interceptor {
	private final IRetry retry;
	@Nullable
	private final Predicate<ResponseSpec> respPredicate;

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		return retry.execute(() -> {
			Response response = chain.proceed(request);
			// 结果集校验
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

}
