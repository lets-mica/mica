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

import java.io.Writer;

/**
 * FastStringWriter，更改于 jdk CharArrayWriter
 *
 * <p>
 * 1. 去掉了锁
 * 2. 初始容量由 32 改为 64
 * 3. null 直接返回，不写入
 * </p>
 *
 * @author L.cm
 */
public class FastStringWriter extends Writer {
	/**
	 * The buffer where data is stored.
	 */
	private char[] buf;
	/**
	 * The number of chars in the buffer.
	 */
	private int count;

	/**
	 * Creates a new CharArrayWriter.
	 */
	public FastStringWriter() {
		this(64);
	}

	/**
	 * Creates a new CharArrayWriter with the specified initial size.
	 *
	 * @param initialSize an int specifying the initial buffer size.
	 * @throws IllegalArgumentException if initialSize is negative
	 */
	public FastStringWriter(int initialSize) {
		if (initialSize < 0) {
			throw new IllegalArgumentException("Negative initial size: " + initialSize);
		}
		buf = new char[initialSize];
	}

	@Override
	public void write(int c) {
		int newCount = count + 1;
		ensureCapacityInternal(newCount);
		buf[count] = (char) c;
		count = newCount;
	}

	@Override
	public void write(char[] c, int off, int len) {
		if ((off < 0) || (off > c.length) || (len < 0) ||
			((off + len) > c.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		int newCount = count + len;
		ensureCapacityInternal(newCount);
		System.arraycopy(c, off, buf, count, len);
		count = newCount;
	}

	@Override
	public void write(String str) {
		if (str == null) {
			return;
		}
		write(str, 0, str.length());
	}

	@Override
	public void write(String str, int off, int len) {
		if (str == null) {
			return;
		}
		int newCount = count + len;
		ensureCapacityInternal(newCount);
		str.getChars(off, off + len, buf, count);
		count = newCount;
	}

	private void write(CharSequence s, int start, int end) {
		int len = end - start;
		ensureCapacityInternal(count + len);
		for (int i = start, j = count; i < end; i++, j++) {
			buf[j] = s.charAt(i);
		}
		count += len;
	}

	@Override
	public FastStringWriter append(CharSequence csq) {
		if (csq == null) {
			return this;
		}
		int length = csq.length();
		if (csq instanceof String) {
			write((String) csq, 0, length);
		} else {
			write(csq, 0, csq.length());
		}
		return this;
	}

	@Override
	public FastStringWriter append(CharSequence csq, int start, int end) {
		if (csq == null) {
			return this;
		}
		if (csq instanceof String) {
			write((String) csq, start, end);
		} else {
			write(csq, start, end);
		}
		return this;
	}

	@Override
	public FastStringWriter append(char c) {
		write(c);
		return this;
	}

	@Override
	public String toString() {
		return new String(buf, 0, count);
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() {
	}

	private void ensureCapacityInternal(int minimumCapacity) {
		if (minimumCapacity > buf.length) {
			expandCapacity(minimumCapacity);
		}
	}

	/**
	 * 扩容
	 *
	 * @param minimumCapacity 最小容量
	 */
	private void expandCapacity(int minimumCapacity) {
		int newCapacity = Math.max(buf.length << 1, minimumCapacity);
		char[] newBuff = new char[newCapacity];
		if (count > 0) {
			System.arraycopy(buf, 0, newBuff, 0, count);
		}
		buf = newBuff;
	}
}
