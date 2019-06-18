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

/**
 * 数字类型工具类
 *
 * @author L.cm
 */
@UtilityClass
public class NumberUtil extends org.springframework.util.NumberUtils {

	/**
	 * <p>Convert a <code>String</code> to an <code>int</code>, returning
	 * <code>zero</code> if the conversion fails.</p>
	 *
	 * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
	 *
	 * <pre>
	 *   NumberUtil.toInt(null) = 0
	 *   NumberUtil.toInt("")   = 0
	 *   NumberUtil.toInt("1")  = 1
	 * </pre>
	 *
	 * @param str the string to convert, may be null
	 * @return the int represented by the string, or <code>zero</code> if
	 * conversion fails
	 */
	public static int toInt(final String str) {
		return ObjectUtil.toInt(str, 0);
	}

	/**
	 * <p>Convert a <code>String</code> to an <code>int</code>, returning a
	 * default value if the conversion fails.</p>
	 *
	 * <p>If the string is <code>null</code>, the default value is returned.</p>
	 *
	 * <pre>
	 *   NumberUtil.toInt(null, 1) = 1
	 *   NumberUtil.toInt("", 1)   = 1
	 *   NumberUtil.toInt("1", 0)  = 1
	 * </pre>
	 *
	 * @param str          the string to convert, may be null
	 * @param defaultValue the default value
	 * @return the int represented by the string, or the default if conversion fails
	 */
	public static int toInt(@Nullable final String str, final int defaultValue) {
		return ObjectUtil.toInt(str, defaultValue);
	}

	/**
	 * <p>Convert a <code>String</code> to a <code>long</code>, returning
	 * <code>zero</code> if the conversion fails.</p>
	 *
	 * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
	 *
	 * <pre>
	 *   NumberUtil.toLong(null) = 0L
	 *   NumberUtil.toLong("")   = 0L
	 *   NumberUtil.toLong("1")  = 1L
	 * </pre>
	 *
	 * @param str the string to convert, may be null
	 * @return the long represented by the string, or <code>0</code> if
	 * conversion fails
	 */
	public static long toLong(final String str) {
		return ObjectUtil.toLong(str, 0L);
	}

	/**
	 * <p>Convert a <code>String</code> to a <code>long</code>, returning a
	 * default value if the conversion fails.</p>
	 *
	 * <p>If the string is <code>null</code>, the default value is returned.</p>
	 *
	 * <pre>
	 *   NumberUtil.toLong(null, 1L) = 1L
	 *   NumberUtil.toLong("", 1L)   = 1L
	 *   NumberUtil.toLong("1", 0L)  = 1L
	 * </pre>
	 *
	 * @param str          the string to convert, may be null
	 * @param defaultValue the default value
	 * @return the long represented by the string, or the default if conversion fails
	 */
	public static long toLong(@Nullable final String str, final long defaultValue) {
		return ObjectUtil.toLong(str, defaultValue);
	}

	/**
	 * All possible chars for representing a number as a String
	 */
	final static byte[] DIGITS = {
		'0' , '1' , '2' , '3' , '4' , '5' ,
		'6' , '7' , '8' , '9' , 'a' , 'b' ,
		'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
		'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
		'o' , 'p' , 'q' , 'r' , 's' , 't' ,
		'u' , 'v' , 'w' , 'x' , 'y' , 'z' ,
		'A' , 'B' , 'C' , 'D' , 'E' , 'F' ,
		'G' , 'H' , 'I' , 'J' , 'K' , 'L' ,
		'M' , 'N' , 'O' , 'P' , 'Q' , 'R' ,
		'S' , 'T' , 'U' , 'V' , 'W' , 'X' ,
		'Y' , 'Z'
	};

	/**
	 * 将 long 转短字符串 为 62 进制
	 * @param i 数字
	 * @return 短字符串
	 */
	public static String to62Str(long i) {
		int radix = DIGITS.length;
		byte[] buf = new byte[65];
		int charPos = 64;
		i = -i;
		while (i <= -radix) {
			buf[charPos--] = DIGITS[(int)(-(i % radix))];
			i = i / radix;
		}
		buf[charPos] = DIGITS[(int)(-i)];
		return new String(buf, charPos, (65 - charPos), Charsets.UTF_8);
	}

}
