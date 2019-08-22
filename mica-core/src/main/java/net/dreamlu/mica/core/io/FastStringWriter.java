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

import net.dreamlu.mica.core.utils.StringPool;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

/**
 * FastStringWriter，更改于 jdk CharArrayWriter
 *
 * <p>
 * 1. 去掉了锁
 * 2. 初始容量由 32 改为 64
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

	/**
	 * Writes a character to the buffer.
	 */
	@Override
	public void write(int c) {
		int newCount = count + 1;
		if (newCount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newCount));
		}
		buf[count] = (char) c;
		count = newCount;
	}

	/**
	 * Writes characters to the buffer.
	 *
	 * @param c   the data to be written
	 * @param off the start offset in the data
	 * @param len the number of chars that are written
	 */
	@Override
	public void write(char[] c, int off, int len) {
		if ((off < 0) || (off > c.length) || (len < 0) ||
			((off + len) > c.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		int newCount = count + len;
		if (newCount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newCount));
		}
		System.arraycopy(c, off, buf, count, len);
		count = newCount;
	}

	/**
	 * Write a portion of a string to the buffer.
	 *
	 * @param str String to be written from
	 * @param off Offset from which to start reading characters
	 * @param len Number of characters to be written
	 */
	@Override
	public void write(String str, int off, int len) {
		int newCount = count + len;
		if (newCount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newCount));
		}
		str.getChars(off, off + len, buf, count);
		count = newCount;
	}

	/**
	 * Writes the contents of the buffer to another character stream.
	 *
	 * @param out the output stream to write to
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeTo(Writer out) throws IOException {
		out.write(buf, 0, count);
	}

	/**
	 * Appends the specified character sequence to this writer.
	 *
	 * <p> An invocation of this method of the form <tt>out.append(csq)</tt>
	 * behaves in exactly the same way as the invocation
	 *
	 * <pre>
	 *     out.write(csq.toString()) </pre>
	 *
	 * <p> Depending on the specification of <tt>toString</tt> for the
	 * character sequence <tt>csq</tt>, the entire sequence may not be
	 * appended. For instance, invoking the <tt>toString</tt> method of a
	 * character buffer will return a subsequence whose content depends upon
	 * the buffer's position and limit.
	 *
	 * @param csq The character sequence to append.  If <tt>csq</tt> is
	 *            <tt>null</tt>, then the four characters <tt>"null"</tt> are
	 *            appended to this writer.
	 * @return This writer
	 */
	@Override
	public FastStringWriter append(CharSequence csq) {
		String s = (csq == null ? StringPool.NULL : csq.toString());
		write(s, 0, s.length());
		return this;
	}

	/**
	 * Appends a subsequence of the specified character sequence to this writer.
	 *
	 * <p> An invocation of this method of the form <tt>out.append(csq, start,
	 * end)</tt> when <tt>csq</tt> is not <tt>null</tt>, behaves in
	 * exactly the same way as the invocation
	 *
	 * <pre>
	 *     out.write(csq.subSequence(start, end).toString()) </pre>
	 *
	 * @param csq   The character sequence from which a subsequence will be
	 *              appended.  If <tt>csq</tt> is <tt>null</tt>, then characters
	 *              will be appended as if <tt>csq</tt> contained the four
	 *              characters <tt>"null"</tt>.
	 * @param start The index of the first character in the subsequence
	 * @param end   The index of the character following the last character in the
	 *              subsequence
	 * @return This writer
	 * @throws IndexOutOfBoundsException If <tt>start</tt> or <tt>end</tt> are negative, <tt>start</tt>
	 *                                   is greater than <tt>end</tt>, or <tt>end</tt> is greater than
	 *                                   <tt>csq.length()</tt>
	 */
	@Override
	public FastStringWriter append(CharSequence csq, int start, int end) {
		String s = (csq == null ? StringPool.NULL : csq).subSequence(start, end).toString();
		write(s, 0, s.length());
		return this;
	}

	/**
	 * Appends the specified character to this writer.
	 *
	 * <p> An invocation of this method of the form <tt>out.append(c)</tt>
	 * behaves in exactly the same way as the invocation
	 *
	 * <pre>
	 *     out.write(c) </pre>
	 *
	 * @param c The 16-bit character to append
	 * @return This writer
	 */
	@Override
	public FastStringWriter append(char c) {
		write(c);
		return this;
	}

	/**
	 * Resets the buffer so that you can use it again without
	 * throwing away the already allocated buffer.
	 */
	public void reset() {
		count = 0;
	}

	/**
	 * Returns a copy of the input data.
	 *
	 * @return an array of chars copied from the input data.
	 */
	public char[] toCharArray() {
		return Arrays.copyOf(buf, count);
	}

	/**
	 * Returns the current size of the buffer.
	 *
	 * @return an int representing the current size of the buffer.
	 */
	public int size() {
		return count;
	}

	/**
	 * Converts input data to a string.
	 *
	 * @return the string.
	 */
	@Override
	public String toString() {
		return new String(buf, 0, count);
	}

	/**
	 * Flush the stream.
	 */
	@Override
	public void flush() {
	}

	/**
	 * Close the stream.  This method does not release the buffer, since its
	 * contents might still be required. Note: Invoking this method in this class
	 * will have no effect.
	 */
	@Override
	public void close() {
	}

}
