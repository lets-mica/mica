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

import org.jspecify.annotations.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * hex 工具，编解码全用 byte
 *
 * @author L.cm
 */
public class HexUtil {
	private static final byte[] DIGITS_LOWER = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	private static final byte[] DIGITS_UPPER = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	/**
	 * encode Hex
	 *
	 * @param data data to hex
	 * @return hex bytes
	 */
	public static byte[] encode(byte[] data) {
		return encode(data, true);
	}

	/**
	 * encode Hex
	 *
	 * @param data        data to hex
	 * @param toLowerCase 是否小写
	 * @return hex bytes
	 */
	public static byte[] encode(byte[] data, boolean toLowerCase) {
		return encode(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	}

	/**
	 * encode Hex
	 *
	 * @param data Data to Hex
	 * @return bytes as a hex string
	 */
	private static byte[] encode(byte[] data, byte[] digits) {
		int len = data.length;
		byte[] out = new byte[len << 1];
		for (int i = 0, j = 0; i < len; i++) {
			out[j++] = digits[(0xF0 & data[i]) >>> 4];
			out[j++] = digits[0xF & data[i]];
		}
		return out;
	}

	/**
	 * encode Hex
	 *
	 * @param data        Data to Hex
	 * @param toLowerCase 是否小写
	 * @return bytes as a hex string
	 */
	public static String encodeToString(byte[] data, boolean toLowerCase) {
		return new String(encode(data, toLowerCase), StandardCharsets.UTF_8);
	}

	/**
	 * encode Hex
	 *
	 * @param data Data to Hex
	 * @return bytes as a hex string
	 */
	public static String encodeToString(byte[] data) {
		return encodeToString(data, StandardCharsets.UTF_8);
	}

	/**
	 * encode Hex
	 *
	 * @param data    Data to Hex
	 * @param charset Charset
	 * @return bytes as a hex string
	 */
	public static String encodeToString(byte[] data, Charset charset) {
		return new String(encode(data), charset);
	}

	/**
	 * encode Hex
	 *
	 * @param data Data to Hex
	 * @return bytes as a hex string
	 */
	@Nullable
	public static String encodeToString(@Nullable String data) {
		if (StringUtil.isBlank(data)) {
			return null;
		}
		return encodeToString(data.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * encode Hex
	 *
	 * @param data    Data to Hex
	 * @param charset Charset
	 * @return bytes as a hex string
	 */
	public static String encodeToString(String data, Charset charset) {
		return encodeToString(data, charset, charset);
	}

	/**
	 * encode Hex
	 *
	 * @param data        Data to Hex
	 * @param dataCharset Charset
	 * @param toCharset   Charset
	 * @return bytes as a hex string
	 */
	public static String encodeToString(String data, Charset dataCharset, Charset toCharset) {
		if (StringUtil.isBlank(data)) {
			return null;
		}
		return encodeToString(data.getBytes(dataCharset), toCharset);
	}

	/**
	 * decode Hex
	 *
	 * @param data Hex data
	 * @return decode hex to bytes
	 */
	public static byte[] decode(String data) {
		return decode(data, StandardCharsets.UTF_8);
	}

	/**
	 * decode Hex
	 *
	 * @param data    Hex data
	 * @param charset Charset
	 * @return decode hex to bytes
	 */
	public static byte[] decode(String data, Charset charset) {
		if (StringUtil.isBlank(data)) {
			return null;
		}
		return decode(data.getBytes(charset));
	}

	/**
	 * decodeToString Hex
	 *
	 * @param data Data to Hex
	 * @return bytes as a hex string
	 */
	public static String decodeToString(byte[] data) {
		byte[] decodeBytes = decode(data);
		return new String(decodeBytes, StandardCharsets.UTF_8);
	}

	/**
	 * decodeToString Hex
	 *
	 * @param data    Data to Hex
	 * @param charset Charset
	 * @return bytes as a hex string
	 */
	public static String decodeToString(byte[] data, Charset charset) {
		byte[] decodeBytes = decode(data);
		return new String(decodeBytes, charset);
	}

	/**
	 * decodeToString Hex
	 *
	 * @param data Data to Hex
	 * @return bytes as a hex string
	 */
	@Nullable
	public static String decodeToString(@Nullable String data) {
		if (StringUtil.isBlank(data)) {
			return null;
		}
		return decodeToString(data.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * decodeToString Hex
	 *
	 * @param data    Data to Hex
	 * @param charset Charset
	 * @return bytes as a hex string
	 */
	public static String decodeToString(String data, Charset charset) {
		return decodeToString(data, charset, charset);
	}

	/**
	 * decodeToString Hex
	 *
	 * @param data        Data to Hex
	 * @param dataCharset Charset
	 * @param toCharset   Charset
	 * @return bytes as a hex string
	 */
	public static String decodeToString(String data, Charset dataCharset, Charset toCharset) {
		if (StringUtil.isBlank(data)) {
			return null;
		}
		return decodeToString(data.getBytes(dataCharset), toCharset);
	}

	/**
	 * decode Hex
	 *
	 * @param data Hex data
	 * @return decode hex to bytes
	 */
	public static byte[] decode(byte[] data) {
		int len = data.length;
		if ((len & 0x01) != 0) {
			throw new IllegalArgumentException("hexBinary needs to be even-length: " + len);
		}
		byte[] out = new byte[len >> 1];
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f |= toDigit(data[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}
		return out;
	}

	private static int toDigit(byte b, int index) {
		int digit = Character.digit(b, 16);
		if (digit == -1) {
			throw new IllegalArgumentException("Illegal hexadecimal byte " + b + " at index " + index);
		}
		return digit;
	}

}
