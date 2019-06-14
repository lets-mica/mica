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

package net.dreamlu.mica.captcha;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.utils.Base64Util;
import net.dreamlu.mica.core.utils.StringUtil;
import org.springframework.cache.Cache;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.concurrent.ThreadLocalRandom;

/**
 * BaseCaptcha
 *
 * @author L.cm
 */
@Slf4j
public abstract class BaseCaptcha {
	protected static final String DEFAULT_COOKIE_NAME = "mica-captcha";
	protected static final String DEFAULT_CHACHE_NAME = "micaCaptchaCache";
	/**
	 * cookie超时默认为session会话状态
	 */
	protected final static int DEFAULT_MAX_AGE = -1;

	private String cacheName;
	private String cookieName;
	protected Cache captchaCache;

	public BaseCaptcha(Cache captchaCache) {
		this.cacheName = DEFAULT_CHACHE_NAME;
		this.cookieName = DEFAULT_COOKIE_NAME;
		this.captchaCache = captchaCache;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	/**
	 * 图片输出的头，避免验证码被缓存
	 * @return HttpHeaders
	 */
	protected HttpHeaders getResponseHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setPragma("no-cache");
		headers.setCacheControl("no-cache");
		headers.setExpires(0);
		headers.setContentType(MediaType.IMAGE_JPEG);
		return headers;
	}

	/**
	 * 生成验证码
	 *
	 * @return {String}
	 */
	public Captcha generateBase64() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		// 转成大写重要
		String captchaCode = CaptchaUtils.generateCode(random).toUpperCase();
		// 生成验证码
		byte[] imgBytes = CaptchaUtils.generate(random, captchaCode);
		String uuid = StringUtil.getUUID();
		// 保存验证码缓存
		captchaCache.put(uuid, captchaCode);
		String base64 = "data:image/jpeg;base64," +
			Base64Util.encodeToString(imgBytes);
		return new Captcha(uuid, base64);
	}

	/**
	 * 校验验证码
	 *
	 * @param uuid uuid
	 * @param userInputCaptcha 用户输入的验证码
	 * @return 是否成功
	 */
	public boolean validate(String uuid, String userInputCaptcha) {
		if (log.isInfoEnabled()) {
			log.info("validate captcha userInputCaptcha is {}", userInputCaptcha);
		}
		String captchaCode = captchaCache.get(uuid, String.class);
		if (StringUtil.isBlank(captchaCode)) {
			return false;
		}
		// 转成大写重要
		userInputCaptcha = userInputCaptcha.toUpperCase();
		boolean result = userInputCaptcha.equals(captchaCode);
		if (result) {
			// 校验成功删除缓存
			captchaCache.evict(uuid);
		}
		return result;
	}
}
