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

import java.awt.*;
import java.util.Random;

/**
 * 算术型验证码
 *
 * @author L.cm
 */
public class MathCaptchaDraw implements CaptchaDraw {

	@Override
	public String draw(Graphics2D g, int width, int height, Font[] fonts, Random random) {
		//设定字体，每次随机
		Font fontTemp = fonts[random.nextInt(fonts.length)];
		// 深色
		g.setColor(CaptchaUtil.randColor(random, 0, 100));
		// 数学公式
		String expr = Expression.randomExpr(random);
		// 补全验证码图案 1+1=?
		String text = expr + "=?";
		// 偏移半个字体大小
		int len = text.length();
		Font font = fontTemp.deriveFont(Font.BOLD, (width - 10F) / len * 1.6F);
		g.setFont(font);
		FontMetrics fontMetrics = g.getFontMetrics();
		// 计算宽度
		int textWidth = 0;
		for (int i = 0; i < len; i++) {
			char charAt = text.charAt(i);
			int charWidth = fontMetrics.charWidth(charAt);
			textWidth += charWidth;
		}
		// 保证居中
		float x = (width - textWidth) / 2.0F;
		// 高度
		float y = height / 4.0F * 3;
		g.drawString(text, x, y);
		return expr;
	}

	@Override
	public boolean validate(String code, String userInputCaptcha) {
		if (!StringUtil.isNumeric(userInputCaptcha)) {
			return false;
		}
		int captchaNum = Integer.parseInt(userInputCaptcha);
		int evalNum = Expression.eval(code);
		return captchaNum == evalNum;
	}

}
