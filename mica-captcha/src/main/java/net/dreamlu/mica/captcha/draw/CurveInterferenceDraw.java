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
import java.awt.geom.CubicCurve2D;
import java.util.Random;

/**
 * 双曲线干扰
 *
 * @author L.cm
 */
public enum CurveInterferenceDraw implements InterferenceDraw {

	/**
	 * 实例
	 */
	INSTANCE;

	@Override
	public void draw(Graphics2D g, int width, int height, Font[] fonts, Random random) {
		// 复用上层颜色,width是线宽,float型
		g.setStroke(new BasicStroke(1.8F));
		int x1 = 5;
		int y1 = CaptchaUtil.randNum(random, 5, height / 2);
		int maxHeight = height - 5;
		int minWidth = width / 4;
		int maxWidth = width / 4 * 3;
		int ctrLx1 = CaptchaUtil.randNum(random, minWidth, maxWidth);
		int ctrLy1 = CaptchaUtil.randNum(random, 5, maxHeight);
		int ctrLx2 = CaptchaUtil.randNum(random, minWidth, maxWidth);
		int ctrLy2 = CaptchaUtil.randNum(random, 5, maxHeight);
		int x2 = width - 5;
		int y2 = CaptchaUtil.randNum(random, height / 2, maxHeight);
		// 画三次曲线
		g.draw(new CubicCurve2D.Double(x1, y1, ctrLx1, ctrLy1, ctrLx2, ctrLy2, x2, y2));
	}
}
