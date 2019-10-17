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
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 日期工具类
 *
 * @author L.cm
 */
@UtilityClass
public class DateUtil {
	public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_DATE = "yyyy-MM-dd";
	public static final String PATTERN_TIME = "HH:mm:ss";
	/**
	 * java 8 时间格式化
	 */
	public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATETIME).withZone(ZoneId.systemDefault());
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATE).withZone(ZoneId.systemDefault());
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(DateUtil.PATTERN_TIME).withZone(ZoneId.systemDefault());

	/**
	 * 添加年
	 *
	 * @param date       时间
	 * @param yearsToAdd 添加的年数
	 * @return 设置后的时间
	 */
	public static Date plusYears(Date date, int yearsToAdd) {
		return DateUtil.set(date, Calendar.YEAR, yearsToAdd);
	}

	/**
	 * 添加月
	 *
	 * @param date        时间
	 * @param monthsToAdd 添加的月数
	 * @return 设置后的时间
	 */
	public static Date plusMonths(Date date, int monthsToAdd) {
		return DateUtil.set(date, Calendar.MONTH, monthsToAdd);
	}

	/**
	 * 添加周
	 *
	 * @param date       时间
	 * @param weeksToAdd 添加的周数
	 * @return 设置后的时间
	 */
	public static Date plusWeeks(Date date, int weeksToAdd) {
		return DateUtil.plus(date, Period.ofWeeks(weeksToAdd));
	}

	/**
	 * 添加天
	 *
	 * @param date      时间
	 * @param daysToAdd 添加的天数
	 * @return 设置后的时间
	 */
	public static Date plusDays(Date date, long daysToAdd) {
		return DateUtil.plus(date, Duration.ofDays(daysToAdd));
	}

	/**
	 * 添加小时
	 *
	 * @param date       时间
	 * @param hoursToAdd 添加的小时数
	 * @return 设置后的时间
	 */
	public static Date plusHours(Date date, long hoursToAdd) {
		return DateUtil.plus(date, Duration.ofHours(hoursToAdd));
	}

	/**
	 * 添加分钟
	 *
	 * @param date         时间
	 * @param minutesToAdd 添加的分钟数
	 * @return 设置后的时间
	 */
	public static Date plusMinutes(Date date, long minutesToAdd) {
		return DateUtil.plus(date, Duration.ofMinutes(minutesToAdd));
	}

	/**
	 * 添加秒
	 *
	 * @param date         时间
	 * @param secondsToAdd 添加的秒数
	 * @return 设置后的时间
	 */
	public static Date plusSeconds(Date date, long secondsToAdd) {
		return DateUtil.plus(date, Duration.ofSeconds(secondsToAdd));
	}

	/**
	 * 添加毫秒
	 *
	 * @param date        时间
	 * @param millisToAdd 添加的毫秒数
	 * @return 设置后的时间
	 */
	public static Date plusMillis(Date date, long millisToAdd) {
		return DateUtil.plus(date, Duration.ofMillis(millisToAdd));
	}

	/**
	 * 添加纳秒
	 *
	 * @param date       时间
	 * @param nanosToAdd 添加的纳秒数
	 * @return 设置后的时间
	 */
	public static Date plusNanos(Date date, long nanosToAdd) {
		return DateUtil.plus(date, Duration.ofNanos(nanosToAdd));
	}

	/**
	 * 日期添加时间量
	 *
	 * @param date   时间
	 * @param amount 时间量
	 * @return 设置后的时间
	 */
	public static Date plus(Date date, TemporalAmount amount) {
		Instant instant = date.toInstant();
		return Date.from(instant.plus(amount));
	}

	/**
	 * 减少年
	 *
	 * @param date  时间
	 * @param years 减少的年数
	 * @return 设置后的时间
	 */
	public static Date minusYears(Date date, int years) {
		return DateUtil.set(date, Calendar.YEAR, -years);
	}

	/**
	 * 减少月
	 *
	 * @param date   时间
	 * @param months 减少的月数
	 * @return 设置后的时间
	 */
	public static Date minusMonths(Date date, int months) {
		return DateUtil.set(date, Calendar.MONTH, -months);
	}

	/**
	 * 减少周
	 *
	 * @param date  时间
	 * @param weeks 减少的周数
	 * @return 设置后的时间
	 */
	public static Date minusWeeks(Date date, int weeks) {
		return DateUtil.minus(date, Period.ofWeeks(weeks));
	}

	/**
	 * 减少天
	 *
	 * @param date 时间
	 * @param days 减少的天数
	 * @return 设置后的时间
	 */
	public static Date minusDays(Date date, long days) {
		return DateUtil.minus(date, Duration.ofDays(days));
	}

	/**
	 * 减少小时
	 *
	 * @param date  时间
	 * @param hours 减少的小时数
	 * @return 设置后的时间
	 */
	public static Date minusHours(Date date, long hours) {
		return DateUtil.minus(date, Duration.ofHours(hours));
	}

	/**
	 * 减少分钟
	 *
	 * @param date    时间
	 * @param minutes 减少的分钟数
	 * @return 设置后的时间
	 */
	public static Date minusMinutes(Date date, long minutes) {
		return DateUtil.minus(date, Duration.ofMinutes(minutes));
	}

	/**
	 * 减少秒
	 *
	 * @param date    时间
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
	 * @param date  时间
	 * @param nanos 减少的纳秒数
	 * @return 设置后的时间
	 */
	public static Date minusNanos(Date date, long nanos) {
		return DateUtil.minus(date, Duration.ofNanos(nanos));
	}

	/**
	 * 日期减少时间量
	 *
	 * @param date   时间
	 * @param amount 时间量
	 * @return 设置后的时间
	 */
	public static Date minus(Date date, TemporalAmount amount) {
		Instant instant = date.toInstant();
		return Date.from(instant.minus(amount));
	}

	/**
	 * 设置日期属性
	 *
	 * @param date          时间
	 * @param calendarField 更改的属性
	 * @param amount        更改数，-1表示减少
	 * @return 设置后的时间
	 */
	private static Date set(Date date, int calendarField, int amount) {
		Assert.notNull(date, "The date must not be null");
		Calendar c = Calendar.getInstance();
		c.setLenient(false);
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	/**
	 * 日期时间格式化
	 *
	 * @param date 时间
	 * @return 格式化后的时间
	 */
	@Nullable
	public static String formatDateTime(@Nullable Date date) {
		if (date == null) {
			return null;
		}
		return DATETIME_FORMATTER.format(date.toInstant());
	}

	/**
	 * 日期格式化
	 *
	 * @param date 时间
	 * @return 格式化后的时间
	 */
	@Nullable
	public static String formatDate(@Nullable Date date) {
		if (date == null) {
			return null;
		}
		return DATE_FORMATTER.format(date.toInstant());
	}

	/**
	 * 时间格式化
	 *
	 * @param date 时间
	 * @return 格式化后的时间
	 */
	@Nullable
	public static String formatTime(@Nullable Date date) {
		if (date == null) {
			return null;
		}
		return TIME_FORMATTER.format(date.toInstant());
	}

	/**
	 * 日期格式化
	 *
	 * @param date    时间
	 * @param pattern 表达式
	 * @return 格式化后的时间
	 */
	@Nullable
	public static String format(@Nullable Date date, String pattern) {
		if (date == null) {
			return null;
		}
		return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).format(date.toInstant());
	}

	/**
	 * java8 日期时间格式化
	 *
	 * @param temporal 时间
	 * @return 格式化后的时间
	 */
	public static String formatDateTime(TemporalAccessor temporal) {
		return DATETIME_FORMATTER.format(temporal);
	}

	/**
	 * java8 日期时间格式化
	 *
	 * @param temporal 时间
	 * @return 格式化后的时间
	 */
	public static String formatDate(TemporalAccessor temporal) {
		return DATE_FORMATTER.format(temporal);
	}

	/**
	 * java8 时间格式化
	 *
	 * @param temporal 时间
	 * @return 格式化后的时间
	 */
	public static String formatTime(TemporalAccessor temporal) {
		return TIME_FORMATTER.format(temporal);
	}

	/**
	 * java8 日期格式化
	 *
	 * @param temporal 时间
	 * @param pattern  表达式
	 * @return 格式化后的时间
	 */
	public static String format(TemporalAccessor temporal, String pattern) {
		return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).format(temporal);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param pattern 表达式
	 * @return 时间
	 */
	public static Date parse(String dateStr, String pattern) {
		return DateUtil.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param format  DateTimeFormatter
	 * @return 时间
	 */
	public static Date parse(String dateStr, DateTimeFormatter format) {
		if (format.getZone() == null) {
			format = format.withZone(ZoneId.systemDefault());
		}
		Instant instant = format.parse(dateStr, Instant::from);
		return Date.from(instant);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param pattern 表达式
	 * @return 时间
	 */
	public static <T> T parse(String dateStr, String pattern, TemporalQuery<T> query) {
		return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).parse(dateStr, query);
	}

	/**
	 * 时间转 Instant
	 *
	 * @param dateTime 时间
	 * @return Instant
	 */
	public static Instant toInstant(LocalDateTime dateTime) {
		return dateTime.atZone(ZoneId.systemDefault()).toInstant();
	}

	/**
	 * Instant 转 时间
	 *
	 * @param instant Instant
	 * @return Instant
	 */
	public static LocalDateTime toDateTime(Instant instant) {
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	/**
	 * Date 转 LocalDateTime
	 *
	 * @param date Date
	 * @return Instant
	 */
	public static LocalDateTime toDateTime(Date date) {
		return DateUtil.toDateTime(date.toInstant());
	}

	/**
	 * 转换成 date
	 *
	 * @param dateTime LocalDateTime
	 * @return Date
	 */
	public static Date toDate(LocalDateTime dateTime) {
		return Date.from(DateUtil.toInstant(dateTime));
	}

	/**
	 * 转换成 date
	 *
	 * @param localDate LocalDate
	 * @return Date
	 */
	public static Date toDate(final LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	/**
	 * Converts local date time to Calendar.
	 */
	public static Calendar toCalendar(final LocalDateTime localDateTime) {
		return GregorianCalendar.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
	}

	/**
	 * localDateTime 转换成毫秒数
	 *
	 * @param localDateTime LocalDateTime
	 * @return long
	 */
	public static long toMilliseconds(final LocalDateTime localDateTime) {
		return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	/**
	 * localDate 转换成毫秒数
	 *
	 * @param localDate LocalDate
	 * @return long
	 */
	public static long toMilliseconds(LocalDate localDate) {
		return toMilliseconds(localDate.atStartOfDay());
	}

	/**
	 * 转换成java8 时间
	 *
	 * @param calendar 日历
	 * @return LocalDateTime
	 */
	public static LocalDateTime fromCalendar(final Calendar calendar) {
		TimeZone tz = calendar.getTimeZone();
		ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
		return LocalDateTime.ofInstant(calendar.toInstant(), zid);
	}

	/**
	 * 转换成java8 时间
	 *
	 * @param instant Instant
	 * @return LocalDateTime
	 */
	public static LocalDateTime fromInstant(final Instant instant) {
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	/**
	 * 转换成java8 时间
	 *
	 * @param date Date
	 * @return LocalDateTime
	 */
	public static LocalDateTime fromDate(final Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	/**
	 * 转换成java8 时间
	 *
	 * @param milliseconds 毫秒数
	 * @return LocalDateTime
	 */
	public static LocalDateTime fromMilliseconds(final long milliseconds) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
	}

	/**
	 * 比较2个时间差，跨度比较小
	 *
	 * @param startInclusive 开始时间
	 * @param endExclusive   结束时间
	 * @return 时间间隔
	 */
	public static Duration between(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive);
	}

	/**
	 * 比较2个时间差，跨度比较大，年月日为单位
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return 时间间隔
	 */
	public static Period between(LocalDate startDate, LocalDate endDate) {
		return Period.between(startDate, endDate);
	}

	/**
	 * 比较2个 时间差
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return 时间间隔
	 */
	public static Duration between(Date startDate, Date endDate) {
		return Duration.between(startDate.toInstant(), endDate.toInstant());
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param pattern 表达式
	 * @return 时间
	 */
	public static LocalDateTime parseDateTime(String dateStr, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return DateUtil.parseDateTime(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr   时间字符串
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
		return DateUtil.parseDateTime(dateStr, DateUtil.DATETIME_FORMATTER);
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
		return DateUtil.parseDate(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr   时间字符串
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
		return DateUtil.parseDate(dateStr, DateUtil.DATE_FORMATTER);
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
		return DateUtil.parseTime(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr   时间字符串
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
		return DateUtil.parseTime(dateStr, DateUtil.TIME_FORMATTER);
	}

}
