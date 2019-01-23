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

import org.springframework.cache.Cache;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * BaseCaptcha
 *
 * @author L.cm
 */
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

	protected HttpHeaders getResponseHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setPragma("no-cache");
		headers.setCacheControl("no-cache");
		headers.setExpires(0);
		headers.setContentType(MediaType.IMAGE_JPEG);
		return headers;
	}

}
