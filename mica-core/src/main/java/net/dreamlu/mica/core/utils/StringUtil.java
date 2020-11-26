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
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
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
	 * <p>The maximum size to which the padding constant(s) can expand.</p>
	 */
	private static final int PAD_LIMIT = 8192;

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
	 * 有 任意 一个 Blank
	 *
	 * @param css CharSequence
	 * @return boolean
	 */
	public static boolean isAnyBlank(Collection<CharSequence> css) {
		if (CollectionUtil.isEmpty(css)) {
			return true;
		}
		return css.stream().anyMatch(StringUtil::isBlank);
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
	 * 是否全非 Blank
	 *
	 * @param css CharSequence
	 * @return boolean
	 */
	public static boolean isNoneBlank(Collection<CharSequence> css) {
		if (CollectionUtil.isEmpty(css)) {
			return false;
		}
		return css.stream().allMatch(StringUtil::isNotBlank);
	}

	/**
	 * 有 任意 一个 Blank
	 *
	 * @param css CharSequence
	 * @return boolean
	 */
	public static boolean isAnyNotBlank(CharSequence... css) {
		if (ObjectUtil.isEmpty(css)) {
			return false;
		}
		return Stream.of(css).anyMatch(StringUtil::isNoneBlank);
	}

	/**
	 * 有 任意 一个 Blank
	 *
	 * @param css CharSequence
	 * @return boolean
	 */
	public static boolean isAnyNotBlank(Collection<CharSequence> css) {
		if (CollectionUtil.isEmpty(css)) {
			return false;
		}
		return css.stream().anyMatch(StringUtil::isNoneBlank);
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
	 * startWith char
	 *
	 * @param cs CharSequence
	 * @param c  char
	 * @return {boolean}
	 */
	public static boolean startWith(CharSequence cs, char c) {
		return cs.charAt(0) == c;
	}

	/**
	 * endWith char
	 *
	 * @param cs CharSequence
	 * @param c  char
	 * @return {boolean}
	 */
	public static boolean endWith(CharSequence cs, char c) {
		return cs.charAt(cs.length() - 1) == c;
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
	public static String format(@Nullable String message, @Nullable Map<String, ?> params) {
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
	 * 格式化执行时间，单位为 ms 和 s，保留三位小数
	 *
	 * @param nanos 纳秒
	 * @return 格式化后的时间
	 */
	public static String format(long nanos) {
		if (nanos < 1) {
			return "0ms";
		}
		double millis = (double) nanos / (1000 * 1000);
		// 不够 1 ms，最小单位为 ms
		if (millis > 1000) {
			return String.format("%.3fs", millis / 1000);
		} else {
			return String.format("%.3fms", millis);
		}
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
			return StringPool.EMPTY;
		}
		Assert.isTrue(count > 0, "Requested random string length " + count + " is less than 0.");
		final Random random = Holder.SECURE_RANDOM;
		char[] buffer = new char[count];
		for (int i = 0; i < count; i++) {
			String factor = randomType.getFactor();
			buffer[i] = factor.charAt(random.nextInt(factor.length()));
		}
		return new String(buffer);
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Returns padding using the specified delimiter repeated
	 * to a given length.</p>
	 *
	 * <pre>
	 * StringUtils.repeat('e', 0)  = ""
	 * StringUtils.repeat('e', 3)  = "eee"
	 * StringUtils.repeat('e', -2) = ""
	 * </pre>
	 *
	 * <p>Note: this method does not support padding with
	 * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary Characters</a>
	 * as they require a pair of {@code char}s to be represented.
	 * </p>
	 *
	 * @param ch     character to repeat
	 * @param repeat number of times to repeat char, negative treated as zero
	 * @return String with repeated character
	 */
	public static String repeat(final char ch, final int repeat) {
		if (repeat <= 0) {
			return StringPool.EMPTY;
		}
		final char[] buf = new char[repeat];
		Arrays.fill(buf, ch);
		return new String(buf);
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Gets the leftmost {@code len} characters of a String.</p>
	 *
	 * <p>If {@code len} characters are not available, or the
	 * String is {@code null}, the String will be returned without
	 * an exception. An empty String is returned if len is negative.</p>
	 *
	 * <pre>
	 * StringUtils.left(null, *)    = null
	 * StringUtils.left(*, -ve)     = ""
	 * StringUtils.left("", *)      = ""
	 * StringUtils.left("abc", 0)   = ""
	 * StringUtils.left("abc", 2)   = "ab"
	 * StringUtils.left("abc", 4)   = "abc"
	 * </pre>
	 *
	 * @param str the CharSequence to get the leftmost characters from, may be null
	 * @param len the length of the required String
	 * @return the leftmost characters, {@code null} if null String input
	 */
	@Nullable
	public static String left(@Nullable final String str, final int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return StringPool.EMPTY;
		}
		if (str.length() <= len) {
			return str;
		}
		return str.substring(0, len);
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Gets the rightmost {@code len} characters of a String.</p>
	 *
	 * <p>If {@code len} characters are not available, or the String
	 * is {@code null}, the String will be returned without an
	 * an exception. An empty String is returned if len is negative.</p>
	 *
	 * <pre>
	 * StringUtils.right(null, *)    = null
	 * StringUtils.right(*, -ve)     = ""
	 * StringUtils.right("", *)      = ""
	 * StringUtils.right("abc", 0)   = ""
	 * StringUtils.right("abc", 2)   = "bc"
	 * StringUtils.right("abc", 4)   = "abc"
	 * </pre>
	 *
	 * @param str the String to get the rightmost characters from, may be null
	 * @param len the length of the required String
	 * @return the rightmost characters, {@code null} if null String input
	 */
	@Nullable
	public static String right(@Nullable final String str, final int len) {
		if (str == null) {
			return null;
		}
		if (len < 0) {
			return StringPool.EMPTY;
		}
		int length = str.length();
		if (length <= len) {
			return str;
		}
		return str.substring(length - len);
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Right pad a String with spaces (' ').</p>
	 *
	 * <p>The String is padded to the size of {@code size}.</p>
	 *
	 * <pre>
	 * StringUtils.rightPad(null, *)   = null
	 * StringUtils.rightPad("", 3)     = "   "
	 * StringUtils.rightPad("bat", 3)  = "bat"
	 * StringUtils.rightPad("bat", 5)  = "bat  "
	 * StringUtils.rightPad("bat", 1)  = "bat"
	 * StringUtils.rightPad("bat", -1) = "bat"
	 * </pre>
	 *
	 * @param str  the String to pad out, may be null
	 * @param size the size to pad to
	 * @return right padded String or original String if no padding is necessary,
	 * {@code null} if null String input
	 */
	@Nullable
	public static String rightPad(@Nullable final String str, final int size) {
		return rightPad(str, size, CharPool.SPACE);
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Right pad a String with a specified character.</p>
	 *
	 * <p>The String is padded to the size of {@code size}.</p>
	 *
	 * <pre>
	 * StringUtils.rightPad(null, *, *)     = null
	 * StringUtils.rightPad("", 3, 'z')     = "zzz"
	 * StringUtils.rightPad("bat", 3, 'z')  = "bat"
	 * StringUtils.rightPad("bat", 5, 'z')  = "batzz"
	 * StringUtils.rightPad("bat", 1, 'z')  = "bat"
	 * StringUtils.rightPad("bat", -1, 'z') = "bat"
	 * </pre>
	 *
	 * @param str     the String to pad out, may be null
	 * @param size    the size to pad to
	 * @param padChar the character to pad with
	 * @return right padded String or original String if no padding is necessary,
	 * {@code null} if null String input
	 */
	@Nullable
	public static String rightPad(@Nullable final String str, final int size, final char padChar) {
		if (str == null) {
			return null;
		}
		final int pads = size - str.length();
		if (pads <= 0) {
			// returns original String when possible
			return str;
		}
		if (pads > PAD_LIMIT) {
			return rightPad(str, size, String.valueOf(padChar));
		}
		return str.concat(repeat(padChar, pads));
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Right pad a String with a specified String.</p>
	 *
	 * <p>The String is padded to the size of {@code size}.</p>
	 *
	 * <pre>
	 * StringUtils.rightPad(null, *, *)      = null
	 * StringUtils.rightPad("", 3, "z")      = "zzz"
	 * StringUtils.rightPad("bat", 3, "yz")  = "bat"
	 * StringUtils.rightPad("bat", 5, "yz")  = "batyz"
	 * StringUtils.rightPad("bat", 8, "yz")  = "batyzyzy"
	 * StringUtils.rightPad("bat", 1, "yz")  = "bat"
	 * StringUtils.rightPad("bat", -1, "yz") = "bat"
	 * StringUtils.rightPad("bat", 5, null)  = "bat  "
	 * StringUtils.rightPad("bat", 5, "")    = "bat  "
	 * </pre>
	 *
	 * @param str    the String to pad out, may be null
	 * @param size   the size to pad to
	 * @param padStr the String to pad with, null or empty treated as single space
	 * @return right padded String or original String if no padding is necessary,
	 * {@code null} if null String input
	 */
	@Nullable
	public static String rightPad(@Nullable final String str, final int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (StringUtils.isEmpty(padStr)) {
			padStr = StringPool.SPACE;
		}
		final int padLen = padStr.length();
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			// returns original String when possible
			return str;
		}
		if (padLen == 1 && pads <= PAD_LIMIT) {
			return rightPad(str, size, padStr.charAt(0));
		}
		if (pads == padLen) {
			return str.concat(padStr);
		} else if (pads < padLen) {
			return str.concat(padStr.substring(0, pads));
		} else {
			final char[] padding = new char[pads];
			final char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return str.concat(new String(padding));
		}
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Left pad a String with spaces (' ').</p>
	 *
	 * <p>The String is padded to the size of {@code size}.</p>
	 *
	 * <pre>
	 * StringUtils.leftPad(null, *)   = null
	 * StringUtils.leftPad("", 3)     = "   "
	 * StringUtils.leftPad("bat", 3)  = "bat"
	 * StringUtils.leftPad("bat", 5)  = "  bat"
	 * StringUtils.leftPad("bat", 1)  = "bat"
	 * StringUtils.leftPad("bat", -1) = "bat"
	 * </pre>
	 *
	 * @param str  the String to pad out, may be null
	 * @param size the size to pad to
	 * @return left padded String or original String if no padding is necessary,
	 * {@code null} if null String input
	 */
	@Nullable
	public static String leftPad(@Nullable final String str, final int size) {
		return leftPad(str, size, CharPool.SPACE);
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Left pad a String with a specified character.</p>
	 *
	 * <p>Pad to a size of {@code size}.</p>
	 *
	 * <pre>
	 * StringUtils.leftPad(null, *, *)     = null
	 * StringUtils.leftPad("", 3, 'z')     = "zzz"
	 * StringUtils.leftPad("bat", 3, 'z')  = "bat"
	 * StringUtils.leftPad("bat", 5, 'z')  = "zzbat"
	 * StringUtils.leftPad("bat", 1, 'z')  = "bat"
	 * StringUtils.leftPad("bat", -1, 'z') = "bat"
	 * </pre>
	 *
	 * @param str     the String to pad out, may be null
	 * @param size    the size to pad to
	 * @param padChar the character to pad with
	 * @return left padded String or original String if no padding is necessary,
	 * {@code null} if null String input
	 * @since 2.0
	 */
	@Nullable
	public static String leftPad(@Nullable final String str, final int size, final char padChar) {
		if (str == null) {
			return null;
		}
		final int pads = size - str.length();
		if (pads <= 0) {
			// returns original String when possible
			return str;
		}
		if (pads > PAD_LIMIT) {
			return leftPad(str, size, String.valueOf(padChar));
		}
		return repeat(padChar, pads).concat(str);
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Left pad a String with a specified String.</p>
	 *
	 * <p>Pad to a size of {@code size}.</p>
	 *
	 * <pre>
	 * StringUtils.leftPad(null, *, *)      = null
	 * StringUtils.leftPad("", 3, "z")      = "zzz"
	 * StringUtils.leftPad("bat", 3, "yz")  = "bat"
	 * StringUtils.leftPad("bat", 5, "yz")  = "yzbat"
	 * StringUtils.leftPad("bat", 8, "yz")  = "yzyzybat"
	 * StringUtils.leftPad("bat", 1, "yz")  = "bat"
	 * StringUtils.leftPad("bat", -1, "yz") = "bat"
	 * StringUtils.leftPad("bat", 5, null)  = "  bat"
	 * StringUtils.leftPad("bat", 5, "")    = "  bat"
	 * </pre>
	 *
	 * @param str    the String to pad out, may be null
	 * @param size   the size to pad to
	 * @param padStr the String to pad with, null or empty treated as single space
	 * @return left padded String or original String if no padding is necessary,
	 * {@code null} if null String input
	 */
	@Nullable
	public static String leftPad(@Nullable final String str, final int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (StringUtils.isEmpty(padStr)) {
			padStr = StringPool.SPACE;
		}
		final int padLen = padStr.length();
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			// returns original String when possible
			return str;
		}
		if (padLen == 1 && pads <= PAD_LIMIT) {
			return leftPad(str, size, padStr.charAt(0));
		}
		if (pads == padLen) {
			return padStr.concat(str);
		} else if (pads < padLen) {
			return padStr.substring(0, pads).concat(str);
		} else {
			final char[] padding = new char[pads];
			final char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return new String(padding).concat(str);
		}
	}

	/**
	 * 参考自 commons lang 微调
	 *
	 * <p>Gets {@code len} characters from the middle of a String.</p>
	 *
	 * <p>If {@code len} characters are not available, the remainder
	 * of the String will be returned without an exception. If the
	 * String is {@code null}, {@code null} will be returned.
	 * An empty String is returned if len is negative or exceeds the
	 * length of {@code str}.</p>
	 *
	 * <pre>
	 * StringUtils.mid(null, *, *)    = null
	 * StringUtils.mid(*, *, -ve)     = ""
	 * StringUtils.mid("", 0, *)      = ""
	 * StringUtils.mid("abc", 0, 2)   = "ab"
	 * StringUtils.mid("abc", 0, 4)   = "abc"
	 * StringUtils.mid("abc", 2, 4)   = "c"
	 * StringUtils.mid("abc", 4, 2)   = ""
	 * StringUtils.mid("abc", -2, 2)  = "ab"
	 * </pre>
	 *
	 * @param str the String to get the characters from, may be null
	 * @param pos the position to start from, negative treated as zero
	 * @param len the length of the required String
	 * @return the middle characters, {@code null} if null String input
	 */
	@Nullable
	public static String mid(@Nullable final String str, int pos, final int len) {
		if (str == null) {
			return null;
		}
		int length = str.length();
		if (len < 0 || pos > length) {
			return StringPool.EMPTY;
		}
		if (pos < 0) {
			pos = 0;
		}
		if (length <= pos + len) {
			return str.substring(pos);
		}
		return str.substring(pos, pos + len);
	}

}

