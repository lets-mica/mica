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

package net.dreamlu.mica.captcha.core;

import lombok.experimental.UtilityClass;
import net.dreamlu.mica.core.utils.StringPool;
import org.springframework.boot.convert.DurationStyle;

import java.awt.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * 验证码工具类
 *
 * @author L.cm
 */
@UtilityClass
public class CaptchaUtil {

	/**
	 * 从 cache name 中解析 ttl，例如： user:test#300ms，不带单位默认为 s 秒
	 *
	 * @param cacheName 缓存名
	 * @return 超时时间
	 */
	public static long getTTLFormCacheName(String cacheName) {
		String[] cacheArray = cacheName.split(StringPool.HASH);
		if (cacheArray.length < 2) {
			return -1L;
		}
		Duration duration = DurationStyle.detectAndParse(cacheArray[1], ChronoUnit.SECONDS);
		return duration.toMillis();
	}

	/**
	 * 生成指定范围的随机数
	 */
	public static int randNum(Random random, int min, int max) {
		int diff = max - min;
		int rand = random.nextInt(diff);
		return min + rand;
	}

	/**
	 * 给定范围获得随机颜色
	 */
	public static Color randColor(Random random, int fc, int bc) {
		int colorMax = 255;
		if (fc > colorMax) {
			fc = 255;
		}
		if (bc > colorMax) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

}
