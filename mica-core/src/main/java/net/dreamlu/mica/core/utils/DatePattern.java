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


import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 常用日期格式，参考自：hutool
 *
 * @author L.cm
 */
public interface DatePattern {

	//-------------------------------------------------------------------------------------------------------------------------------- Normal
	/**
	 * 标准日期格式：yyyy-MM-dd
	 */
	String NORM_DATE_PATTERN = "yyyy-MM-dd";
	/**
	 * 标准日期格式 {@link DateTimeFormatter}：yyyy-MM-dd
	 */
	DateTimeFormatter NORM_DATE_FORMAT = DateTimeFormatter.ofPattern(NORM_DATE_PATTERN).withZone(ZoneId.systemDefault());

	/**
	 * 标准时间格式：HH:mm:ss
	 */
	String NORM_TIME_PATTERN = "HH:mm:ss";
	/**
	 * 标准时间格式 {@link DateTimeFormatter}：HH:mm:ss
	 */
	DateTimeFormatter NORM_TIME_FORMAT = DateTimeFormatter.ofPattern(NORM_TIME_PATTERN).withZone(ZoneId.systemDefault());

	/**
	 * 标准日期时间格式，精确到分：yyyy-MM-dd HH:mm
	 */
	String NORM_DATETIME_MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
	/**
	 * 标准日期时间格式，精确到分 {@link DateTimeFormatter}：yyyy-MM-dd HH:mm
	 */
	DateTimeFormatter NORM_DATETIME_MINUTE_FORMAT = DateTimeFormatter.ofPattern(NORM_DATETIME_MINUTE_PATTERN).withZone(ZoneId.systemDefault());

	/**
	 * 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss
	 */
	String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 标准日期时间格式，精确到秒 {@link DateTimeFormatter}：yyyy-MM-dd HH:mm:ss
	 */
	DateTimeFormatter NORM_DATETIME_FORMAT = DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN).withZone(ZoneId.systemDefault());

	/**
	 * 标准日期时间格式，精确到毫秒：yyyy-MM-dd HH:mm:ss.SSS
	 */
	String NORM_DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
	/**
	 * 标准日期时间格式，精确到毫秒 {@link DateTimeFormatter}：yyyy-MM-dd HH:mm:ss.SSS
	 */
	DateTimeFormatter NORM_DATETIME_MS_FORMAT = DateTimeFormatter.ofPattern(NORM_DATETIME_MS_PATTERN).withZone(ZoneId.systemDefault());

	/**
	 * 标准日期格式：yyyy年MM月dd日
	 */
	String CHINESE_DATE_PATTERN = "yyyy年MM月dd日";
	/**
	 * 标准日期格式 {@link DateTimeFormatter}：yyyy年MM月dd日
	 */
	DateTimeFormatter CHINESE_DATE_FORMAT = DateTimeFormatter.ofPattern(CHINESE_DATE_PATTERN).withZone(ZoneId.systemDefault());

	//-------------------------------------------------------------------------------------------------------------------------------- Pure
	/**
	 * 标准日期格式：yyyyMMdd
	 */
	String PURE_DATE_PATTERN = "yyyyMMdd";
	/**
	 * 标准日期格式 {@link DateTimeFormatter}：yyyyMMdd
	 */
	DateTimeFormatter PURE_DATE_FORMAT = DateTimeFormatter.ofPattern(PURE_DATE_PATTERN).withZone(ZoneId.systemDefault());

	/**
	 * 标准日期格式：HHmmss
	 */
	String PURE_TIME_PATTERN = "HHmmss";
	/**
	 * 标准日期格式 {@link DateTimeFormatter}：HHmmss
	 */
	DateTimeFormatter PURE_TIME_FORMAT = DateTimeFormatter.ofPattern(PURE_TIME_PATTERN);

	/**
	 * 标准日期格式：yyyyMMddHHmmss
	 */
	String PURE_DATETIME_PATTERN = "yyyyMMddHHmmss";
	/**
	 * 标准日期格式 {@link DateTimeFormatter}：yyyyMMddHHmmss
	 */
	DateTimeFormatter PURE_DATETIME_FORMAT = DateTimeFormatter.ofPattern(PURE_DATETIME_PATTERN);

	/**
	 * 标准日期格式：yyyyMMddHHmmssSSS
	 */
	String PURE_DATETIME_MS_PATTERN = "yyyyMMddHHmmssSSS";
	/**
	 * 标准日期格式 {@link DateTimeFormatter}：yyyyMMddHHmmssSSS
	 */
	DateTimeFormatter PURE_DATETIME_MS_FORMAT = DateTimeFormatter.ofPattern(PURE_DATETIME_MS_PATTERN);

	//-------------------------------------------------------------------------------------------------------------------------------- Others
	/**
	 * HTTP头中日期时间格式：EEE, dd MMM yyyy HH:mm:ss z
	 */
	String HTTP_DATETIME_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
	/**
	 * HTTP头中日期时间格式 {@link DateTimeFormatter}：EEE, dd MMM yyyy HH:mm:ss z
	 */
	DateTimeFormatter HTTP_DATETIME_FORMAT = DateTimeFormatter.ofPattern(HTTP_DATETIME_PATTERN, Locale.US).withZone(ZoneId.of("GMT"));

	/**
	 * JDK中日期时间格式：EEE MMM dd HH:mm:ss zzz yyyy
	 */
	String JDK_DATETIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";
	/**
	 * JDK中日期时间格式 {@link DateTimeFormatter}：EEE MMM dd HH:mm:ss zzz yyyy
	 */
	DateTimeFormatter JDK_DATETIME_FORMAT = DateTimeFormatter.ofPattern(JDK_DATETIME_PATTERN, Locale.US);

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ss'Z'
	 */
	String UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	/**
	 * UTC时间{@link DateTimeFormatter}：yyyy-MM-dd'T'HH:mm:ss'Z'
	 */
	DateTimeFormatter UTC_FORMAT = DateTimeFormatter.ofPattern(UTC_PATTERN).withZone(ZoneOffset.UTC);

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ssZ
	 */
	String UTC_WITH_ZONE_OFFSET_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
	/**
	 * UTC时间{@link DateTimeFormatter}：yyyy-MM-dd'T'HH:mm:ssZ
	 */
	DateTimeFormatter UTC_WITH_ZONE_OFFSET_FORMAT = DateTimeFormatter.ofPattern(UTC_WITH_ZONE_OFFSET_PATTERN).withZone(ZoneOffset.UTC);

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
	 */
	String UTC_MS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	/**
	 * UTC时间{@link DateTimeFormatter}：yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
	 */
	DateTimeFormatter UTC_MS_FORMAT = DateTimeFormatter.ofPattern(UTC_MS_PATTERN).withZone(ZoneOffset.UTC);

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ssZ
	 */
	String UTC_MS_WITH_ZONE_OFFSET_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	/**
	 * UTC时间{@link DateTimeFormatter}：yyyy-MM-dd'T'HH:mm:ssZ
	 */
	DateTimeFormatter UTC_MS_WITH_ZONE_OFFSET_FORMAT = DateTimeFormatter.ofPattern(UTC_MS_WITH_ZONE_OFFSET_PATTERN).withZone(ZoneOffset.UTC);

}
