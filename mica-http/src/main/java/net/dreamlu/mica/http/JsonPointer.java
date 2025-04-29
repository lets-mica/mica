/*
 * Copyright (c) 2019-2029, Dreamlu (596392912@qq.com & www.dreamlu.net).
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

package net.dreamlu.mica.http;

import java.lang.annotation.*;

/**
 * 利用 jackson JSON Pointer 语法直接解析 bean
 *
 * @author L.cm
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface JsonPointer {

	/**
	 * JSON Pointer 语法 value
	 * @return value
	 */
	String value();

	/**
	 * 嵌套的内部模型：默认 false
	 *
	 * @return 是否为内部模型
	 */
	boolean inner() default false;

}
