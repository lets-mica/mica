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
}
