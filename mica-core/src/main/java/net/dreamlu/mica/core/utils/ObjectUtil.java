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

import java.util.Objects;

/**
 * 对象工具类
 *
 * @author L.cm
 */
@UtilityClass
public class ObjectUtil extends org.springframework.util.ObjectUtils {

	/**
	 * 判断对象为null
	 *
	 * @param object 对象
	 * @return 对象是否为空
	 */
	public static boolean isNull(@Nullable Object object) {
		return Objects.isNull(object);
	}

	/**
	 * 判断对象不为null
	 *
	 * @param object 对象
	 * @return 对象是否不为空
	 */
	public static boolean isNotNull(@Nullable Object object) {
		return Objects.nonNull(object);
	}

	/**
	 * 判断对象为true
	 *
	 * @param object 对象
	 * @return 对象是否为true
	 */
	public static boolean isTrue(@Nullable Boolean object) {
		return Boolean.TRUE.equals(object);
	}

	/**
	 * 判断对象为false
	 *
	 * @param object 对象
	 * @return 对象是否为false
	 */
	public static boolean isFalse(@Nullable Boolean object) {
		return object == null || Boolean.FALSE.equals(object);
	}

	/**
	 * 判断数组不为空
	 *
	 * @param array 数组
	 * @return 数组是否为空
	 */
	public static boolean isNotEmpty(@Nullable Object[] array) {
		return !ObjectUtil.isEmpty(array);
	}

	/**
	 * 判断对象不为空
	 *
	 * @param obj 数组
	 * @return 数组是否为空
	 */
	public static boolean isNotEmpty(@Nullable Object obj) {
		return !ObjectUtil.isEmpty(obj);
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
	 * 强转string
	 *
	 * @param object Object
	 * @return String
	 */
	@Nullable
	public static String toStr(@Nullable Object object) {
		return toStr(object, null);
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
		if (null == object) {
			return defaultValue;
		}
		if (object instanceof CharSequence) {
			return ((CharSequence) object).toString();
		}
		return String.valueOf(object);
	}

	/**
	 * 对象转为 int （支持 String 和 Number），默认: 0
	 *
	 * @param object Object
	 * @return int
	 */
	public static int toInt(@Nullable Object object) {
		return toInt(object, 0);
	}

	/**
	 * 对象转为 int （支持 String 和 Number）
	 *
	 * @param object       Object
	 * @param defaultValue 默认值
	 * @return int
	 */
	public static int toInt(@Nullable Object object, int defaultValue) {
		if (object instanceof Number) {
			return ((Number) object).intValue();
		}
		if (object instanceof CharSequence) {
			String value = ((CharSequence) object).toString();
			try {
				return Integer.parseInt(value);
			} catch (final NumberFormatException nfe) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 对象转为 long （支持 String 和 Number），默认: 0L
	 *
	 * @param object Object
	 * @return long
	 */
	public static long toLong(@Nullable Object object) {
		return toLong(object, 0L);
	}

	/**
	 * 对象转为 long （支持 String 和 Number），默认: 0L
	 *
	 * @param object Object
	 * @return long
	 */
	public static long toLong(@Nullable Object object, long defaultValue) {
		if (object instanceof Number) {
			return ((Number) object).longValue();
		}
		if (object instanceof CharSequence) {
			String value = ((CharSequence) object).toString();
			try {
				return Long.parseLong(value);
			} catch (final NumberFormatException nfe) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 对象转为 Float
	 *
	 * @param object Object
	 * @return 结果
	 */
	public static float toFloat(@Nullable Object object) {
		return toFloat(object, 0.0f);
	}

	/**
	 * 对象转为 Float
	 *
	 * @param object Object
	 * @param defaultValue float
	 * @return 结果
	 */
	public static float toFloat(@Nullable Object object, float defaultValue) {
		if (object instanceof Number) {
			return ((Number) object).floatValue();
		}
		if (object instanceof CharSequence) {
			String value = ((CharSequence) object).toString();
			try {
				return Float.parseFloat(value);
			} catch (NumberFormatException nfe) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 对象转为 Double
	 *
	 * @param object Object
	 * @return 结果
	 */
	public static double toDouble(@Nullable Object object) {
		return toDouble(object, 0.0d);
	}

	/**
	 * 对象转为 Double
	 *
	 * @param object Object
	 * @param defaultValue double
	 * @return 结果
	 */
	public static double toDouble(@Nullable Object object, double defaultValue) {
		if (object instanceof Number) {
			return ((Number) object).doubleValue();
		}
		if (object instanceof CharSequence) {
			String value = ((CharSequence) object).toString();
			try {
				return Double.parseDouble(value);
			} catch (NumberFormatException nfe) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 对象转为 Byte
	 *
	 * @param object Object
	 * @return 结果
	 */
	public static byte toByte(@Nullable Object object) {
		return toByte(object, (byte) 0);
	}

	/**
	 * 对象转为 Byte
	 *
	 * @param object Object
	 * @param defaultValue byte
	 * @return 结果
	 */
	public static byte toByte(@Nullable Object object, byte defaultValue) {
		if (object instanceof Number) {
			return ((Number) object).byteValue();
		}
		if (object instanceof CharSequence) {
			String value = ((CharSequence) object).toString();
			try {
				return Byte.parseByte(value);
			} catch (NumberFormatException nfe) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 对象转为 Short
	 *
	 * @param object Object
	 * @return 结果
	 */
	public static short toShort(@Nullable Object object) {
		return toShort(object, (short) 0);
	}

	/**
	 * 对象转为 Short
	 *
	 * @param object Object
	 * @param defaultValue short
	 * @return 结果
	 */
	public static short toShort(@Nullable Object object, short defaultValue) {
		if (object instanceof Number) {
			return ((Number) object).byteValue();
		}
		if (object instanceof CharSequence) {
			String value = ((CharSequence) object).toString();
			try {
				return Short.parseShort(value);
			} catch (NumberFormatException nfe) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 对象转为 Boolean
	 *
	 * @param object Object
	 * @return 结果
	 */
	@Nullable
	public static Boolean toBoolean(@Nullable Object object) {
		return toBoolean(object, null);
	}

	/**
	 * 对象转为 Boolean
	 *
	 * @param object Object
	 * @param defaultValue 默认值
	 * @return 结果
	 */
	@Nullable
	public static Boolean toBoolean(@Nullable Object object, @Nullable Boolean defaultValue) {
		if (object instanceof Boolean) {
			return (Boolean) object;
		}
		if (object instanceof CharSequence) {
			String value = ((CharSequence) object).toString();
			try {
				return Boolean.parseBoolean(value.trim());
			} catch (NumberFormatException nfe) {
				return defaultValue;
			}
		}
		return defaultValue;
	}
}
