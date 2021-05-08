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

package net.dreamlu.mica.captcha.config;

import net.dreamlu.mica.captcha.cache.ICaptchaCache;
import net.dreamlu.mica.captcha.core.Captcha;
import net.dreamlu.mica.captcha.service.CaptchaServiceImpl;
import net.dreamlu.mica.captcha.service.ICaptchaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.ResourceHint;

/**
 * 验证码自动配置
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
	prefix = MicaCaptchaProperties.PREFIX,
	name = "enabled",
	havingValue = "true",
	matchIfMissing = true
)
@EnableConfigurationProperties(MicaCaptchaProperties.class)
@NativeHint(resources = @ResourceHint(patterns = "^fonts/.*.ttf"))
public class MicaCaptchaAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Captcha imageCaptcha(MicaCaptchaProperties properties) {
		return new Captcha(properties.getCaptchaType());
	}

	@Bean
	@ConditionalOnMissingBean
	public ICaptchaService imageCaptchaService(MicaCaptchaProperties properties,
										  ICaptchaCache captchaCache,
										  Captcha captcha) {
		return new CaptchaServiceImpl(properties, captchaCache, captcha);
	}

}
