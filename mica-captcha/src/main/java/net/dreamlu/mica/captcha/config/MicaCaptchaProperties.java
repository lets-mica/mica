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

import lombok.Getter;
import lombok.Setter;
import net.dreamlu.mica.captcha.enums.CaptchaType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 验证码配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(MicaCaptchaProperties.PREFIX)
public class MicaCaptchaProperties {
	public static final String PREFIX = "mica.captcha";

	/**
	 * 验证码类型，默认：随机数
	 */
	private CaptchaType captchaType = CaptchaType.RANDOM;
	/**
	 * 验证码cache名，默认：captcha:cache#5m，配合 mica-redis 5分钟缓存
	 */
	private String cacheName = "captcha:cache#5m";

}
