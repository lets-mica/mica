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

package net.dreamlu.mica.captcha.reactive;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.captcha.BaseCaptcha;
import net.dreamlu.mica.captcha.CaptchaUtils;
import net.dreamlu.mica.core.utils.StringPool;
import net.dreamlu.mica.core.utils.StringUtil;
import org.springframework.cache.Cache;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 图形验证码
 *
 * @author L.cm
 */
@Slf4j
public class MicaCaptchaReactive extends BaseCaptcha {

	public MicaCaptchaReactive(Cache captchaCache) {
		super(captchaCache);
	}

	/**
	 * 生成验证码
	 *
	 * @param exchange ServerWebExchange
	 * @return {Resource}
	 */
	public ResponseEntity<Resource> generate(ServerWebExchange exchange) {
		// 生成验证码
		byte[] bytes = generateByteArray(exchange);
		Resource resource = new ByteArrayResource(bytes);
		return new ResponseEntity<>(resource, this.getResponseHeaders(), HttpStatus.OK);
	}

	/**
	 * 生成验证码 byte array
	 *
	 * @param exchange ServerWebExchange
	 * @return {byte array}
	 */
	public byte[] generateByteArray(ServerWebExchange exchange) {
		final ServerHttpRequest request = exchange.getRequest();
		final ServerHttpResponse response = exchange.getResponse();
		String cookieName = getCookieName();
		// 先检查cookie的uuid是否存在，不存在设置 cookie
		String cookieValue = Optional.of(request.getCookies())
			.map(x -> x.getFirst(cookieName))
			.map(HttpCookie::getValue)
			.orElseGet(() -> {
				String newCookieValue = StringUtil.getUUID();
				ResponseCookie cookie = ResponseCookie.from(cookieName, newCookieValue)
					.maxAge(DEFAULT_MAX_AGE)
					.path(StringPool.SLASH)
					.build();
				response.addCookie(cookie);
				return newCookieValue;
			});
		ThreadLocalRandom random = ThreadLocalRandom.current();
		// 转成大写重要
		String captchaCode = CaptchaUtils.generateCode(random).toUpperCase();
		captchaCache.put(cookieValue, captchaCode);
		// 生成验证码
		return CaptchaUtils.generate(random, captchaCode);
	}

	/**
	 * 仅能验证一次，验证后立即删除
	 *
	 * @param exchange         ServerWebExchange
	 * @param userInputCaptcha 用户输入的验证码
	 * @return 验证通过返回 true, 否则返回 false
	 */
	public boolean validate(ServerWebExchange exchange, String userInputCaptcha) {
		if (log.isInfoEnabled()) {
			log.info("validate captcha userInputCaptcha is {}", userInputCaptcha);
		}
		final ServerHttpRequest request = exchange.getRequest();
		final ServerHttpResponse response = exchange.getResponse();
		// 获取 cookie
		String cookieName = getCookieName();
		String cookieValue = Optional.of(request.getCookies())
			.map(x -> x.getFirst(cookieName))
			.map(HttpCookie::getValue)
			.orElse(null);
		if (StringUtil.isBlank(cookieValue)) {
			return false;
		}
		String captchaCode = captchaCache.get(cookieValue, String.class);
		if (StringUtil.isBlank(captchaCode)) {
			return false;
		}
		// 转成大写重要
		userInputCaptcha = userInputCaptcha.toUpperCase();
		boolean result = userInputCaptcha.equals(captchaCode);
		if (result) {
			// 校验成功删除缓存和cookie
			captchaCache.evict(cookieValue);
			ResponseCookie cookie = ResponseCookie.from(cookieName, StringPool.EMPTY)
				.maxAge(0)
				.path(StringPool.SLASH)
				.build();
			response.addCookie(cookie);
		}
		return result;
	}
}
