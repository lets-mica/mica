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

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Base64工具
 *
 * @author L.cm
 */
public class Base64Util {
	public static final Base64.Encoder ENCODER = Base64.getEncoder();
	public static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder();
	public static final Base64.Decoder DECODER = Base64.getDecoder();
	public static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();

	/**
	 * 编码
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String encode(String value) {
		return encode(value, Charsets.UTF_8);
	}

	/**
	 * 编码
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String encode(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		return new String(encode(val), charset);
	}

	/**
	 * 编码URL安全
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String encodeUrlSafe(String value) {
		return encodeUrlSafe(value, Charsets.UTF_8);
	}

	/**
	 * 编码URL安全
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String encodeUrlSafe(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		return new String(encodeUrlSafe(val), charset);
	}

	/**
	 * 解码
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String decode(String value) {
		return decode(value, Charsets.UTF_8);
	}

	/**
	 * 解码
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String decode(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		byte[] decodedValue = decode(val);
		return new String(decodedValue, charset);
	}

	/**
	 * 解码URL安全
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String decodeUrlSafe(String value) {
		return decodeUrlSafe(value, Charsets.UTF_8);
	}

	/**
	 * 解码URL安全
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String decodeUrlSafe(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		byte[] decodedValue = decodeUrlSafe(val);
		return new String(decodedValue, charset);
	}

	/**
	 * Base64-encode the given byte array.
	 *
	 * @param src the original byte array
	 * @return the encoded byte array
	 */
	public static byte[] encode(byte[] src) {
		if (src.length == 0) {
			return src;
		}
		return ENCODER.encode(src);
	}

	/**
	 * Base64-decode the given byte array.
	 *
	 * @param src the encoded byte array
	 * @return the original byte array
	 */
	public static byte[] decode(byte[] src) {
		if (src.length == 0) {
			return src;
		}
		return DECODER.decode(src);
	}

	/**
	 * Base64-encode the given byte array using the RFC 4648
	 * "URL and Filename Safe Alphabet".
	 *
	 * @param src the original byte array
	 * @return the encoded byte array
	 */
	public static byte[] encodeUrlSafe(byte[] src) {
		if (src.length == 0) {
			return src;
		}
		return URL_ENCODER.encode(src);
	}

	/**
	 * Base64-decode the given byte array using the RFC 4648
	 * "URL and Filename Safe Alphabet".
	 *
	 * @param src the encoded byte array
	 * @return the original byte array
	 * @since 4.2.4
	 */
	public static byte[] decodeUrlSafe(byte[] src) {
		if (src.length == 0) {
			return src;
		}
		return URL_DECODER.decode(src);
	}

	/**
	 * Base64-encode the given byte array to a String.
	 *
	 * @param src the original byte array
	 * @return the encoded byte array as a UTF-8 String
	 */
	public static String encodeToString(byte[] src) {
		if (src.length == 0) {
			return "";
		}
		return new String(encode(src), Charsets.UTF_8);
	}

	/**
	 * Base64-decode the given byte array from an UTF-8 String.
	 *
	 * @param src the encoded UTF-8 String
	 * @return the original byte array
	 */
	public static byte[] decodeFromString(String src) {
		if (src.isEmpty()) {
			return new byte[0];
		}
		return decode(src.getBytes(Charsets.UTF_8));
	}

	/**
	 * Base64-encode the given byte array to a String using the RFC 4648
	 * "URL and Filename Safe Alphabet".
	 *
	 * @param src the original byte array
	 * @return the encoded byte array as a UTF-8 String
	 */
	public static String encodeToUrlSafeString(byte[] src) {
		return new String(encodeUrlSafe(src), Charsets.UTF_8);
	}

	/**
	 * Base64-decode the given byte array from an UTF-8 String using the RFC 4648
	 * "URL and Filename Safe Alphabet".
	 *
	 * @param src the encoded UTF-8 String
	 * @return the original byte array
	 */
	public static byte[] decodeFromUrlSafeString(String src) {
		return decodeUrlSafe(src.getBytes(Charsets.UTF_8));
	}

}
