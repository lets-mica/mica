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

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 忽略的信息
 *
 * @author L.cm
 */
@Getter
@RequiredArgsConstructor
class XssCleanIgnoreInfo {
	private static final String[] EMPTY = new String[0];
	/**
	 * 实例
	 */
	public static final XssCleanIgnoreInfo INSTANCE = new XssCleanIgnoreInfo();

	/**
	 * 忽略的属性名
	 */
	private final String[] params;

	private XssCleanIgnoreInfo() {
		this(EMPTY);
	}

	/**
	 * 获取，转换注解信息
	 *
	 * @param annotation XssCleanIgnore
	 * @return XssCleanIgnoreInfo
	 */
	public static XssCleanIgnoreInfo from(XssCleanIgnore annotation) {
		if (annotation == null) {
			return null;
		}
		return new XssCleanIgnoreInfo(annotation.value());
	}

}
