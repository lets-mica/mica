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

package net.dreamlu.mica.holidays.core;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 日期类型，工作日对应结果为 0, 休息日对应结果为 1, 节假日对应的结果为 2；
 *
 * @author L.cm
 */
@Getter
@RequiredArgsConstructor
public enum DaysType {

	/**
	 * 工作日
	 */
	WEEKDAYS((byte) 0),
	/**
	 * 休息日
	 */
	REST_DAYS((byte) 1),
	/**
	 * 节假日
	 */
	HOLIDAYS((byte) 2);

	@JsonValue
	private final byte type;

	/**
	 * 将 type 转换成枚举
	 *
	 * @param type type
	 * @return DaysType
	 */
	public static DaysType from(byte type) {
		switch (type) {
			case 0:
				return WEEKDAYS;
			case 1:
				return REST_DAYS;
			case 2:
				return HOLIDAYS;
			default:
				throw new IllegalArgumentException("未知的 DaysType:" + type);
		}
	}

}
