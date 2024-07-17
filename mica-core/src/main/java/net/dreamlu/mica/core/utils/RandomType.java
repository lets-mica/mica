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

package net.dreamlu.mica.core.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Random;

/**
 * 生成的随机数类型
 *
 * @author L.cm
 */
@Getter
@RequiredArgsConstructor
public enum RandomType {
	/**
	 * INT STRING ALL
	 */
	INT(0, 9),
	STRING(10, 61),
	ALL(0, 61);

	private final int begin;
	private final int end;

	/**
	 * 生成随机数
	 *
	 * @param random random
	 * @return 随机数
	 */
	public byte random(Random random) {
		int idx = random.nextInt(end);
		if (idx < begin) {
			idx += begin;
		}
		return NumberUtil.DIGITS[idx];
	}

}
