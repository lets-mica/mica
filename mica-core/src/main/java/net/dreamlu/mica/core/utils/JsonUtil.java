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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import lombok.experimental.UtilityClass;
import net.dreamlu.mica.core.jackson.MicaJavaTimeModule;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

/**
 * json 工具类
 *
 * @author L.cm
 */
@UtilityClass
public class JsonUtil {

	/**
	 * 将对象序列化成json字符串
	 *
	 * @param object javaBean
	 * @return jsonString json字符串
	 */
	@Nullable
	public static String toJson(@Nullable Object object) {
		if (object == null) {
			return null;
		}
		try {
			return getInstance().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将对象序列化成 json byte 数组
	 *
	 * @param object javaBean
	 * @return jsonString json字符串
	 */
	public static byte[] toJsonAsBytes(@Nullable Object object) {
		if (object == null) {
			return new byte[0];
		}
		try {
			return getInstance().writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param jsonString jsonString
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(String jsonString) {
		Objects.requireNonNull(jsonString, "jsonString is null");
		try {
			return getInstance().readTree(jsonString);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param in InputStream
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(InputStream in) {
		Objects.requireNonNull(in, "InputStream in is null");
		try {
			return getInstance().readTree(in);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param content content
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(byte[] content) {
		Objects.requireNonNull(content, "byte[] content is null");
		try {
			return getInstance().readTree(content);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json字符串转成 JsonNode
	 *
	 * @param jsonParser JsonParser
	 * @return jsonString json字符串
	 */
	public static JsonNode readTree(JsonParser jsonParser) {
		Objects.requireNonNull(jsonParser, "jsonParser is null");
		try {
			return getInstance().readTree(jsonParser);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json byte 数组反序列化成对象
	 *
	 * @param content   json bytes
	 * @param valueType class
	 * @param <T>       T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readValue(@Nullable byte[] content, Class<T> valueType) {
		if (ObjectUtil.isEmpty(content)) {
			return null;
		}
		try {
			return getInstance().readValue(content, valueType);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
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
	public static <T> T readValue(@Nullable String jsonString, Class<T> valueType) {
		if (StringUtil.isBlank(jsonString)) {
			return null;
		}
		try {
			return getInstance().readValue(jsonString, valueType);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
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
	public static <T> T readValue(@Nullable InputStream in, Class<T> valueType) {
		if (in == null) {
			return null;
		}
		try {
			return getInstance().readValue(in, valueType);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param content       bytes
	 * @param typeReference 泛型类型
	 * @param <T>           T 泛型标记
	 * @return Bean
	 */
	@Nullable
	public static <T> T readValue(@Nullable byte[] content, TypeReference<T> typeReference) {
		if (ObjectUtil.isEmpty(content)) {
			return null;
		}
		try {
			return getInstance().readValue(content, typeReference);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
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
	public static <T> T readValue(@Nullable String jsonString, TypeReference<T> typeReference) {
		if (StringUtil.isBlank(jsonString)) {
			return null;
		}
		try {
			return getInstance().readValue(jsonString, typeReference);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
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
	public static <T> T readValue(@Nullable InputStream in, TypeReference<T> typeReference) {
		if (in == null) {
			return null;
		}
		try {
			return getInstance().readValue(in, typeReference);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 封装 map type
	 *
	 * @param keyClass   key 类型
	 * @param valueClass value 类型
	 * @return MapType
	 */
	public static MapType getMapType(Class<?> keyClass, Class<?> valueClass) {
		return getInstance().getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
	}

	/**
	 * 封装 map type
	 *
	 * @param elementClass 集合值类型
	 * @return CollectionLikeType
	 */
	public static CollectionLikeType getListType(Class<?> elementClass) {
		return getInstance().getTypeFactory().constructCollectionLikeType(List.class, elementClass);
	}

	/**
	 * 读取集合
	 *
	 * @param content      bytes
	 * @param elementClass elementClass
	 * @param <T>          泛型
	 * @return 集合
	 */
	public static <T> List<T> readList(@Nullable byte[] content, Class<T> elementClass) {
		if (ObjectUtil.isEmpty(content)) {
			return Collections.emptyList();
		}
		try {
			return getInstance().readValue(content, getListType(elementClass));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 读取集合
	 *
	 * @param content      InputStream
	 * @param elementClass elementClass
	 * @param <T>          泛型
	 * @return 集合
	 */
	public static <T> List<T> readList(@Nullable InputStream content, Class<T> elementClass) {
		if (content == null) {
			return Collections.emptyList();
		}
		try {
			return getInstance().readValue(content, getListType(elementClass));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 读取集合
	 *
	 * @param content      bytes
	 * @param elementClass elementClass
	 * @param <T>          泛型
	 * @return 集合
	 */
	public static <T> List<T> readList(@Nullable String content, Class<T> elementClass) {
		if (ObjectUtil.isEmpty(content)) {
			return Collections.emptyList();
		}
		try {
			return getInstance().readValue(content, getListType(elementClass));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
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
	public static <K, V> Map<K, V> readMap(@Nullable byte[] content, Class<?> keyClass, Class<?> valueClass) {
		if (ObjectUtil.isEmpty(content)) {
			return Collections.emptyMap();
		}
		try {
			return getInstance().readValue(content, getMapType(keyClass, valueClass));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
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
	public static <K, V> Map<K, V> readMap(@Nullable InputStream content, Class<?> keyClass, Class<?> valueClass) {
		if (ObjectUtil.isEmpty(content)) {
			return Collections.emptyMap();
		}
		try {
			return getInstance().readValue(content, getMapType(keyClass, valueClass));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
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
	public static <K, V> Map<K, V> readMap(@Nullable String content, Class<?> keyClass, Class<?> valueClass) {
		if (ObjectUtil.isEmpty(content)) {
			return Collections.emptyMap();
		}
		try {
			return getInstance().readValue(content, getMapType(keyClass, valueClass));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * jackson 的类型转换
	 *
	 * @param fromValue   来源对象
	 * @param toValueType 转换的类型
	 * @param <T>         泛型标记
	 * @return 转换结果
	 */
	public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
		return getInstance().convertValue(fromValue, toValueType);
	}

	/**
	 * jackson 的类型转换
	 *
	 * @param fromValue   来源对象
	 * @param toValueType 转换的类型
	 * @param <T>         泛型标记
	 * @return 转换结果
	 */
	public static <T> T convertValue(Object fromValue, JavaType toValueType) {
		return getInstance().convertValue(fromValue, toValueType);
	}

	/**
	 * jackson 的类型转换
	 *
	 * @param fromValue      来源对象
	 * @param toValueTypeRef 泛型类型
	 * @param <T>            泛型标记
	 * @return 转换结果
	 */
	public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
		return getInstance().convertValue(fromValue, toValueTypeRef);
	}

	/**
	 * tree 转对象
	 *
	 * @param treeNode  TreeNode
	 * @param valueType valueType
	 * @param <T>       泛型标记
	 * @return 转换结果
	 */
	public static <T> T treeToValue(TreeNode treeNode, Class<T> valueType) {
		try {
			return getInstance().treeToValue(treeNode, valueType);
		} catch (JsonProcessingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 对象转为 json node
	 *
	 * @param value 对象
	 * @return JsonNode
	 */
	public static JsonNode valueToTree(@Nullable Object value) {
		return getInstance().valueToTree(value);
	}

	/**
	 * 判断是否可以序列化
	 *
	 * @param value 对象
	 * @return 是否可以序列化
	 */
	public static boolean canSerialize(@Nullable Object value) {
		if (value == null) {
			return true;
		}
		return getInstance().canSerialize(value.getClass());
	}

	public static ObjectMapper getInstance() {
		return JacksonHolder.INSTANCE;
	}

	private static class JacksonHolder {
		private static ObjectMapper INSTANCE = new JacksonObjectMapper();
	}

	private static class JacksonObjectMapper extends ObjectMapper {
		private static final long serialVersionUID = 4288193147502386170L;

		private static final Locale CHINA = Locale.CHINA;

		JacksonObjectMapper() {
			super();
			super.setLocale(CHINA);
			super.setDateFormat(new SimpleDateFormat(DateUtil.PATTERN_DATETIME, CHINA));
			// 单引号
			super.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			super.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
			// 允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
			super.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			// 忽略json字符串中不识别的属性
			super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// 忽略无法转换的对象
			super.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			super.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
			super.findAndRegisterModules();
			super.registerModule(new MicaJavaTimeModule());
		}

	}
}
