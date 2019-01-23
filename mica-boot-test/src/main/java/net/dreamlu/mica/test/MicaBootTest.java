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

package net.dreamlu.mica.test;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 简化 测试
 *
 * @author L.cm
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MicaBootTest {
	/**
	 * 服务名：appName
	 * @return appName
	 */
	@AliasFor("appName")
	String value() default "mica-test";
	/**
	 * 服务名：appName
	 * @return appName
	 */
	@AliasFor("value")
	String appName() default "mica-test";
	/**
	 * profile
	 * @return profile
	 */
	String profile() default "dev";
	/**
	 * 启用 ServiceLoader 加载 launcherService
	 * @return 是否启用
	 */
	boolean enableLoader() default false;
}
