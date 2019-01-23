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

import lombok.AllArgsConstructor;
import net.dreamlu.mica.captcha.MicaCaptchaProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码 自动化配置
 *
 * @author L.cm
 */
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(value = "mica.captcha.enabled", havingValue = "true", matchIfMissing = true)
public class ReactiveCaptchaConfiguration {
	private MicaCaptchaProperties captchaProperties;

	@Bean
	@ConditionalOnMissingBean
	public MicaCaptchaReactive dreamCaptcha(CacheManager cacheManager) {
		String cacheName = captchaProperties.getCacheName();
		Cache captchaCache = cacheManager.getCache(cacheName);
		MicaCaptchaReactive captcha = new MicaCaptchaReactive(captchaCache);
		// cookie
		String cookieName = captchaProperties.getCookieName();
		captcha.setCookieName(cookieName);
		return captcha;
	}
}
