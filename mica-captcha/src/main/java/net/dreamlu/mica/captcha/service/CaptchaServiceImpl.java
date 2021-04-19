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

package net.dreamlu.mica.captcha.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.captcha.cache.ICaptchaCache;
import net.dreamlu.mica.captcha.config.MicaCaptchaProperties;
import net.dreamlu.mica.captcha.core.Captcha;
import net.dreamlu.mica.core.utils.StringUtil;

import java.io.OutputStream;

/**
 * 验证码服务
 *
 * @author L.cm
 */
@Slf4j
@RequiredArgsConstructor
public class CaptchaServiceImpl implements ICaptchaService {
	private final MicaCaptchaProperties properties;
	private final ICaptchaCache captchaCache;
	private final Captcha captcha;

	@Override
	public void generate(String uuid, OutputStream outputStream) {
		String generate = captcha.generate(() -> outputStream);
		captchaCache.put(properties.getCacheName(), uuid, generate);
	}

	@Override
	public boolean validate(String uuid, String userInputCaptcha) {
		log.debug("validate captcha uuid is {}, userInputCaptcha is {}", uuid, userInputCaptcha);
		String code = captchaCache.getAndRemove(properties.getCacheName(), uuid);
		if (StringUtil.isBlank(code)) {
			return false;
		}
		return captcha.validate(code, userInputCaptcha);
	}

}
