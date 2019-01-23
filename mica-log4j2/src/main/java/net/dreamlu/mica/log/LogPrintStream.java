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

package net.dreamlu.mica.log;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintStream;
import java.util.Locale;

/**
 * 替换 系统 System.err 和 System.out 为log
 *
 * @author L.cm
 */
@Slf4j
public class LogPrintStream extends PrintStream {
	private final boolean error;

	private LogPrintStream(boolean error) {
		super(error ? System.err : System.out);
		this.error = error;
	}

	public static LogPrintStream out() {
		return new LogPrintStream(false);
	}

	public static LogPrintStream err() {
		return new LogPrintStream(true);
	}

	@Override
	public void print(String s) {
		if (error) {
			log.error(s);
		} else {
			log.info(s);
		}
	}

	/**
	 * 重写掉它，因为它会打印很多无用的新行
	 */
	@Override
	public void println() {}

	@Override
	public void println(String x) {
		if (error) {
			log.error(x);
		} else {
			log.info(x);
		}
	}

	@Override
	public PrintStream printf(String format, Object... args) {
		if (error) {
			log.error(String.format(format, args));
		} else {
			log.info(String.format(format, args));
		}
		return this;
	}

	@Override
	public PrintStream printf(Locale l, String format, Object... args) {
		if (error) {
			log.error(String.format(l, format, args));
		} else {
			log.info(String.format(l, format, args));
		}
		return this;
	}
}
