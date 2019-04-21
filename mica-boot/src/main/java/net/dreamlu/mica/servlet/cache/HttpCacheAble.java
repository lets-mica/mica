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

package net.dreamlu.mica.servlet.cache;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Http cache 只支持方法上添加注解，因为 http-cache 只支持 get 和 head
 *
 * <p>
 * cache-control
 * <p>
 * max-age 大于0 时 直接从游览器缓存中 提取
 * max-age 小于或等于0 时 向server 发送http 请求确认 ,该资源是否有修改
 * </p>
 *
 * @author L.cm
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpCacheAble {

	/**
	 * 缓存的时间,默认0,单位秒
	 *
	 * @return {long}
	 */
	@AliasFor("maxAge")
	long value();

	/**
	 * 缓存的时间,默认0,单位秒
	 *
	 * @return {long}
	 */
	@AliasFor("value")
	long maxAge() default 0;

}
