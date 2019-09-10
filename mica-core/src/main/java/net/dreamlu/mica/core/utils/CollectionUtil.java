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

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 集合工具类
 *
 * @author L.cm
 */
@UtilityClass
@SuppressWarnings("unchecked")
public class CollectionUtil extends org.springframework.util.CollectionUtils {

	/**
	 * Return {@code true} if the supplied Collection is not {@code null} or empty.
	 * Otherwise, return {@code false}.
	 *
	 * @param collection the Collection to check
	 * @return whether the given Collection is not empty
	 */
	public static boolean isNotEmpty(@Nullable Collection<?> collection) {
		return !CollectionUtil.isEmpty(collection);
	}

	/**
	 * Return {@code true} if the supplied Map is not {@code null} or empty.
	 * Otherwise, return {@code false}.
	 *
	 * @param map the Map to check
	 * @return whether the given Map is not empty
	 */
	public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
		return !CollectionUtil.isEmpty(map);
	}

	/**
	 * Check whether the given Array contains the given element.
	 *
	 * @param array   the Array to check
	 * @param element the element to look for
	 * @param <T>     The generic tag
	 * @return {@code true} if found, {@code false} else
	 */
	public static <T> boolean contains(@Nullable T[] array, final T element) {
		if (array == null) {
			return false;
		}
		return Arrays.stream(array).anyMatch(x -> ObjectUtil.nullSafeEquals(x, element));
	}

	/**
	 * Concatenates 2 arrays
	 *
	 * @param one   数组1
	 * @param other 数组2
	 * @return 新数组
	 */
	public static String[] concat(String[] one, String[] other) {
		return concat(one, other, String.class);
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
		T[] target = (T[]) Array.newInstance(clazz, one.length + other.length);
		System.arraycopy(one, 0, target, 0, one.length);
		System.arraycopy(other, 0, target, one.length, other.length);
		return target;
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
		Objects.requireNonNull(es, "args es is null.");
		return Arrays.stream(es).collect(Collectors.toSet());
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
		Objects.requireNonNull(es, "args es is null.");
		return Arrays.stream(es).collect(Collectors.toList());
	}

	/**
	 * Iterable 转换为List集合
	 *
	 * @param elements Iterable
	 * @param <E>      泛型
	 * @return 集合
	 */
	public static <E> List<E> toList(Iterable<E> elements) {
		Objects.requireNonNull(elements, "elements es is null.");
		if (elements instanceof Collection) {
			return new ArrayList((Collection) elements);
		}
		Iterator<E> iterator = elements.iterator();
		List<E> list = new ArrayList<>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

	/**
	 * 将key value 数组转为 map
	 *
	 * @param keysValues key value 数组
	 * @param <K>        key
	 * @param <V>        value
	 * @return map 集合
	 */
	public static <K, V> Map<K, V> toMap(Object... keysValues) {
		int kvLength = keysValues.length;
		if (kvLength % 2 != 0) {
			throw new IllegalArgumentException("wrong number of arguments for met, keysValues length can not be odd");
		}
		Map<K, V> keyValueMap = new HashMap<>(kvLength);
		for (int i = kvLength - 2; i >= 0; i -= 2) {
			Object key = keysValues[i];
			Object value = keysValues[i + 1];
			keyValueMap.put((K) key, (V) value);
		}
		return keyValueMap;
	}

}
