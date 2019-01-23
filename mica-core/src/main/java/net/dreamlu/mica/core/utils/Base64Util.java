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

import java.nio.charset.Charset;

/**
 * Base64工具
 *
 * @author L.cm
 */
@UtilityClass
public class Base64Util extends org.springframework.util.Base64Utils {

	/**
	 * 编码
	 * @param value 字符串
	 * @return {String}
	 */
	public static String encode(String value) {
		return Base64Util.encode(value, Charsets.UTF_8);
	}

	/**
	 * 编码
	 * @param value 字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String encode(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		return new String(Base64Util.encode(val), charset);
	}

	/**
	 * 编码URL安全
	 * @param value 字符串
	 * @return {String}
	 */
	public static String encodeUrlSafe(String value) {
		return Base64Util.encodeUrlSafe(value, Charsets.UTF_8);
	}

	/**
	 * 编码URL安全
	 * @param value 字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String encodeUrlSafe(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		return new String(Base64Util.encodeUrlSafe(val), charset);
	}

	/**
	 * 解码
	 * @param value 字符串
	 * @return {String}
	 */
	public static String decode(String value) {
		return Base64Util.decode(value, Charsets.UTF_8);
	}

	/**
	 * 解码
	 * @param value 字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String decode(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		byte[] decodedValue = Base64Util.decode(val);
		return new String(decodedValue, charset);
	}

	/**
	 * 解码URL安全
	 * @param value 字符串
	 * @return {String}
	 */
	public static String decodeUrlSafe(String value) {
		return Base64Util.decodeUrlSafe(value, Charsets.UTF_8);
	}

	/**
	 * 解码URL安全
	 * @param value 字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String decodeUrlSafe(String value, Charset charset) {
		byte[] val = value.getBytes(charset);
		byte[] decodedValue = Base64Util.decodeUrlSafe(val);
		return new String(decodedValue, charset);
	}
}
