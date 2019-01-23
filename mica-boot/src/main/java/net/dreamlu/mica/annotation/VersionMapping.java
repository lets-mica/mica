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

package net.dreamlu.mica.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

/**
 * 版本号处理
 *
 * <p>
 *     1. url 版本号：添加到 url 前
 *     2. Accept 版本：application/vnd.mica.VERSION+json
 * </p>
 *
 * @author L.cm
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping
@UrlVersion
@ApiVersion
@Validated
public @interface VersionMapping {
	/**
	 * Alias for {@link RequestMapping#name}.
	 * @return {String[]}
	 */
	@AliasFor(annotation = RequestMapping.class)
	String name() default "";

	/**
	 * Alias for {@link RequestMapping#value}.
	 * @return {String[]}
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] value() default {};

	/**
	 * Alias for {@link RequestMapping#path}.
	 * @return {String[]}
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] path() default {};

	/**
	 * Alias for {@link RequestMapping#params}.
	 * @return {String[]}
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] params() default {};

	/**
	 * Alias for {@link RequestMapping#headers}.
	 * @return {String[]}
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] headers() default {};

	/**
	 * Alias for {@link RequestMapping#consumes}.
	 * @return {String[]}
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] consumes() default {};

	/**
	 * Alias for {@link RequestMapping#produces}.
	 * default json utf-8
	 * @return {String[]}
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] produces() default {};

	/**
	 * Alias for {@link UrlVersion#value}.
	 * @return {String}
	 */
	@AliasFor(annotation = UrlVersion.class, attribute = "value")
	String urlVersion() default "";

	/**
	 * Alias for {@link ApiVersion#value}.
	 * @return {String}
	 */
	@AliasFor(annotation = ApiVersion.class, attribute = "value")
	String apiVersion() default "";

}
