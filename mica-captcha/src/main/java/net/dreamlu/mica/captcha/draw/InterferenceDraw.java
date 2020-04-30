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
 * 干扰画
 *
 * @author L.cm
 */
public interface InterferenceDraw {

	/**
	 * 画干扰层
	 *
	 * @param g      Graphics2D
	 * @param width  画布宽度
	 * @param height 画布高度
	 * @param fonts  字体
	 * @param random Random
	 */
	void draw(Graphics2D g, int width, int height, Font[] fonts, Random random);

}
