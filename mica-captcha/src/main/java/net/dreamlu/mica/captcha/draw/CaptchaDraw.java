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

package net.dreamlu.mica.captcha.draw;

import java.awt.*;
import java.util.Random;

/**
 * 画验证码
 *
 * @author L.cm
 */
public interface CaptchaDraw {

	/**
	 * 画验证码层
	 *
	 * @param g      Graphics2D
	 * @param width  画布宽度
	 * @param height 画布高度
	 * @param fonts  字体
	 * @param random Random
	 * @return 返回验证码 code
	 */
	String draw(Graphics2D g, int width, int height, Font[] fonts, Random random);

	/**
	 * 校验
	 *
	 * @param code             code
	 * @param userInputCaptcha 用户输入的字符串
	 * @return 是否成功
	 */
	boolean validate(String code, String userInputCaptcha);

}
