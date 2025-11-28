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

import net.dreamlu.mica.core.utils.Exceptions;

/**
 * xss 数据处理类型
 *
 * @author L.cm
 */
public enum XssType {

	/**
	 * 表单
	 */
	FORM() {
		@Override
		public RuntimeException getXssException(String name, String input) {
			return new FromXssException(input, "Xss校验失败, 值:" + input);
		}
	},

	/**
	 * body json
	 */
	JACKSON() {
		@Override
		public RuntimeException getXssException(String name, String input) {
			return Exceptions.unchecked(new JacksonXssException(name, input, "Xss校验失败，属性：" + name + ", 值:" + input));
		}
	};

	/**
	 * 获取 xss 异常
	 *
	 * @param name    属性名
	 * @param input   input
	 * @return XssException
	 */
	public abstract RuntimeException getXssException(String name, String input);

}
