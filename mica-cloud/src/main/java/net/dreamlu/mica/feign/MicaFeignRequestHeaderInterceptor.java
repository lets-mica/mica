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

package net.dreamlu.mica.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import net.dreamlu.mica.hystrix.MicaHttpHeadersContextHolder;
import org.springframework.http.HttpHeaders;

/**
 * feign 传递Request header
 *
 * <p>
 *     https://blog.csdn.net/u014519194/article/details/77160958
 *     http://tietang.wang/2016/02/25/hystrix/Hystrix%E5%8F%82%E6%95%B0%E8%AF%A6%E8%A7%A3/
 *     https://github.com/Netflix/Hystrix/issues/92#issuecomment-260548068
 * </p>
 *
 * @author L.cm
 */
public class MicaFeignRequestHeaderInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		// 默认都使用 hystrix
		HttpHeaders headers = MicaHttpHeadersContextHolder.get();
		if (headers != null && !headers.isEmpty()) {
			headers.forEach((key, values) ->
				values.forEach(value -> requestTemplate.header(key, value))
			);
		}
	}

}
