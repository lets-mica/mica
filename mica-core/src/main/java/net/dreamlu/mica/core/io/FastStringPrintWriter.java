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

package net.dreamlu.mica.core.io;

import net.dreamlu.mica.core.utils.CharPool;

import java.io.PrintWriter;

/**
 * 快速的 PrintWriter，用来处理异常信息，转化为字符串
 *
 * <p>
 * 1. 默认容量为 256
 * </p>
 *
 * @author L.cm
 */
public class FastStringPrintWriter extends PrintWriter {
	private final FastStringWriter writer;

	public FastStringPrintWriter() {
		this(256);
	}

	public FastStringPrintWriter(int initialSize) {
		super(new FastStringWriter(initialSize));
		this.writer = (FastStringWriter) out;
	}

	/**
	 * Throwable printStackTrace，只掉用了该方法
	 *
	 * @param x Object
	 */
	@Override
	public void println(Object x) {
		writer.write(String.valueOf(x));
		writer.write(CharPool.NEWLINE);
	}

	@Override
	public String toString() {
		return writer.toString();
	}
}
