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

package net.dreamlu.mica.captcha.servlet;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.captcha.BaseCaptcha;
import net.dreamlu.mica.captcha.CaptchaUtils;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.core.utils.WebUtil;
import org.springframework.cache.Cache;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 图形验证码
 *
 * @author L.cm
 */
@Slf4j
public class MicaCaptchaServlet extends BaseCaptcha {

	public MicaCaptchaServlet(Cache captchaCache) {
		super(captchaCache);
	}

	/**
	 * 生成验证码
	 * @param response HttpServletResponse
	 * @return {ResponseEntity}
	 */
	public ResponseEntity<Resource> generate(HttpServletResponse response) {
		// 生成验证码
		byte[] bytes = generateByteArray(response);
		Resource resource = new ByteArrayResource(bytes);
		return new ResponseEntity<>(resource, this.getResponseHeaders(), HttpStatus.OK );
	}

	/**
	 * 生成验证码 byte array
	 * @param response HttpServletResponse
	 * @return {byte array}
	 */
	public byte[] generateByteArray(HttpServletResponse response) {
		String cookieName = getCookieName();
		// 先检查cookie的uuid是否存在
		String cookieValue = WebUtil.getCookieVal(cookieName);
		boolean hasCookie = true;
		if (StringUtil.isBlank(cookieValue)) {
			hasCookie = false;
			cookieValue = StringUtil.getUUID();
		}
		ThreadLocalRandom random = ThreadLocalRandom.current();
		// 转成大写重要
		String captchaCode = CaptchaUtils.generateCode(random).toUpperCase();
		// 不存在cookie时设置cookie
		if (!hasCookie) {
			WebUtil.setCookie(response, cookieName, cookieValue, DEFAULT_MAX_AGE);
		}
		captchaCache.put(cookieValue, captchaCode);
		// 生成验证码
		return CaptchaUtils.generate(random, captchaCode);
	}

	/**
	 * 仅能验证一次，验证后立即删除
	 *
	 * @param response         HttpServletResponse
	 * @param userInputCaptcha 用户输入的验证码
	 * @return 验证通过返回 true, 否则返回 false
	 */
	public boolean validate(HttpServletResponse response, String userInputCaptcha) {
		if (log.isInfoEnabled()) {
			log.info("validate captcha userInputCaptcha is {}", userInputCaptcha);
		}
		String cookieName = getCookieName();
		String cookieValue = WebUtil.getCookieVal(cookieName);
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
			WebUtil.removeCookie(response, cookieName);
		}
		return result;
	}
}
