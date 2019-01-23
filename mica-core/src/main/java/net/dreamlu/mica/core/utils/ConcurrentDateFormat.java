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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 参考tomcat8中的并发DateFormat
 * <p>
 * {@link SimpleDateFormat}的线程安全包装器。
 * 不使用ThreadLocal，创建足够的SimpleDateFormat对象来满足并发性要求。
 * </p>
 *
 * @author L.cm
 */
public class ConcurrentDateFormat {
	private final String format;
	private final Locale locale;
	private final TimeZone timezone;
	private final Queue<SimpleDateFormat> queue = new ConcurrentLinkedQueue<>();

	private ConcurrentDateFormat(String format, Locale locale, TimeZone timezone) {
		this.format = format;
		this.locale = locale;
		this.timezone = timezone;
		SimpleDateFormat initial = createInstance();
		queue.add(initial);
	}

	public static ConcurrentDateFormat of(String format) {
		return new ConcurrentDateFormat(format, Locale.getDefault(), TimeZone.getDefault());
	}

	public static ConcurrentDateFormat of(String format, TimeZone timezone) {
		return new ConcurrentDateFormat(format, Locale.getDefault(), timezone);
	}

	public static ConcurrentDateFormat of(String format, Locale locale, TimeZone timezone) {
		return new ConcurrentDateFormat(format, locale, timezone);
	}

	public String format(Date date) {
		SimpleDateFormat sdf = queue.poll();
		if (sdf == null) {
			sdf = createInstance();
		}
		String result = sdf.format(date);
		queue.add(sdf);
		return result;
	}

	public Date parse(String source) throws ParseException {
		SimpleDateFormat sdf = queue.poll();
		if (sdf == null) {
			sdf = createInstance();
		}
		Date result = sdf.parse(source);
		queue.add(sdf);
		return result;
	}

	private SimpleDateFormat createInstance() {
		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
		sdf.setTimeZone(timezone);
		return sdf;
	}
}
