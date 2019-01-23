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

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author L.cm
 */
@UtilityClass
public class DateUtil {

	/**
	 * 添加年
	 *
	 * @param date   时间
	 * @param yearsToAdd 添加的年数
	 * @return 设置后的时间
	 */
	public static Date plusYears(Date date, int yearsToAdd) {
		return DateUtil.plus(date, Period.ofYears(yearsToAdd));
	}

	/**
	 * 添加月
	 *
	 * @param date   时间
	 * @param monthsToAdd 添加的月数
	 * @return 设置后的时间
	 */
	public static Date plusMonths(Date date, int monthsToAdd) {
		return DateUtil.plus(date, Period.ofMonths(monthsToAdd));
	}

	/**
	 * 添加周
	 *
	 * @param date   时间
	 * @param weeksToAdd 添加的周数
	 * @return 设置后的时间
	 */
	public static Date plusWeeks(Date date, int weeksToAdd) {
		return DateUtil.plus(date, Period.ofWeeks(weeksToAdd));
	}

	/**
	 * 添加天
	 *
	 * @param date   时间
	 * @param daysToAdd 添加的天数
	 * @return 设置后的时间
	 */
	public static Date plusDays(Date date, long daysToAdd) {
		return DateUtil.plus(date, Duration.ofDays(daysToAdd));
	}

	/**
	 * 添加小时
	 *
	 * @param date   时间
	 * @param hoursToAdd 添加的小时数
	 * @return 设置后的时间
	 */
	public static Date plusHours(Date date, long hoursToAdd) {
		return DateUtil.plus(date, Duration.ofHours(hoursToAdd));
	}

	/**
	 * 添加分钟
	 *
	 * @param date   时间
	 * @param minutesToAdd 添加的分钟数
	 * @return 设置后的时间
	 */
	public static Date plusMinutes(Date date, long minutesToAdd) {
		return DateUtil.plus(date, Duration.ofMinutes(minutesToAdd));
	}

	/**
	 * 添加秒
	 *
	 * @param date   时间
	 * @param secondsToAdd 添加的秒数
	 * @return 设置后的时间
	 */
	public static Date plusSeconds(Date date, long secondsToAdd) {
		return DateUtil.plus(date, Duration.ofSeconds(secondsToAdd));
	}

	/**
	 * 添加毫秒
	 *
	 * @param date   时间
	 * @param millisToAdd 添加的毫秒数
	 * @return 设置后的时间
	 */
	public static Date plusMillis(Date date, long millisToAdd) {
		return DateUtil.plus(date, Duration.ofMillis(millisToAdd));
	}

	/**
	 * 添加纳秒
	 *
	 * @param date 时间
	 * @param nanosToAdd 添加的纳秒数
	 * @return 设置后的时间
	 */
	public static Date plusNanos(Date date, long nanosToAdd) {
		return DateUtil.plus(date, Duration.ofNanos(nanosToAdd));
	}

	/**
	 * 日期添加时间量
	 *
	 * @param date          时间
	 * @param amount        时间量
	 * @return 设置后的时间
	 */
	public static Date plus(Date date, TemporalAmount amount) {
		Instant instant = date.toInstant();
		instant.plus(amount);
		return Date.from(instant);
	}

	/**
	 * 减少年
	 *
	 * @param date   时间
	 * @param years 减少的年数
	 * @return 设置后的时间
	 */
	public static Date minusYears(Date date, int years) {
		return DateUtil.minus(date, Period.ofYears(years));
	}

	/**
	 * 减少月
	 *
	 * @param date   时间
	 * @param months 减少的月数
	 * @return 设置后的时间
	 */
	public static Date minusMonths(Date date, int months) {
		return DateUtil.minus(date, Period.ofMonths(months));
	}

	/**
	 * 减少周
	 *
	 * @param date   时间
	 * @param weeks 减少的周数
	 * @return 设置后的时间
	 */
	public static Date minusWeeks(Date date, int weeks) {
		return DateUtil.minus(date, Period.ofWeeks(weeks));
	}

	/**
	 * 减少天
	 *
	 * @param date   时间
	 * @param days 减少的天数
	 * @return 设置后的时间
	 */
	public static Date minusDays(Date date, long days) {
		return DateUtil.minus(date, Duration.ofDays(days));
	}

	/**
	 * 减少小时
	 *
	 * @param date   时间
	 * @param hours 减少的小时数
	 * @return 设置后的时间
	 */
	public static Date minusHours(Date date, long hours) {
		return DateUtil.minus(date, Duration.ofHours(hours));
	}

	/**
	 * 减少分钟
	 *
	 * @param date   时间
	 * @param minutes 减少的分钟数
	 * @return 设置后的时间
	 */
	public static Date minusMinutes(Date date, long minutes) {
		return DateUtil.minus(date, Duration.ofMinutes(minutes));
	}

	/**
	 * 减少秒
	 *
	 * @param date   时间
	 * @param seconds 减少的秒数
	 * @return 设置后的时间
	 */
	public static Date minusSeconds(Date date, long seconds) {
		return DateUtil.minus(date, Duration.ofSeconds(seconds));
	}

	/**
	 * 减少毫秒
	 *
	 * @param date   时间
	 * @param millis 减少的毫秒数
	 * @return 设置后的时间
	 */
	public static Date minusMillis(Date date, long millis) {
		return DateUtil.minus(date, Duration.ofMillis(millis));
	}

	/**
	 * 减少纳秒
	 *
	 * @param date 时间
	 * @param nanos 减少的纳秒数
	 * @return 设置后的时间
	 */
	public static Date minusNanos(Date date, long nanos) {
		return DateUtil.minus(date, Duration.ofNanos(nanos));
	}

	/**
	 * 日期减少时间量
	 *
	 * @param date          时间
	 * @param amount        时间量
	 * @return 设置后的时间
	 */
	public static Date minus(Date date, TemporalAmount amount) {
		Instant instant = date.toInstant();
		instant.minus(amount);
		return Date.from(instant);
	}

	public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_DATE = "yyyy-MM-dd";
	public static final String PATTERN_TIME = "HH:mm:ss";
	/**
	 * 格式化
 	 */
	public static final ConcurrentDateFormat DATETIME_FORMAT = ConcurrentDateFormat.of(PATTERN_DATETIME);
	public static final ConcurrentDateFormat DATE_FORMAT = ConcurrentDateFormat.of(PATTERN_DATE);
	public static final ConcurrentDateFormat TIME_FORMAT = ConcurrentDateFormat.of(PATTERN_TIME);

	/**
	 * 日期时间格式化
	 *
	 * @param date    时间
	 * @return 格式化后的时间
	 */
	public static String formatDateTime(Date date) {
		return DATETIME_FORMAT.format(date);
	}

	/**
	 * 日期格式化
	 *
	 * @param date    时间
	 * @return 格式化后的时间
	 */
	public static String formatDate(Date date) {
		return DATE_FORMAT.format(date);
	}

	/**
	 * 时间格式化
	 *
	 * @param date    时间
	 * @return 格式化后的时间
	 */
	public static String formatTime(Date date) {
		return TIME_FORMAT.format(date);
	}

	/**
	 * 日期格式化
	 *
	 * @param date    时间
	 * @param pattern 表达式
	 * @return 格式化后的时间
	 */
	public static String format(Date date, String pattern) {
		return ConcurrentDateFormat.of(pattern).format(date);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param pattern 表达式
	 * @return 时间
	 */
	public static Date parse(String dateStr, String pattern) {
		ConcurrentDateFormat format = ConcurrentDateFormat.of(pattern);
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param format ConcurrentDateFormat
	 * @return 时间
	 */
	public static Date parse(String dateStr, ConcurrentDateFormat format) {
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 转换成java8 时间
	 * @param date Date
	 * @return LocalDateTime
	 */
	public static LocalDateTime toDateTime(Date date) {
		return DateTimeUtil.toDateTime(date.toInstant());
	}

	/**
	 * 比较2个 时间差
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return 时间间隔
	 */
	public static Duration between(Date startDate, Date endDate) {
		return Duration.between(startDate.toInstant(), endDate.toInstant());
	}

}
