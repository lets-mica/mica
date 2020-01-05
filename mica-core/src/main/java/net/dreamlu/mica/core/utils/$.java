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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.experimental.UtilityClass;
import org.springframework.beans.BeansException;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.function.Supplier;

/**
 * 工具包集合，工具类快捷方式
 *
 * @author L.cm
 */
@UtilityClass
public class $ {

	/**
	 * 断言，必须不能为 null
	 * <blockquote><pre>
	 * public Foo(Bar bar) {
	 *     this.bar = $.requireNotNull(bar);
	 * }
	 * </pre></blockquote>
	 *
	 * @param obj the object reference to check for nullity
	 * @param <T> the type of the reference
	 * @return {@code obj} if not {@code null}
	 * @throws NullPointerException if {@code obj} is {@code null}
	 */
	public static <T> T requireNotNull(T obj) {
		return Objects.requireNonNull(obj);
	}

	/**
	 * 断言，必须不能为 null
	 * <blockquote><pre>
	 * public Foo(Bar bar, Baz baz) {
	 *     this.bar = $.requireNotNull(bar, "bar must not be null");
	 *     this.baz = $.requireNotNull(baz, "baz must not be null");
	 * }
	 * </pre></blockquote>
	 *
	 * @param obj     the object reference to check for nullity
	 * @param message detail message to be used in the event that a {@code
	 *                NullPointerException} is thrown
	 * @param <T>     the type of the reference
	 * @return {@code obj} if not {@code null}
	 * @throws NullPointerException if {@code obj} is {@code null}
	 */
	public static <T> T requireNotNull(T obj, String message) {
		return Objects.requireNonNull(obj, message);
	}

	/**
	 * 断言，必须不能为 null
	 * <blockquote><pre>
	 * public Foo(Bar bar, Baz baz) {
	 *     this.bar = $.requireNotNull(bar, () -> "bar must not be null");
	 * }
	 * </pre></blockquote>
	 *
	 * @param obj             the object reference to check for nullity
	 * @param messageSupplier supplier of the detail message to be
	 *                        used in the event that a {@code NullPointerException} is thrown
	 * @param <T>             the type of the reference
	 * @return {@code obj} if not {@code null}
	 * @throws NullPointerException if {@code obj} is {@code null}
	 */
	public static <T> T requireNotNull(T obj, Supplier<String> messageSupplier) {
		return Objects.requireNonNull(obj, messageSupplier);
	}

	/**
	 * 判断对象为true
	 *
	 * @param object 对象
	 * @return 对象是否为true
	 */
	public static boolean isTrue(@Nullable Boolean object) {
		return ObjectUtil.isTrue(object);
	}

	/**
	 * 判断对象为false
	 *
	 * @param object 对象
	 * @return 对象是否为false
	 */
	public static boolean isFalse(@Nullable Boolean object) {
		return ObjectUtil.isFalse(object);
	}

	/**
	 * 判断对象是否为null
	 * <p>
	 * This method exists to be used as a
	 * {@link java.util.function.Predicate}, {@code context($::isNull)}
	 * </p>
	 *
	 * @param obj a reference to be checked against {@code null}
	 * @return {@code true} if the provided reference is {@code null} otherwise
	 * {@code false}
	 * @see java.util.function.Predicate
	 */
	public static boolean isNull(@Nullable Object obj) {
		return Objects.isNull(obj);
	}

	/**
	 * 判断对象是否 not null
	 * <p>
	 * This method exists to be used as a
	 * {@link java.util.function.Predicate}, {@code context($::notNull)}
	 * </p>
	 *
	 * @param obj a reference to be checked against {@code null}
	 * @return {@code true} if the provided reference is non-{@code null}
	 * otherwise {@code false}
	 * @see java.util.function.Predicate
	 */
	public static boolean isNotNull(@Nullable Object obj) {
		return Objects.nonNull(obj);
	}

	/**
	 * 首字母变小写
	 *
	 * @param str 字符串
	 * @return {String}
	 */
	public static String firstCharToLower(String str) {
		return StringUtil.firstCharToLower(str);
	}

	/**
	 * 首字母变大写
	 *
	 * @param str 字符串
	 * @return {String}
	 */
	public static String firstCharToUpper(String str) {
		return StringUtil.firstCharToUpper(str);
	}

	/**
	 * 判断是否为空字符串
	 * <pre class="code">
	 * $.isBlank(null)		= true
	 * $.isBlank("")		= true
	 * $.isBlank(" ")		= true
	 * $.isBlank("12345")	= false
	 * $.isBlank(" 12345 ")	= false
	 * </pre>
	 *
	 * @param cs the {@code CharSequence} to check (may be {@code null})
	 * @return {@code true} if the {@code CharSequence} is not {@code null},
	 * its length is greater than 0, and it does not contain whitespace only
	 * @see Character#isWhitespace
	 */
	public static boolean isBlank(@Nullable final CharSequence cs) {
		return StringUtil.isBlank(cs);
	}

	/**
	 * 判断不为空字符串
	 * <pre>
	 * $.isNotBlank(null)	= false
	 * $.isNotBlank("")		= false
	 * $.isNotBlank(" ")	= false
	 * $.isNotBlank("bob")	= true
	 * $.isNotBlank("  bob  ") = true
	 * </pre>
	 *
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is
	 * not empty and not null and not whitespace
	 * @see Character#isWhitespace
	 */
	public static boolean isNotBlank(@Nullable final CharSequence cs) {
		return StringUtil.isNotBlank(cs);
	}

	/**
	 * 判断是否有任意一个 空字符串
	 *
	 * @param css CharSequence
	 * @return boolean
	 */
	public static boolean isAnyBlank(final CharSequence... css) {
		return StringUtil.isAnyBlank(css);
	}

	/**
	 * 判断是否全为非空字符串
	 *
	 * @param css CharSequence
	 * @return boolean
	 */
	public static boolean isNoneBlank(final CharSequence... css) {
		return StringUtil.isNoneBlank(css);
	}

	/**
	 * 判断对象是数组
	 *
	 * @param obj the object to check
	 * @return 是否数组
	 */
	public static boolean isArray(@Nullable Object obj) {
		return ObjectUtil.isArray(obj);
	}

	/**
	 * 判断空对象 object、map、list、set、字符串、数组
	 *
	 * @param obj the object to check
	 * @return 数组是否为空
	 */
	public static boolean isEmpty(@Nullable Object obj) {
		return ObjectUtil.isEmpty(obj);
	}

	/**
	 * 对象不为空 object、map、list、set、字符串、数组
	 *
	 * @param obj the object to check
	 * @return 是否不为空
	 */
	public static boolean isNotEmpty(@Nullable Object obj) {
		return !ObjectUtil.isEmpty(obj);
	}

	/**
	 * 判断数组为空
	 *
	 * @param array the array to check
	 * @return 数组是否为空
	 */
	public static boolean isEmpty(@Nullable Object[] array) {
		return ObjectUtil.isEmpty(array);
	}

	/**
	 * 判断数组不为空
	 *
	 * @param array 数组
	 * @return 数组是否不为空
	 */
	public static boolean isNotEmpty(@Nullable Object[] array) {
		return ObjectUtil.isNotEmpty(array);
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
		return StringUtil.format(message, params);
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
		return StringUtil.format(message, arguments);
	}

	/**
	 * 清理字符串，清理出某些不可见字符和一些sql特殊字符
	 *
	 * @param txt 文本
	 * @return {String}
	 */
	@Nullable
	public static String cleanText(@Nullable String txt) {
		return StringUtil.cleanText(txt);
	}

	/**
	 * 获取标识符，用于参数清理
	 *
	 * @param param 参数
	 * @return 清理后的标识符
	 */
	@Nullable
	public static String cleanIdentifier(@Nullable String param) {
		return StringUtil.cleanIdentifier(param);
	}

	/**
	 * 安全的 equals
	 *
	 * @param o1 first Object to compare
	 * @param o2 second Object to compare
	 * @return whether the given objects are equal
	 * @see Object#equals(Object)
	 * @see java.util.Arrays#equals
	 */
	public static boolean equalsSafe(@Nullable Object o1, @Nullable Object o2) {
		return ObjectUtil.nullSafeEquals(o1, o2);
	}

	/**
	 * 对象 eq
	 *
	 * @param o1 Object
	 * @param o2 Object
	 * @return 是否eq
	 */
	public static boolean equals(@Nullable Object o1, @Nullable Object o2) {
		return Objects.equals(o1, o2);
	}

	/**
	 * 比较两个对象是否不相等。<br>
	 *
	 * @param o1 对象1
	 * @param o2 对象2
	 * @return 是否不eq
	 */
	public static boolean isNotEqual(Object o1, Object o2) {
		return !Objects.equals(o1, o2);
	}

	/**
	 * 返回对象的 hashCode
	 *
	 * @param obj Object
	 * @return hashCode
	 */
	public static int hashCode(@Nullable Object obj) {
		return Objects.hashCode(obj);
	}

	/**
	 * 如果对象为null，返回默认值
	 *
	 * @param object       Object
	 * @param defaultValue 默认值
	 * @return Object
	 */
	public static Object defaultIfNull(@Nullable Object object, Object defaultValue) {
		return object != null ? object : defaultValue;
	}

	/**
	 * 判断数组中是否包含元素
	 *
	 * @param array   the Array to check
	 * @param element the element to look for
	 * @param <T>     The generic tag
	 * @return {@code true} if found, {@code false} else
	 */
	public static <T> boolean contains(@Nullable T[] array, final T element) {
		return CollectionUtil.contains(array, element);
	}

	/**
	 * 判断迭代器中是否包含元素
	 *
	 * @param iterator the Iterator to check
	 * @param element  the element to look for
	 * @return {@code true} if found, {@code false} otherwise
	 */
	public static boolean contains(@Nullable Iterator<?> iterator, Object element) {
		return CollectionUtil.contains(iterator, element);
	}

	/**
	 * 判断枚举是否包含该元素
	 *
	 * @param enumeration the Enumeration to check
	 * @param element     the element to look for
	 * @return {@code true} if found, {@code false} otherwise
	 */
	public static boolean contains(@Nullable Enumeration<?> enumeration, Object element) {
		return CollectionUtil.contains(enumeration, element);
	}

	/**
	 * Concatenates 2 arrays
	 *
	 * @param one   数组1
	 * @param other 数组2
	 * @return 新数组
	 */
	public static String[] concat(String[] one, String[] other) {
		return CollectionUtil.concat(one, other, String.class);
	}

	/**
	 * Concatenates 2 arrays
	 *
	 * @param one   数组1
	 * @param other 数组2
	 * @param clazz 数组类
	 * @return 新数组
	 */
	public static <T> T[] concat(T[] one, T[] other, Class<T> clazz) {
		return CollectionUtil.concat(one, other, clazz);
	}

	/**
	 * 不可变 Set
	 *
	 * @param es  对象
	 * @param <E> 泛型
	 * @return 集合
	 */
	@SafeVarargs
	public static <E> Set<E> ofImmutableSet(E... es) {
		return CollectionUtil.ofImmutableSet(es);
	}

	/**
	 * 不可变 List
	 *
	 * @param es  对象
	 * @param <E> 泛型
	 * @return 集合
	 */
	@SafeVarargs
	public static <E> List<E> ofImmutableList(E... es) {
		return CollectionUtil.ofImmutableList(es);
	}

	/**
	 * 判断一个字符串是否是数字
	 *
	 * @param cs the CharSequence to check, may be null
	 * @return {boolean}
	 */
	public static boolean isNumeric(final CharSequence cs) {
		return StringUtil.isNumeric(cs);
	}

	/**
	 * 强转string
	 *
	 * @param object Object
	 * @return String
	 */
	@Nullable
	public static String toStr(@Nullable Object object) {
		return ObjectUtil.toStr(object, null);
	}

	/**
	 * 强转string
	 *
	 * @param object       Object
	 * @param defaultValue 默认值
	 * @return String
	 */
	@Nullable
	public static String toStr(@Nullable Object object, @Nullable String defaultValue) {
		return ObjectUtil.toStr(object, defaultValue);
	}

	/**
	 * 对象转为 int （支持 String 和 Number），默认: 0
	 *
	 * @param object Object
	 * @return int
	 */
	public static int toInt(@Nullable Object object) {
		return ObjectUtil.toInt(object, 0);
	}

	/**
	 * 对象转为 int （支持 String 和 Number）
	 *
	 * @param object       Object
	 * @param defaultValue 默认值
	 * @return int
	 */
	public static int toInt(@Nullable Object object, int defaultValue) {
		return ObjectUtil.toInt(object, defaultValue);
	}

	/**
	 * 对象转为 long （支持 String 和 Number），默认: 0L
	 *
	 * @param object Object
	 * @return long
	 */
	public static long toLong(@Nullable Object object) {
		return ObjectUtil.toLong(object, 0L);
	}

	/**
	 * 对象转为 long （支持 String 和 Number），默认: 0L
	 *
	 * @param object Object
	 * @return long
	 */
	public static long toLong(@Nullable Object object, long defaultValue) {
		return ObjectUtil.toLong(object, defaultValue);
	}

	/**
	 * 对象转为 Float
	 *
	 * @param object Object
	 * @return 结果
	 */
	public static float toFloat(@Nullable Object object) {
		return ObjectUtil.toFloat(object, 0.0f);
	}

	/**
	 * 对象转为 Float
	 *
	 * @param object       Object
	 * @param defaultValue float
	 * @return 结果
	 */
	public static float toFloat(@Nullable Object object, float defaultValue) {
		return ObjectUtil.toFloat(object, defaultValue);
	}

	/**
	 * 对象转为 Double
	 *
	 * @param object Object
	 * @return 结果
	 */
	public static double toDouble(@Nullable Object object) {
		return ObjectUtil.toDouble(object, 0.0d);
	}

	/**
	 * 对象转为 Double
	 *
	 * @param object       Object
	 * @param defaultValue double
	 * @return 结果
	 */
	public static double toDouble(@Nullable Object object, double defaultValue) {
		return ObjectUtil.toDouble(object, defaultValue);
	}

	/**
	 * 对象转为 Byte
	 *
	 * @param object Object
	 * @return 结果
	 */
	public static byte toByte(@Nullable Object object) {
		return ObjectUtil.toByte(object, (byte) 0);
	}

	/**
	 * 对象转为 Byte
	 *
	 * @param object       Object
	 * @param defaultValue byte
	 * @return 结果
	 */
	public static byte toByte(@Nullable Object object, byte defaultValue) {
		return ObjectUtil.toByte(object, defaultValue);
	}

	/**
	 * 对象转为 Short
	 *
	 * @param object Object
	 * @return 结果
	 */
	public static short toShort(@Nullable Object object) {
		return ObjectUtil.toShort(object, (short) 0);
	}

	/**
	 * 对象转为 Short
	 *
	 * @param object       Object
	 * @param defaultValue short
	 * @return 结果
	 */
	public static short toShort(@Nullable Object object, short defaultValue) {
		return ObjectUtil.toShort(object, defaultValue);
	}

	/**
	 * 对象转为 Boolean
	 *
	 * @param object Object
	 * @return 结果
	 */
	@Nullable
	public static Boolean toBoolean(@Nullable Object object) {
		return ObjectUtil.toBoolean(object, null);
	}

	/**
	 * 对象转为 Boolean
	 *
	 * @param object       Object
	 * @param defaultValue 默认值
	 * @return 结果
	 */
	@Nullable
	public static Boolean toBoolean(@Nullable Object object, @Nullable Boolean defaultValue) {
		return ObjectUtil.toBoolean(object, defaultValue);
	}

	/**
	 * 将 long 转短字符串 为 62 进制
	 *
	 * @param num 数字
	 * @return 短字符串
	 */
	public static String to62Str(long num) {
		return NumberUtil.to62Str(num);
	}

	/**
	 * 将集合拼接成字符串，默认使用`,`拼接
	 *
	 * @param coll the {@code Collection} to convert
	 * @return the delimited {@code String}
	 */
	public static String join(Collection<?> coll) {
		return StringUtil.join(coll);
	}

	/**
	 * 将集合拼接成字符串，默认指定分隔符
	 *
	 * @param coll  the {@code Collection} to convert
	 * @param delim the delimiter to use (typically a ",")
	 * @return the delimited {@code String}
	 */
	public static String join(Collection<?> coll, String delim) {
		return StringUtil.join(coll, delim);
	}

	/**
	 * 将数组拼接成字符串，默认使用`,`拼接
	 *
	 * @param arr the array to display
	 * @return the delimited {@code String}
	 */
	public static String join(Object[] arr) {
		return StringUtil.join(arr);
	}

	/**
	 * 将数组拼接成字符串，默认指定分隔符
	 *
	 * @param arr   the array to display
	 * @param delim the delimiter to use (typically a ",")
	 * @return the delimited {@code String}
	 */
	public static String join(Object[] arr, String delim) {
		return StringUtil.join(arr, delim);
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
		return StringUtil.splitTrim(str, delimiter);
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
	 * 生成uuid
	 *
	 * @return UUID
	 */
	public static String getUUID() {
		return StringUtil.getUUID();
	}

	/**
	 * 转义HTML用于安全过滤
	 *
	 * @param html html
	 * @return {String}
	 */
	public static String escapeHtml(String html) {
		return StringUtil.escapeHtml(html);
	}

	/**
	 * 随机数生成
	 *
	 * @param count 字符长度
	 * @return 随机数
	 */
	public static String random(int count) {
		return StringUtil.random(count);
	}

	/**
	 * 随机数生成
	 *
	 * @param count      字符长度
	 * @param randomType 随机数类别
	 * @return 随机数
	 */
	public static String random(int count, RandomType randomType) {
		return StringUtil.random(count, randomType);
	}

	/**
	 * Calculates the MD5 digest.
	 *
	 * @param data Data to digest
	 * @return MD5 digest as a hex array
	 */
	public static byte[] md5(final byte[] data) {
		return DigestUtil.md5(data);
	}

	/**
	 * Calculates the MD5 digest.
	 *
	 * @param data Data to digest
	 * @return MD5 digest as a hex array
	 */
	public static byte[] md5(final String data) {
		return DigestUtil.md5(data);
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 32 character hex string.
	 *
	 * @param data Data to digest
	 * @return MD5 digest as a hex string
	 */
	public static String md5Hex(final String data) {
		return DigestUtil.md5Hex(data);
	}

	/**
	 * Return a hexadecimal string representation of the MD5 digest of the given bytes.
	 *
	 * @param bytes the bytes to calculate the digest over
	 * @return a hexadecimal digest string
	 */
	public static String md5Hex(final byte[] bytes) {
		return DigestUtil.md5Hex(bytes);
	}

	/**
	 * sha1
	 *
	 * @param data Data to digest
	 * @return digest as a hex array
	 */
	public static byte[] sha1(String data) {
		return DigestUtil.sha1(data);
	}

	/**
	 * sha1
	 *
	 * @param bytes Data to digest
	 * @return digest as a hex array
	 */
	public static byte[] sha1(final byte[] bytes) {
		return DigestUtil.sha1(bytes);
	}

	/**
	 * sha1Hex
	 *
	 * @param data Data to digest
	 * @return digest as a hex string
	 */
	public static String sha1Hex(String data) {
		return DigestUtil.sha1Hex(data);
	}

	/**
	 * sha1Hex
	 *
	 * @param bytes Data to digest
	 * @return digest as a hex string
	 */
	public static String sha1Hex(final byte[] bytes) {
		return DigestUtil.sha1Hex(bytes);
	}

	/**
	 * SHA224
	 *
	 * @param data Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha224(String data) {
		return DigestUtil.sha224(data);
	}

	/**
	 * SHA224
	 *
	 * @param bytes Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha224(final byte[] bytes) {
		return DigestUtil.sha224(bytes);
	}

	/**
	 * SHA224Hex
	 *
	 * @param data Data to digest
	 * @return digest as a hex string
	 */
	public static String sha224Hex(String data) {
		return DigestUtil.sha224Hex(data.getBytes(Charsets.UTF_8));
	}

	/**
	 * SHA224Hex
	 *
	 * @param bytes Data to digest
	 * @return digest as a hex string
	 */
	public static String sha224Hex(final byte[] bytes) {
		return DigestUtil.sha224Hex(bytes);
	}

	/**
	 * sha256Hex
	 *
	 * @param data Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha256(String data) {
		return DigestUtil.sha256(data);
	}

	/**
	 * sha256Hex
	 *
	 * @param bytes Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha256(final byte[] bytes) {
		return DigestUtil.sha256(bytes);
	}

	/**
	 * sha256Hex
	 *
	 * @param data Data to digest
	 * @return digest as a hex string
	 */
	public static String sha256Hex(String data) {
		return DigestUtil.sha256Hex(data);
	}

	/**
	 * sha256Hex
	 *
	 * @param bytes Data to digest
	 * @return digest as a hex string
	 */
	public static String sha256Hex(final byte[] bytes) {
		return DigestUtil.sha256Hex(bytes);
	}

	/**
	 * sha384
	 *
	 * @param data Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha384(String data) {
		return DigestUtil.sha384(data);
	}

	/**
	 * sha384
	 *
	 * @param bytes Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha384(final byte[] bytes) {
		return DigestUtil.sha384(bytes);
	}

	/**
	 * sha384Hex
	 *
	 * @param data Data to digest
	 * @return digest as a hex string
	 */
	public static String sha384Hex(String data) {
		return DigestUtil.sha384Hex(data);
	}

	/**
	 * sha384Hex
	 *
	 * @param bytes Data to digest
	 * @return digest as a hex string
	 */
	public static String sha384Hex(final byte[] bytes) {
		return DigestUtil.sha384Hex(bytes);
	}

	/**
	 * sha512Hex
	 *
	 * @param data Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha512(String data) {
		return DigestUtil.sha512(data);
	}

	/**
	 * sha512Hex
	 *
	 * @param bytes Data to digest
	 * @return digest as a byte array
	 */
	public static byte[] sha512(final byte[] bytes) {
		return DigestUtil.sha512(bytes);
	}

	/**
	 * sha512Hex
	 *
	 * @param data Data to digest
	 * @return digest as a hex string
	 */
	public static String sha512Hex(String data) {
		return DigestUtil.sha512Hex(data);
	}

	/**
	 * sha512Hex
	 *
	 * @param bytes Data to digest
	 * @return digest as a hex string
	 */
	public static String sha512Hex(final byte[] bytes) {
		return DigestUtil.sha512Hex(bytes);
	}

	/**
	 * hmacMd5
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a byte array
	 */
	public static byte[] hmacMd5(String data, String key) {
		return DigestUtil.hmacMd5(data, key);
	}

	/**
	 * hmacMd5
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a byte array
	 */
	public static byte[] hmacMd5(final byte[] bytes, String key) {
		return DigestUtil.hmacMd5(bytes, key);
	}

	/**
	 * hmacMd5 Hex
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a hex string
	 */
	public static String hmacMd5Hex(String data, String key) {
		return DigestUtil.hmacMd5Hex(data, key);
	}

	/**
	 * hmacMd5 Hex
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a hex string
	 */
	public static String hmacMd5Hex(final byte[] bytes, String key) {
		return DigestUtil.hmacMd5Hex(bytes, key);
	}

	/**
	 * hmacSha1
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a byte array
	 */
	public static byte[] hmacSha1(String data, String key) {
		return DigestUtil.hmacSha1(data, key);
	}

	/**
	 * hmacSha1
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a byte array
	 */
	public static byte[] hmacSha1(final byte[] bytes, String key) {
		return DigestUtil.hmacSha1(bytes, key);
	}

	/**
	 * hmacSha1 Hex
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a hex string
	 */
	public static String hmacSha1Hex(String data, String key) {
		return DigestUtil.hmacSha1Hex(data, key);
	}

	/**
	 * hmacSha1 Hex
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a hex string
	 */
	public static String hmacSha1Hex(final byte[] bytes, String key) {
		return DigestUtil.hmacSha1Hex(bytes, key);
	}

	/**
	 * hmacSha224
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a hex string
	 */
	public static byte[] hmacSha224(String data, String key) {
		return DigestUtil.hmacSha224(data, key);
	}

	/**
	 * hmacSha224
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a hex string
	 */
	public static byte[] hmacSha224(final byte[] bytes, String key) {
		return DigestUtil.hmacSha224(bytes, key);
	}

	/**
	 * hmacSha224 Hex
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a hex string
	 */
	public static String hmacSha224Hex(String data, String key) {
		return DigestUtil.hmacSha224Hex(data, key);
	}

	/**
	 * hmacSha224 Hex
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a hex string
	 */
	public static String hmacSha224Hex(final byte[] bytes, String key) {
		return DigestUtil.hmacSha224Hex(bytes, key);
	}

	/**
	 * hmacSha256
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a hex string
	 */
	public static byte[] hmacSha256(String data, String key) {
		return DigestUtil.hmacSha256(data, key);
	}

	/**
	 * hmacSha256
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a byte array
	 */
	public static byte[] hmacSha256(final byte[] bytes, String key) {
		return DigestUtil.hmacSha256(bytes, key);
	}

	/**
	 * hmacSha256 Hex
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a byte array
	 */
	public static String hmacSha256Hex(String data, String key) {
		return DigestUtil.hmacSha256Hex(data, key);
	}

	/**
	 * hmacSha256 Hex
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a hex string
	 */
	public static String hmacSha256Hex(final byte[] bytes, String key) {
		return DigestUtil.hmacSha256Hex(bytes, key);
	}

	/**
	 * hmacSha384
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a byte array
	 */
	public static byte[] hmacSha384(String data, String key) {
		return DigestUtil.hmacSha384(data, key);
	}

	/**
	 * hmacSha384
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a byte array
	 */
	public static byte[] hmacSha384(final byte[] bytes, String key) {
		return DigestUtil.hmacSha384(bytes, key);
	}

	/**
	 * hmacSha384 Hex
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a hex string
	 */
	public static String hmacSha384Hex(String data, String key) {
		return DigestUtil.hmacSha384Hex(data, key);
	}

	/**
	 * hmacSha384 Hex
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a hex string
	 */
	public static String hmacSha384Hex(final byte[] bytes, String key) {
		return DigestUtil.hmacSha384Hex(bytes, key);
	}

	/**
	 * hmacSha512
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a byte array
	 */
	public static byte[] hmacSha512(String data, String key) {
		return DigestUtil.hmacSha512(data, key);
	}

	/**
	 * hmacSha512
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a byte array
	 */
	public static byte[] hmacSha512(final byte[] bytes, String key) {
		return DigestUtil.hmacSha512(bytes, key);
	}

	/**
	 * hmacSha512 Hex
	 *
	 * @param data Data to digest
	 * @param key  key
	 * @return digest as a hex string
	 */
	public static String hmacSha512Hex(String data, String key) {
		return DigestUtil.hmacSha512Hex(data, key);
	}

	/**
	 * hmacSha512 Hex
	 *
	 * @param bytes Data to digest
	 * @param key   key
	 * @return digest as a hex string
	 */
	public static String hmacSha512Hex(final byte[] bytes, String key) {
		return DigestUtil.hmacSha512Hex(bytes, key);
	}

	/**
	 * byte 数组序列化成 hex
	 *
	 * @param bytes bytes to encode
	 * @return MD5 digest as a hex string
	 */
	public static String encodeHex(byte[] bytes) {
		return DigestUtil.encodeHex(bytes);
	}

	/**
	 * 字符串反序列化成 hex
	 *
	 * @param hexString String to decode
	 * @return MD5 digest as a hex string
	 */
	public static byte[] decodeHex(final String hexString) {
		return DigestUtil.decodeHex(hexString);
	}

	/**
	 * Base64编码
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String encodeBase64(String value) {
		return Base64Util.encode(value);
	}

	/**
	 * Base64编码
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String encodeBase64(String value, Charset charset) {
		return Base64Util.encode(value, charset);
	}

	/**
	 * Base64编码为URL安全
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String encodeBase64UrlSafe(String value) {
		return Base64Util.encodeUrlSafe(value);
	}

	/**
	 * Base64编码为URL安全
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String encodeBase64UrlSafe(String value, Charset charset) {
		return Base64Util.encodeUrlSafe(value, charset);
	}

	/**
	 * Base64解码
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String decodeBase64(String value) {
		return Base64Util.decode(value);
	}

	/**
	 * Base64解码
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String decodeBase64(String value, Charset charset) {
		return Base64Util.decode(value, charset);
	}

	/**
	 * Base64URL安全解码
	 *
	 * @param value 字符串
	 * @return {String}
	 */
	public static String decodeBase64UrlSafe(String value) {
		return Base64Util.decodeUrlSafe(value);
	}

	/**
	 * Base64URL安全解码
	 *
	 * @param value   字符串
	 * @param charset 字符集
	 * @return {String}
	 */
	public static String decodeBase64UrlSafe(String value, Charset charset) {
		return Base64Util.decodeUrlSafe(value, charset);
	}

	/**
	 * 关闭 Closeable
	 *
	 * @param closeable 自动关闭
	 */
	public static void closeQuietly(@Nullable Closeable closeable) {
		IoUtil.closeQuietly(closeable);
	}

	/**
	 * InputStream to String utf-8
	 *
	 * @param input the <code>InputStream</code> to read from
	 * @return the requested String
	 * @throws NullPointerException if the input is null
	 */
	public static String readToString(InputStream input) {
		return IoUtil.readToString(input);
	}

	/**
	 * InputStream to String
	 *
	 * @param input   the <code>InputStream</code> to read from
	 * @param charset the <code>Charset</code>
	 * @return the requested String
	 * @throws NullPointerException if the input is null
	 */
	public static String readToString(@Nullable InputStream input, Charset charset) {
		return IoUtil.readToString(input, charset);
	}

	/**
	 * InputStream to bytes 数组
	 *
	 * @param input InputStream
	 * @return the requested byte array
	 */
	public static byte[] readToByteArray(@Nullable InputStream input) {
		return IoUtil.readToByteArray(input);
	}

	/**
	 * 读取文件为字符串
	 *
	 * @param file the file to read, must not be {@code null}
	 * @return the file contents, never {@code null}
	 */
	public static String readToString(final File file) {
		return FileUtil.readToString(file);
	}

	/**
	 * 读取文件为字符串
	 *
	 * @param file     the file to read, must not be {@code null}
	 * @param encoding the encoding to use, {@code null} means platform default
	 * @return the file contents, never {@code null}
	 */
	public static String readToString(File file, Charset encoding) {
		return FileUtil.readToString(file, encoding);
	}

	/**
	 * 读取文件为 byte 数组
	 *
	 * @param file the file to read, must not be {@code null}
	 * @return the file contents, never {@code null}
	 */
	public static byte[] readToByteArray(File file) {
		return FileUtil.readToByteArray(file);
	}


	/**
	 * 拼接临时文件目录.
	 *
	 * @return 临时文件目录.
	 */
	public static String toTempDirPath(String subDirFile) {
		return FileUtil.toTempDirPath(subDirFile);
	}

	/**
	 * Returns a {@link File} representing the system temporary directory.
	 *
	 * @return the system temporary directory.
	 */
	public static File getTempDir() {
		return FileUtil.getTempDir();
	}

	/**
	 * 拼接临时文件目录.
	 *
	 * @return 临时文件目录.
	 */
	public static File toTempDir(String subDirFile) {
		return FileUtil.toTempDir(subDirFile);
	}

	/**
	 * 获取资源，注意：boot 中请不要使用 Resource getFile，应该使用 getInputStream，支持一下协议：
	 *
	 * <p>
	 * 1. classpath:
	 * 2. file:
	 * 3. ftp:
	 * 4. http: and https:
	 * 6. C:/dir1/ and /Users/lcm
	 * </p>
	 *
	 * @param resourceLocation 资源路径
	 * @return Resource
	 * @throws IOException io异常
	 */
	public static Resource getResource(String resourceLocation) throws IOException {
		return ResourceUtil.getResource(resourceLocation);
	}

	/**
	 * 将对象序列化成json字符串
	 *
	 * @param object javaBean
	 * @return jsonString json字符串
	 */
	@Nullable
	public static String toJson(@Nullable Object object) {
		return JsonUtil.toJson(object);
	}

	/**
	 * 将对象序列化成 json byte 数组
	 *
	 * @param object javaBean
	 * @return jsonString json字符串
	 */
	@Nullable
	public static byte[] toJsonAsBytes(@Nullable Object object) {
		return JsonUtil.toJsonAsBytes(object);
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param jsonString jsonString
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(String jsonString) {
		return JsonUtil.readTree(jsonString);
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param in InputStream
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(InputStream in) {
		return JsonUtil.readTree(in);
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param content content
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(byte[] content) {
		return JsonUtil.readTree(content);
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param jsonParser JsonParser
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(JsonParser jsonParser) {
		return JsonUtil.readTree(jsonParser);
	}

	/**
	 * 将json byte 数组反序列化成对象
	 *
	 * @param bytes     json bytes
	 * @param valueType class
	 * @param <T>       T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readJson(byte[] bytes, Class<T> valueType) {
		return JsonUtil.readValue(bytes, valueType);
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param jsonString jsonString
	 * @param valueType  class
	 * @param <T>        T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readJson(@Nullable String jsonString, Class<T> valueType) {
		return JsonUtil.readValue(jsonString, valueType);
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param in        InputStream
	 * @param valueType class
	 * @param <T>       T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readJson(@Nullable InputStream in, Class<T> valueType) {
		return JsonUtil.readValue(in, valueType);
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param bytes         bytes
	 * @param typeReference 泛型类型
	 * @param <T>           T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readJson(byte[] bytes, TypeReference<T> typeReference) {
		return JsonUtil.readValue(bytes, typeReference);
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param jsonString    jsonString
	 * @param typeReference 泛型类型
	 * @param <T>           T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readJson(@Nullable String jsonString, TypeReference<T> typeReference) {
		return JsonUtil.readValue(jsonString, typeReference);
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param in            InputStream
	 * @param typeReference 泛型类型
	 * @param <T>           T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readJsonAsJson(@Nullable InputStream in, TypeReference<T> typeReference) {
		return JsonUtil.readValue(in, typeReference);
	}


	/**
	 * 读取集合
	 *
	 * @param content      bytes
	 * @param elementClass elementClass
	 * @param <T>          泛型
	 * @return 集合
	 */
	@Nullable
	public static <T> List<T> readJsonAsList(@Nullable byte[] content, Class<T> elementClass) {
		return JsonUtil.readList(content, elementClass);
	}

	/**
	 * 读取集合
	 *
	 * @param content      InputStream
	 * @param elementClass elementClass
	 * @param <T>          泛型
	 * @return 集合
	 */
	@Nullable
	public static <T> List<T> readJsonAsList(@Nullable InputStream content, Class<T> elementClass) {
		return JsonUtil.readList(content, elementClass);
	}

	/**
	 * 读取集合
	 *
	 * @param content      bytes
	 * @param elementClass elementClass
	 * @param <T>          泛型
	 * @return 集合
	 */
	@Nullable
	public static <T> List<T> readJsonAsList(@Nullable String content, Class<T> elementClass) {
		return JsonUtil.readList(content, elementClass);
	}

	/**
	 * 读取集合
	 *
	 * @param content    bytes
	 * @param keyClass   key类型
	 * @param valueClass 值类型
	 * @param <K>        泛型
	 * @param <V>        泛型
	 * @return 集合
	 */
	@Nullable
	public static <K, V> Map<K, V> readJsonAsMap(@Nullable byte[] content, Class<?> keyClass, Class<?> valueClass) {
		return JsonUtil.readMap(content, keyClass, valueClass);
	}

	/**
	 * 读取集合
	 *
	 * @param content    InputStream
	 * @param keyClass   key类型
	 * @param valueClass 值类型
	 * @param <K>        泛型
	 * @param <V>        泛型
	 * @return 集合
	 */
	@Nullable
	public static <K, V> Map<K, V> readJsonAsMap(@Nullable InputStream content, Class<?> keyClass, Class<?> valueClass) {
		return JsonUtil.readMap(content, keyClass, valueClass);
	}

	/**
	 * 读取集合
	 *
	 * @param content    bytes
	 * @param keyClass   key类型
	 * @param valueClass 值类型
	 * @param <K>        泛型
	 * @param <V>        泛型
	 * @return 集合
	 */
	@Nullable
	public static <K, V> Map<K, V> readJsonAsMap(@Nullable String content, Class<?> keyClass, Class<?> valueClass) {
		return JsonUtil.readMap(content, keyClass, valueClass);
	}

	/**
	 * url 编码
	 *
	 * @param source the String to be encoded
	 * @return the encoded String
	 */
	public static String urlEncode(String source) {
		return UrlUtil.encode(source, Charsets.UTF_8);
	}

	/**
	 * url 编码
	 *
	 * @param source  the String to be encoded
	 * @param charset the character encoding to encode to
	 * @return the encoded String
	 */
	public static String urlEncode(String source, Charset charset) {
		return UrlUtil.encode(source, charset);
	}

	/**
	 * url 解码
	 *
	 * @param source the encoded String
	 * @return the decoded value
	 * @throws IllegalArgumentException when the given source contains invalid encoded sequences
	 * @see StringUtils#uriDecode(String, Charset)
	 * @see java.net.URLDecoder#decode(String, String)
	 */
	public static String urlDecode(String source) {
		return StringUtils.uriDecode(source, Charsets.UTF_8);
	}

	/**
	 * url 解码
	 *
	 * @param source  the encoded String
	 * @param charset the character encoding to use
	 * @return the decoded value
	 * @throws IllegalArgumentException when the given source contains invalid encoded sequences
	 * @see StringUtils#uriDecode(String, Charset)
	 * @see java.net.URLDecoder#decode(String, String)
	 */
	public static String urlDecode(String source, Charset charset) {
		return StringUtils.uriDecode(source, charset);
	}

	/**
	 * 日期时间格式化
	 *
	 * @param date 时间
	 * @return 格式化后的时间
	 */
	public static String formatDateTime(Date date) {
		return DateUtil.formatDateTime(date);
	}

	/**
	 * 日期格式化
	 *
	 * @param date 时间
	 * @return 格式化后的时间
	 */
	public static String formatDate(Date date) {
		return DateUtil.formatDate(date);
	}

	/**
	 * 时间格式化
	 *
	 * @param date 时间
	 * @return 格式化后的时间
	 */
	public static String formatTime(Date date) {
		return DateUtil.formatTime(date);
	}

	/**
	 * 对象格式化 支持数字，date，java8时间
	 *
	 * @param object  格式化对象
	 * @param pattern 表达式
	 * @return 格式化后的字符串
	 */
	public static String format(Object object, String pattern) {
		if (object instanceof Number) {
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			return decimalFormat.format(object);
		} else if (object instanceof Date) {
			return DateUtil.format((Date) object, pattern);
		} else if (object instanceof TemporalAccessor) {
			return DateUtil.format((TemporalAccessor) object, pattern);
		}
		throw new IllegalArgumentException("未支持的对象:" + object + ",格式:" + object);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param pattern 表达式
	 * @return 时间
	 */
	public static Date parseDate(String dateStr, String pattern) {
		return DateUtil.parse(dateStr, pattern);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr   时间字符串
	 * @param formatter DateTimeFormatter
	 * @return 时间
	 */
	public static Date parse(String dateStr, DateTimeFormatter formatter) {
		return DateUtil.parse(dateStr, formatter);
	}

	/**
	 * 日期时间格式化
	 *
	 * @param temporal 时间
	 * @return 格式化后的时间
	 */
	public static String formatDateTime(TemporalAccessor temporal) {
		return DateUtil.formatDateTime(temporal);
	}

	/**
	 * 日期时间格式化
	 *
	 * @param temporal 时间
	 * @return 格式化后的时间
	 */
	public static String formatDate(TemporalAccessor temporal) {
		return DateUtil.formatDate(temporal);
	}

	/**
	 * 时间格式化
	 *
	 * @param temporal 时间
	 * @return 格式化后的时间
	 */
	public static String formatTime(TemporalAccessor temporal) {
		return DateUtil.formatTime(temporal);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr   时间字符串
	 * @param formatter DateTimeFormatter
	 * @return 时间
	 */
	public static LocalDateTime parseDateTime(String dateStr, DateTimeFormatter formatter) {
		return DateUtil.parseDateTime(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @return 时间
	 */
	public static LocalDateTime parseDateTime(String dateStr) {
		return DateUtil.parseDateTime(dateStr);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr   时间字符串
	 * @param formatter DateTimeFormatter
	 * @return 时间
	 */
	public static LocalDate parseDate(String dateStr, DateTimeFormatter formatter) {
		return DateUtil.parseDate(dateStr, formatter);
	}

	/**
	 * 将字符串转换为日期
	 *
	 * @param dateStr 时间字符串
	 * @return 时间
	 */
	public static LocalDate parseDate(String dateStr) {
		return DateUtil.parseDate(dateStr, DateUtil.DATE_FORMATTER);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr   时间字符串
	 * @param formatter DateTimeFormatter
	 * @return 时间
	 */
	public static LocalTime parseTime(String dateStr, DateTimeFormatter formatter) {
		return DateUtil.parseTime(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @return 时间
	 */
	public static LocalTime parseTime(String dateStr) {
		return DateUtil.parseTime(dateStr);
	}

	/**
	 * 时间比较
	 *
	 * @param startInclusive the start instant, inclusive, not null
	 * @param endExclusive   the end instant, exclusive, not null
	 * @return a {@code Duration}, not null
	 */
	public static Duration between(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive);
	}

	/**
	 * 比较2个 时间差
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return 时间间隔
	 */
	public static Duration between(Date startDate, Date endDate) {
		return DateUtil.between(startDate, endDate);
	}

	/**
	 * 对象类型转换
	 *
	 * @param source     the source object
	 * @param targetType the target type
	 * @param <T>        泛型标记
	 * @return the converted value
	 * @throws IllegalArgumentException if targetType is {@code null},
	 *                                  or sourceType is {@code null} but source is not {@code null}
	 */
	@Nullable
	public static <T> T convert(@Nullable Object source, Class<T> targetType) {
		return ConvertUtil.convert(source, targetType);
	}

	/**
	 * 对象类型转换
	 *
	 * @param source     the source object
	 * @param sourceType the source type
	 * @param targetType the target type
	 * @param <T>        泛型标记
	 * @return the converted value
	 * @throws IllegalArgumentException if targetType is {@code null},
	 *                                  or sourceType is {@code null} but source is not {@code null}
	 */
	@Nullable
	public static <T> T convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		return ConvertUtil.convert(source, sourceType, targetType);
	}

	/**
	 * 对象类型转换
	 *
	 * @param source     the source object
	 * @param targetType the target type
	 * @param <T>        泛型标记
	 * @return the converted value
	 * @throws IllegalArgumentException if targetType is {@code null},
	 *                                  or sourceType is {@code null} but source is not {@code null}
	 */
	@Nullable
	public static <T> T convert(@Nullable Object source, TypeDescriptor targetType) {
		return ConvertUtil.convert(source, targetType);
	}

	/**
	 * 获取方法参数信息
	 *
	 * @param constructor    构造器
	 * @param parameterIndex 参数序号
	 * @return {MethodParameter}
	 */
	public static MethodParameter getMethodParameter(Constructor<?> constructor, int parameterIndex) {
		return ClassUtil.getMethodParameter(constructor, parameterIndex);
	}

	/**
	 * 获取方法参数信息
	 *
	 * @param method         方法
	 * @param parameterIndex 参数序号
	 * @return {MethodParameter}
	 */
	public static MethodParameter getMethodParameter(Method method, int parameterIndex) {
		return ClassUtil.getMethodParameter(method, parameterIndex);
	}

	/**
	 * 获取Annotation注解
	 *
	 * @param annotatedElement AnnotatedElement
	 * @param annotationType   注解类
	 * @param <A>              泛型标记
	 * @return {Annotation}
	 */
	@Nullable
	public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement, Class<A> annotationType) {
		return AnnotatedElementUtils.findMergedAnnotation(annotatedElement, annotationType);
	}

	/**
	 * 获取Annotation，先找方法，没有则再找方法上的类
	 *
	 * @param method         Method
	 * @param annotationType 注解类
	 * @param <A>            泛型标记
	 * @return {Annotation}
	 */
	@Nullable
	public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
		return ClassUtil.getAnnotation(method, annotationType);
	}

	/**
	 * 获取Annotation，先找HandlerMethod，没有则再找对应的类
	 *
	 * @param handlerMethod  HandlerMethod
	 * @param annotationType 注解类
	 * @param <A>            泛型标记
	 * @return {Annotation}
	 */
	@Nullable
	public static <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
		return ClassUtil.getAnnotation(handlerMethod, annotationType);
	}

	/**
	 * 实例化对象
	 *
	 * @param clazz 类
	 * @param <T>   泛型标记
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> clazz) {
		return (T) BeanUtil.instantiateClass(clazz);
	}

	/**
	 * 实例化对象
	 *
	 * @param clazzStr 类名
	 * @param <T>      泛型标记
	 * @return 对象
	 */
	public static <T> T newInstance(String clazzStr) {
		return BeanUtil.newInstance(clazzStr);
	}

	/**
	 * 获取Bean的属性
	 *
	 * @param bean         bean
	 * @param propertyName 属性名
	 * @return 属性值
	 */
	@Nullable
	public static Object getProperty(@Nullable Object bean, String propertyName) {
		return BeanUtil.getProperty(bean, propertyName);
	}

	/**
	 * 设置Bean属性
	 *
	 * @param bean         bean
	 * @param propertyName 属性名
	 * @param value        属性值
	 */
	public static void setProperty(Object bean, String propertyName, Object value) {
		BeanUtil.setProperty(bean, propertyName, value);
	}

	/**
	 * 浅复制
	 *
	 * @param source 源对象
	 * @param <T>    泛型标记
	 * @return T
	 */
	@Nullable
	public static <T> T clone(@Nullable T source) {
		return BeanUtil.clone(source);
	}

	/**
	 * 拷贝对象，支持 Map 和 Bean
	 *
	 * @param source 源对象
	 * @param clazz  类名
	 * @param <T>    泛型标记
	 * @return T
	 */
	@Nullable
	public static <T> T copy(@Nullable Object source, Class<T> clazz) {
		return BeanUtil.copy(source, clazz);
	}

	/**
	 * 拷贝对象，支持 Map 和 Bean
	 *
	 * @param source     源对象
	 * @param targetBean 需要赋值的对象
	 */
	public static void copy(@Nullable Object source, @Nullable Object targetBean) {
		BeanUtil.copy(source, targetBean);
	}

	/**
	 * 拷贝对象，source 对象属性做非 null 判断
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param source     源对象
	 * @param targetBean 需要赋值的对象
	 */
	public static void copyNonNull(@Nullable Object source, @Nullable Object targetBean) {
		BeanUtil.copyNonNull(source, targetBean);
	}

	/**
	 * 拷贝对象，并对不同类型属性进行转换
	 *
	 * @param source 源对象
	 * @param clazz  类名
	 * @param <T>    泛型标记
	 * @return T
	 */
	@Nullable
	public static <T> T copyWithConvert(@Nullable Object source, Class<T> clazz) {
		return BeanUtil.copyWithConvert(source, clazz);
	}

	/**
	 * 拷贝列表对象
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param sourceList  源列表
	 * @param targetClazz 转换成的类型
	 * @param <T>         泛型标记
	 * @return T
	 */
	public static <T> List<T> copy(@Nullable Collection<?> sourceList, Class<T> targetClazz) {
		return BeanUtil.copy(sourceList, targetClazz);
	}

	/**
	 * 拷贝列表对象，并对不同类型属性进行转换
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param sourceList  源对象列表
	 * @param targetClazz 转换成的类
	 * @param <T>         泛型标记
	 * @return List
	 */
	public static <T> List<T> copyWithConvert(@Nullable Collection<?> sourceList, Class<T> targetClazz) {
		return BeanUtil.copyWithConvert(sourceList, targetClazz);
	}

	/**
	 * 拷贝对象，扩展 Spring 的拷贝方法
	 *
	 * @param source the source bean
	 * @param clazz  the target bean class
	 * @param <T>    泛型标记
	 * @return T
	 * @throws BeansException if the copying failed
	 */
	@Nullable
	public static <T> T copyProperties(@Nullable Object source, Class<T> clazz) throws BeansException {
		return BeanUtil.copyProperties(source, clazz);
	}

	/**
	 * 拷贝列表对象，扩展 Spring 的拷贝方法
	 *
	 * @param sourceList  the source list bean
	 * @param targetClazz the target bean class
	 * @param <T>         泛型标记
	 * @return List
	 * @throws BeansException if the copying failed
	 */
	public static <T> List<T> copyProperties(@Nullable Collection<?> sourceList, Class<T> targetClazz) throws BeansException {
		return BeanUtil.copyProperties(sourceList, targetClazz);
	}

	/**
	 * 将对象装成map形式
	 *
	 * @param bean 源对象
	 * @return {Map}
	 */
	public static Map<String, Object> toMap(@Nullable Object bean) {
		return BeanUtil.toMap(bean);
	}

	/**
	 * 将map 转为 bean
	 *
	 * @param beanMap   map
	 * @param valueType 对象类型
	 * @param <T>       泛型标记
	 * @return {T}
	 */
	public static <T> T toBean(Map<String, Object> beanMap, Class<T> valueType) {
		return BeanUtil.toBean(beanMap, valueType);
	}

}
