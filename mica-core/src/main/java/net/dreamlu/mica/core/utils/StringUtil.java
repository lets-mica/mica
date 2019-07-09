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
import org.springframework.util.Assert;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 继承自Spring util的工具类，减少jar依赖
 *
 * @author L.cm
 */
@UtilityClass
public class StringUtil extends org.springframework.util.StringUtils {
	/**
	 * 特殊字符正则，sql特殊字符和空白符
	 */
	private final static Pattern SPECIAL_CHARS_REGEX = Pattern.compile("[`'\"|/,;()-+*%#·•�　\\s]");
	/**
	 * 随机字符串因子
	 */
	private static final String INT_STR = "0123456789";
	private static final String STR_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final String ALL_STR = INT_STR + STR_STR;

	/**
	 * 首字母变小写
	 *
	 * @param str 字符串
	 * @return {String}
	 */
	public static String firstCharToLower(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= CharPool.UPPER_A && firstChar <= CharPool.UPPER_Z) {
			char[] arr = str.toCharArray();
			arr[0] += (CharPool.LOWER_A - CharPool.UPPER_A);
			return new String(arr);
		}
		return str;
	}

	/**
	 * 首字母变大写
	 *
	 * @param str 字符串
	 * @return {String}
	 */
	public static String firstCharToUpper(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= CharPool.LOWER_A && firstChar <= CharPool.LOWER_Z) {
			char[] arr = str.toCharArray();
			arr[0] -= (CharPool.LOWER_A - CharPool.UPPER_A);
			return new String(arr);
		}
		return str;
	}

	/**
	 * Check whether the given {@code CharSequence} contains actual <em>text</em>.
	 * <p>More specifically, this method returns {@code true} if the
	 * {@code CharSequence} is not {@code null}, its length is greater than
	 * 0, and it contains at least one non-whitespace character.
	 * <pre class="code">
	 * StringUtil.isBlank(null) = true
	 * StringUtil.isBlank("") = true
	 * StringUtil.isBlank(" ") = true
	 * StringUtil.isBlank("12345") = false
	 * StringUtil.isBlank(" 12345 ") = false
	 * </pre>
	 *
	 * @param cs the {@code CharSequence} to check (may be {@code null})
	 * @return {@code true} if the {@code CharSequence} is not {@code null},
	 * its length is greater than 0, and it does not contain whitespace only
	 * @see Character#isWhitespace
	 */
	public static boolean isBlank(@Nullable final CharSequence cs) {
		return !StringUtil.hasText(cs);
	}

	/**
	 * <p>Checks if a CharSequence is not empty (""), not null and not whitespace only.</p>
	 * <pre>
	 * StringUtil.isNotBlank(null)	  = false
	 * StringUtil.isNotBlank("")		= false
	 * StringUtil.isNotBlank(" ")	   = false
	 * StringUtil.isNotBlank("bob")	 = true
	 * StringUtil.isNotBlank("  bob  ") = true
	 * </pre>
	 *
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is
	 * not empty and not null and not whitespace
	 * @see Character#isWhitespace
	 */
	public static boolean isNotBlank(@Nullable final CharSequence cs) {
		return StringUtil.hasText(cs);
	}

	/**
	 * 有 任意 一个 Blank
	 *
	 * @param css CharSequence
	 * @return boolean
	 */
	public static boolean isAnyBlank(final CharSequence... css) {
		if (ObjectUtil.isEmpty(css)) {
			return true;
		}
		return Stream.of(css).anyMatch(StringUtil::isBlank);
	}

	/**
	 * 是否全非 Blank
	 *
	 * @param css CharSequence
	 * @return boolean
	 */
	public static boolean isNoneBlank(final CharSequence... css) {
		if (ObjectUtil.isEmpty(css)) {
			return false;
		}
		return Stream.of(css).allMatch(StringUtil::isNotBlank);
	}

	/**
	 * 判断一个字符串是否是数字
	 *
	 * @param cs the CharSequence to check, may be null
	 * @return {boolean}
	 */
	public static boolean isNumeric(final CharSequence cs) {
		if (StringUtil.isBlank(cs)) {
			return false;
		}
		for (int i = cs.length(); --i >= 0; ) {
			int chr = cs.charAt(i);
			if (chr < 48 || chr > 57) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 将字符串中特定模式的字符转换成map中对应的值
	 * <p>
	 * use: format("my name is ${name}, and i like ${like}!", {"name":"L.cm", "like": "Java"})
	 *
	 * @param message 需要转换的字符串
	 * @param params  转换所需的键值对集合
	 * @return 转换后的字符串
	 */
	public static String format(@Nullable String message, @Nullable Map<String, Object> params) {
		// message 为 null 返回空字符串
		if (message == null) {
			return StringPool.EMPTY;
		}
		// 参数为 null 或者为空
		if (params == null || params.isEmpty()) {
			return message;
		}
		// 替换变量
		StringBuilder sb = new StringBuilder((int) (message.length() * 1.5));
		int cursor = 0;
		for (int start, end; (start = message.indexOf(StringPool.DOLLAR_LEFT_BRACE, cursor)) != -1 && (end = message.indexOf(CharPool.RIGHT_BRACE, start)) != -1; ) {
			sb.append(message, cursor, start);
			String key = message.substring(start + 2, end);
			Object value = params.get(StringUtil.trimWhitespace(key));
			sb.append(value == null ? StringPool.EMPTY : value);
			cursor = end + 1;
		}
		sb.append(message.substring(cursor));
		return sb.toString();
	}

	/**
	 * 同 log 格式的 format 规则
	 * <p>
	 * use: format("my name is {}, and i like {}!", "L.cm", "Java")
	 *
	 * @param message   需要转换的字符串
	 * @param arguments 需要替换的变量
	 * @return 转换后的字符串
	 */
	public static String format(@Nullable String message, @Nullable Object... arguments) {
		// message 为 null 返回空字符串
		if (message == null) {
			return StringPool.EMPTY;
		}
		// 参数为 null 或者为空
		if (arguments == null || arguments.length == 0) {
			return message;
		}
		StringBuilder sb = new StringBuilder((int) (message.length() * 1.5));
		int cursor = 0;
		int index = 0;
		int argsLength = arguments.length;
		for (int start, end; (start = message.indexOf(CharPool.LEFT_BRACE, cursor)) != -1 && (end = message.indexOf(CharPool.RIGHT_BRACE, start)) != -1 && index < argsLength; ) {
			sb.append(message, cursor, start);
			sb.append(arguments[index]);
			cursor = end + 1;
			index++;
		}
		sb.append(message.substring(cursor));
		return sb.toString();
	}

	/**
	 * Convert a {@code Collection} into a delimited {@code String} (e.g., CSV).
	 * <p>Useful for {@code toString()} implementations.
	 *
	 * @param coll the {@code Collection} to convert
	 * @return the delimited {@code String}
	 */
	public static String join(Collection<?> coll) {
		return StringUtil.collectionToCommaDelimitedString(coll);
	}

	/**
	 * Convert a {@code Collection} into a delimited {@code String} (e.g. CSV).
	 * <p>Useful for {@code toString()} implementations.
	 *
	 * @param coll  the {@code Collection} to convert
	 * @param delim the delimiter to use (typically a ",")
	 * @return the delimited {@code String}
	 */
	public static String join(Collection<?> coll, String delim) {
		return StringUtil.collectionToDelimitedString(coll, delim);
	}

	/**
	 * Convert a {@code String} array into a comma delimited {@code String}
	 * (i.e., CSV).
	 * <p>Useful for {@code toString()} implementations.
	 *
	 * @param arr the array to display
	 * @return the delimited {@code String}
	 */
	public static String join(Object[] arr) {
		return StringUtil.arrayToCommaDelimitedString(arr);
	}

	/**
	 * Convert a {@code String} array into a delimited {@code String} (e.g. CSV).
	 * <p>Useful for {@code toString()} implementations.
	 *
	 * @param arr   the array to display
	 * @param delim the delimiter to use (typically a ",")
	 * @return the delimited {@code String}
	 */
	public static String join(Object[] arr, String delim) {
		return StringUtil.arrayToDelimitedString(arr, delim);
	}

	/**
	 * 分割 字符串
	 *
	 * @param str       字符串
	 * @param delimiter 分割符
	 * @return 字符串数组
	 */
	public static String[] split(@Nullable String str, @Nullable String delimiter) {
		return StringUtil.delimitedListToStringArray(str, delimiter);
	}

	/**
	 * 分割 字符串 删除常见 空白符
	 *
	 * @param str       字符串
	 * @param delimiter 分割符
	 * @return 字符串数组
	 */
	public static String[] splitTrim(@Nullable String str, @Nullable String delimiter) {
		return StringUtil.delimitedListToStringArray(str, delimiter, " \t\n\n\f");
	}

	/**
	 * 字符串是否符合指定的 表达式
	 *
	 * <p>
	 * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy"
	 * </p>
	 *
	 * @param pattern 表达式
	 * @param str     字符串
	 * @return 是否匹配
	 */
	public static boolean simpleMatch(@Nullable String pattern, @Nullable String str) {
		return PatternMatchUtils.simpleMatch(pattern, str);
	}

	/**
	 * 字符串是否符合指定的 表达式
	 *
	 * <p>
	 * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy"
	 * </p>
	 *
	 * @param patterns 表达式 数组
	 * @param str      字符串
	 * @return 是否匹配
	 */
	public static boolean simpleMatch(@Nullable String[] patterns, String str) {
		return PatternMatchUtils.simpleMatch(patterns, str);
	}

	/**
	 * 生成uuid，采用 jdk 9 的形式，优化性能
	 *
	 * @return UUID
	 */
	public static String getUUID() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		long lsb = random.nextLong();
		long msb = random.nextLong();
		byte[] buf = new byte[32];
		formatUnsignedLong(lsb, buf, 20, 12);
		formatUnsignedLong(lsb >>> 48, buf, 16, 4);
		formatUnsignedLong(msb, buf, 12, 4);
		formatUnsignedLong(msb >>> 16, buf, 8, 4);
		formatUnsignedLong(msb >>> 32, buf, 0, 8);
		return new String(buf, Charsets.UTF_8);
	}

	private static void formatUnsignedLong(long val, byte[] buf, int offset, int len) {
		int charPos = offset + len;
		int radix = 1 << 4;
		int mask = radix - 1;
		do {
			buf[--charPos] = NumberUtil.DIGITS[((int) val) & mask];
			val >>>= 4;
		} while (charPos > offset);
	}

	/**
	 * 转义HTML用于安全过滤
	 *
	 * @param html html
	 * @return {String}
	 */
	public static String escapeHtml(String html) {
		return HtmlUtils.htmlEscape(html);
	}

	/**
	 * 清理字符串，清理出某些不可见字符和一些sql特殊字符
	 *
	 * @param txt 文本
	 * @return {String}
	 */
	@Nullable
	public static String cleanText(@Nullable String txt) {
		if (txt == null) {
			return null;
		}
		return SPECIAL_CHARS_REGEX.matcher(txt).replaceAll(StringPool.EMPTY);
	}

	/**
	 * 获取标识符，用于参数清理
	 *
	 * @param param 参数
	 * @return 清理后的标识符
	 */
	@Nullable
	public static String cleanIdentifier(@Nullable String param) {
		if (param == null) {
			return null;
		}
		StringBuilder paramBuilder = new StringBuilder();
		for (int i = 0; i < param.length(); i++) {
			char c = param.charAt(i);
			if (Character.isJavaIdentifierPart(c)) {
				paramBuilder.append(c);
			}
		}
		return paramBuilder.toString();
	}

	/**
	 * 随机数生成
	 *
	 * @param count 字符长度
	 * @return 随机数
	 */
	public static String random(int count) {
		return StringUtil.random(count, RandomType.ALL);
	}

	/**
	 * 随机数生成
	 *
	 * @param count      字符长度
	 * @param randomType 随机数类别
	 * @return 随机数
	 */
	public static String random(int count, RandomType randomType) {
		if (count == 0) {
			return "";
		}
		Assert.isTrue(count > 0, "Requested random string length " + count + " is less than 0.");
		final ThreadLocalRandom random = ThreadLocalRandom.current();
		char[] buffer = new char[count];
		for (int i = 0; i < count; i++) {
			if (RandomType.INT == randomType) {
				buffer[i] = INT_STR.charAt(random.nextInt(INT_STR.length()));
			} else if (RandomType.STRING == randomType) {
				buffer[i] = STR_STR.charAt(random.nextInt(STR_STR.length()));
			} else {
				buffer[i] = ALL_STR.charAt(random.nextInt(ALL_STR.length()));
			}
		}
		return new String(buffer);
	}
}

