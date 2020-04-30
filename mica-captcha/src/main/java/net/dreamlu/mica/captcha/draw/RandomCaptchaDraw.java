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

import net.dreamlu.mica.captcha.core.CaptchaUtil;
import net.dreamlu.mica.core.utils.StringUtil;
import org.springframework.util.ObjectUtils;

import java.awt.*;
import java.util.Random;

/**
 * 随机字符串验证码
 *
 * @author L.cm
 */
public class RandomCaptchaDraw implements CaptchaDraw {
	/**
	 * 默认的验证码数量，由于字体大小定死，后期再扩展自动一数量
	 */
	private static final int CODE_SIZE = 4;
	/**
	 * 验证码随机字符数组
	 */
	private static final char[] CHAR_ARRAY = "3456789ABCDEFGHJKMNPQRSTUVWXY".toCharArray();
	private final int codeSize;

	public RandomCaptchaDraw() {
		this(CODE_SIZE);
	}

	public RandomCaptchaDraw(int codeSize) {
		this.codeSize = codeSize;
	}

	@Override
	public String draw(Graphics2D g, int width, int height, Font[] fonts, Random random) {
		// 取随机产生的认证码(4位数字)
		String code = generateCode(random, codeSize);
		char[] buffer = code.toCharArray();
		for (int i = 0; i < buffer.length; i++) {
			//旋转度数 最好小于45度
			int degree = random.nextInt(25);
			if (i % 2 == 0) {
				degree = -degree;
			}
			//定义坐标
			int x = 27 * i;
			int y = 28;
			//旋转区域
			double radians = Math.toRadians(degree);
			g.rotate(radians, x, y);
			//设定字体颜色
			g.setColor(CaptchaUtil.randColor(random, 20, 130));
			//设定字体，每次随机
			Font fontTemp = fonts[random.nextInt(fonts.length)];
			Font font = fontTemp.deriveFont(Font.BOLD, (width - 10F) / codeSize * 1.2F);
			g.setFont(font);
			char xcode = buffer[i];
			//将认证码显示到图象中
			g.drawString(String.valueOf(xcode), x + 8, y + 10);
			//旋转之后，必须旋转回来
			g.rotate(-radians, x, y);
		}
		return code;
	}

	/**
	 * 生成验证码字符串
	 *
	 * @param random Random
	 * @return 验证码字符串
	 */
	private static String generateCode(Random random, int size) {
		char[] buffer = new char[size];
		for (int i = 0; i < size; i++) {
			buffer[i] = CHAR_ARRAY[random.nextInt(CHAR_ARRAY.length)];
		}
		return new String(buffer);
	}

	@Override
	public boolean validate(String code, String userInputCaptcha) {
		if (StringUtil.isBlank(userInputCaptcha)) {
			return false;
		}
		// 转成大写重要
		return ObjectUtils.nullSafeEquals(code, userInputCaptcha.toUpperCase());
	}

}
