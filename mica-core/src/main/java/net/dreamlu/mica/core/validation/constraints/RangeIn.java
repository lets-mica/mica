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

package net.dreamlu.mica.core.validation.constraints;

import net.dreamlu.mica.core.validation.constraintvalidators.RangeInValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证规定字段只能在范围内，可用于数值、字符串、数组、List、Set.
 * <p>
 * 例如：
 * <pre>
 * {@code
 * @RangeIn(value = "1,2", message = "输入值只允许： [1, 2]")
 * private Long code;
 *
 * @RangeIn(value = "1,2", message = "输入值只允许： [1, 2]")
 * private List<Long> codes;
 * }
 * </pre>
 * @author dy
 */
@Constraint(validatedBy = RangeInValidator.class)
@Target({
	ElementType.METHOD,
	ElementType.FIELD,
	ElementType.ANNOTATION_TYPE,
	ElementType.CONSTRUCTOR,
	ElementType.PARAMETER,
	ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface RangeIn {
	String value();

	String message() default "输入值不在范围内";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
