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

package net.dreamlu.mica.redis.pubsub;

import lombok.experimental.UtilityClass;
import net.dreamlu.mica.core.utils.CharPool;

/**
 * channel 工具类
 *
 * @author L.cm
 */
@UtilityClass
class ChannelUtil {

	/**
	 * 判断是否为模糊话题，*、? 和 [...]
	 *
	 * @param channel 话题名
	 * @return 是否模糊话题
	 */
	public static boolean isPattern(String channel) {
		int length = channel.length();
		boolean isRightSqBracket = false;
		// 倒序，因为表达式一般在尾部
		for (int i = length - 1; i > 0; i--) {
			char charAt = channel.charAt(i);
			switch (charAt) {
				case CharPool.ASTERISK:
				case CharPool.QUESTION_MARK:
					if (isEscapeChars(channel, i)) {
						break;
					}
					return true;
				case CharPool.RIGHT_SQ_BRACKET:
					if (isEscapeChars(channel, i)) {
						break;
					}
					isRightSqBracket = true;
					break;
				case CharPool.LEFT_SQ_BRACKET:
					if (isEscapeChars(channel, i)) {
						break;
					}
					if (isRightSqBracket) {
						return true;
					}
					break;
				default:
					break;
			}
		}
		return false;
	}

	/**
	 * 判断是否为转义字符
	 *
	 * @param name  话题名
	 * @param index 索引
	 * @return 是否为转义字符
	 */
	private static boolean isEscapeChars(String name, int index) {
		if (index < 1) {
			return false;
		}
		// 预读一位，判断是否为转义符 “/”
		char charAt = name.charAt(index - 1);
		return CharPool.BACK_SLASH == charAt;
	}

}
