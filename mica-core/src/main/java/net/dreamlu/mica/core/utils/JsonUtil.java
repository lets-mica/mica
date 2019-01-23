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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.experimental.UtilityClass;
import net.dreamlu.mica.core.jackson.MicaJavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * json 工具类
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
	public static String toJson(Object object) {
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
	public static byte[] toJsonAsBytes(Object object) {
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
		try {
			return getInstance().readTree(jsonParser);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json byte 数组反序列化成对象
	 *
	 * @param bytes json bytes
	 * @param valueType  class
	 * @param <T>        T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(byte[] bytes, Class<T> valueType) {
		try {
			return getInstance().readValue(bytes, valueType);
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
	public static <T> T parse(String jsonString, Class<T> valueType) {
		try {
			return getInstance().readValue(jsonString, valueType);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param in InputStream
	 * @param valueType  class
	 * @param <T>        T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(InputStream in, Class<T> valueType) {
		try {
			return getInstance().readValue(in, valueType);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param bytes bytes
	 * @param typeReference 泛型类型
	 * @param <T>        T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(byte[] bytes, TypeReference<?> typeReference) {
		try {
			return getInstance().readValue(bytes, typeReference);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param jsonString jsonString
	 * @param typeReference 泛型类型
	 * @param <T>        T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(String jsonString, TypeReference<?> typeReference) {
		try {
			return getInstance().readValue(jsonString, typeReference);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将json反序列化成对象
	 *
	 * @param in InputStream
	 * @param typeReference 泛型类型
	 * @param <T>        T 泛型标记
	 * @return Bean
	 */
	public static <T> T parse(InputStream in, TypeReference<?> typeReference) {
		try {
			return getInstance().readValue(in, typeReference);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
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
