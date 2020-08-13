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
import lombok.SneakyThrows;
import net.dreamlu.mica.core.utils.DateUtil;
import net.dreamlu.mica.core.utils.ReflectUtil;

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
public class FastDateFormat extends DateFormat {
	private static final java.lang.reflect.Field FIELD = getToStringCacheField();
	private static final boolean FIELD_IS_STRING = FIELD != null && FIELD.getType() == String.class;
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
		String value = DateUtil.format(date, formatter);
		if (value == null) {
			return stringBuffer;
		}
		// jdk8-b91 之前没有该方法
		if (FIELD == null) {
			return stringBuffer.append(value);
		}
		// 兼容 java8 和 java8+
		if (FIELD_IS_STRING) {
			FIELD.set(stringBuffer, value);
		} else {
			FIELD.set(stringBuffer, value.toCharArray());
		}
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
		java.lang.reflect.Field field = ReflectUtil.getField(StringBuffer.class, "toStringCache");
		if (field != null) {
			field.setAccessible(true);
		}
		return field;
	}

	@Override
	public synchronized void setTimeZone(TimeZone zone) {
		this.formatter = this.formatter.withZone(zone.toZoneId());
	}

	@Override
	public TimeZone getTimeZone() {
		return TimeZone.getTimeZone(this.formatter.getZone());
	}

	@Override
	public Object clone() {
		// formatter 线程安全，返回 this 性能会更好。
		return this;
	}
}
