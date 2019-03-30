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

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * DateTime 工具类
 *
 * @author L.cm
 */
@UtilityClass
public class DateTimeUtil {

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param pattern 表达式
	 * @return 时间
	 */
	public static LocalDateTime parseDateTime(String dateStr, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return DateTimeUtil.parseDateTime(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param formatter DateTimeFormatter
	 * @return 时间
	 */
	public static LocalDateTime parseDateTime(String dateStr, DateTimeFormatter formatter) {
		return LocalDateTime.parse(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @return 时间
	 */
	public static LocalDateTime parseDateTime(String dateStr) {
		return DateTimeUtil.parseDateTime(dateStr, DateUtil.DATETIME_FORMATTER);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param pattern 表达式
	 * @return 时间
	 */
	public static LocalDate parseDate(String dateStr, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return DateTimeUtil.parseDate(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param formatter DateTimeFormatter
	 * @return 时间
	 */
	public static LocalDate parseDate(String dateStr, DateTimeFormatter formatter) {
		return LocalDate.parse(dateStr, formatter);
	}

	/**
	 * 将字符串转换为日期
	 *
	 * @param dateStr 时间字符串
	 * @return 时间
	 */
	public static LocalDate parseDate(String dateStr) {
		return DateTimeUtil.parseDate(dateStr, DateUtil.DATE_FORMATTER);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param pattern 时间正则
	 * @return 时间
	 */
	public static LocalTime parseTime(String dateStr, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return DateTimeUtil.parseTime(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param formatter DateTimeFormatter
	 * @return 时间
	 */
	public static LocalTime parseTime(String dateStr, DateTimeFormatter formatter) {
		return LocalTime.parse(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @return 时间
	 */
	public static LocalTime parseTime(String dateStr) {
		return DateTimeUtil.parseTime(dateStr, DateUtil.TIME_FORMATTER);
	}

}
