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

import java.awt.*;
import java.util.Random;

/**
 * 小字符背景
 *
 * @author L.cm
 */
public enum SmallCharsBackgroundDraw implements BackgroundDraw {

	/**
	 * 实例
	 */
	INSTANCE;

	/**
	 * 验证码随机字符数组
	 */
	private static final char[] CHAR_ARRAY = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

	@Override
	public void draw(Graphics2D g, int width, int height, Font[] fonts, Random random) {
		// 设定字体，每次随机
		Font fontTemp = fonts[random.nextInt(fonts.length)];
		// 设定背景色，淡色
		g.setColor(CaptchaUtil.randColor(random, 220, 250));
		g.fillRect(0, 0, width, height);
		Font font = fontTemp.deriveFont(Font.PLAIN, 16.0F);
		g.setFont(font);
		FontMetrics fontMetrics = g.getFontMetrics();
		// 画小字符背景，10个
		int charCount = 10;
		for (int i = 0; i < charCount; i++) {
			g.setColor(CaptchaUtil.randColor(random, 120, 200));
			char ch = CHAR_ARRAY[random.nextInt(CHAR_ARRAY.length)];
			int offset = fontMetrics.charWidth(ch);
			int x = CaptchaUtil.randNum(random, offset, width - offset);
			int y = CaptchaUtil.randNum(random, offset, height - offset);
			g.drawString(String.valueOf(ch), x, y);
		}
	}
}
