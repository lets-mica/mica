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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 节假日接口
 *
 * @author L.cm
 */
public interface HolidaysApi {

	/**
	 * 获取日志类型
	 *
	 * @param localDate LocalDate
	 * @return DaysType
	 */
	DaysType getDaysType(LocalDate localDate);

	/**
	 * 获取日志类型
	 *
	 * @param localDateTime LocalDateTime
	 * @return DaysType
	 */
	default DaysType getDaysType(LocalDateTime localDateTime) {
		return getDaysType(localDateTime.toLocalDate());
	}

	/**
	 * 获取日志类型
	 *
	 * @param date Date
	 * @return DaysType
	 */
	default DaysType getDaysType(Date date) {
		return getDaysType(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	}

	/**
	 * 判断是否工作日
	 *
	 * @param localDate LocalDate
	 * @return 是否工作日
	 */
	default boolean isWeekdays(LocalDate localDate) {
		return DaysType.WEEKDAYS.equals(getDaysType(localDate));
	}

	/**
	 * 判断是否工作日
	 *
	 * @param localDateTime LocalDateTime
	 * @return 是否工作日
	 */
	default boolean isWeekdays(LocalDateTime localDateTime) {
		return DaysType.WEEKDAYS.equals(getDaysType(localDateTime));
	}

	/**
	 * 判断是否工作日
	 *
	 * @param date Date
	 * @return 是否工作日
	 */
	default boolean isWeekdays(Date date) {
		return DaysType.WEEKDAYS.equals(getDaysType(date));
	}

}
