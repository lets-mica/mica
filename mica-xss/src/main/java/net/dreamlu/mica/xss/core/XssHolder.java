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

package net.dreamlu.mica.xss.core;

import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * 利用 ThreadLocal 缓存线程间的数据
 *
 * @author L.cm
 */
public class XssHolder {
	private static final ThreadLocal<XssCleanIgnore> TL = new ThreadLocal<>();

	/**
	 * 是否开启
	 *
	 * @return boolean
	 */
	public static boolean isEnabled() {
		return Objects.isNull(TL.get());
	}

	/**
	 * 判断是否被忽略
	 *
	 * @return XssCleanIgnore
	 */
	static boolean isIgnore(String name) {
		XssCleanIgnore cleanIgnore = TL.get();
		if (cleanIgnore == null) {
			return false;
		}
		String[] ignoreArray = cleanIgnore.value();
		// 指定忽略的属性
		return ObjectUtils.containsElement(ignoreArray, name);
	}

	/**
	 * 标记为开启
	 */
	static void setIgnore(XssCleanIgnore xssCleanIgnore) {
		TL.set(xssCleanIgnore);
	}

	/**
	 * 关闭 xss 清理
	 */
	public static void remove() {
		TL.remove();
	}

}
