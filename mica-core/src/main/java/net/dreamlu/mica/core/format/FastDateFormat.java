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

package net.dreamlu.mica.core.format;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.dreamlu.mica.core.utils.DateUtil;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 线程安全、高性能的 DateFormat
 *
 * <p>
 * 用于某些参数为 DateFormat 的组件中，对于业务代码推荐直接使用 DateUtil 性能会更好。
 * </p>
 *
 * @author L.cm
 */
@Getter
@Setter
public class FastDateFormat extends DateFormat {
	private static final java.lang.reflect.Field FIELD = getToStringCacheField();
	private DateTimeFormatter formatter;

	public FastDateFormat(DateTimeFormatter formatter) {
		this.formatter = formatter;
	}

	public FastDateFormat(String pattern) {
		this(DateTimeFormatter.ofPattern(pattern));
	}

	public FastDateFormat(String pattern, Locale locale) {
		this(DateTimeFormatter.ofPattern(pattern, locale));
	}

	@SneakyThrows
	@Override
	public StringBuffer format(Date date, StringBuffer stringBuffer, FieldPosition fieldPosition) {
		FIELD.set(stringBuffer, DateUtil.format(date, formatter));
		return stringBuffer;
	}

	@Override
	public Date parse(String source) {
		return DateUtil.parse(source, formatter);
	}

	@Override
	public Date parse(String dateStr, ParsePosition parsePosition) {
		return null;
	}

	@SneakyThrows
	private static java.lang.reflect.Field getToStringCacheField() {
		java.lang.reflect.Field field = StringBuffer.class.getDeclaredField("toStringCache");
		field.setAccessible(true);
		return field;
	}

	@Override
	public synchronized void setTimeZone(TimeZone zone) {
		formatter = formatter.withZone(zone.toZoneId());
	}

	@Override
	public TimeZone getTimeZone() {
		return TimeZone.getTimeZone(formatter.getZone());
	}

	@Override
	public Object clone() {
		return new FastDateFormat(this.formatter);
	}
}
