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

package net.dreamlu.mica.lock.annotation;

import java.lang.annotation.*;

/**
 * 分布式锁注解
 *
 * @author L.cm
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisLock {

	/**
	 * 分布式锁的 key，必须：请保持唯一性
	 *
	 * @return key
	 */
	String value();

	/**
	 * 分布式锁参数，可选，支持 spring el # 读取方法参数和 @ 读取 spring bean
	 *
	 * @return param
	 */
	String param() default "";

	/**
	 * 等待锁超时时间，单位：秒
	 * 默认30s
	 *
	 * @return int
	 */
	int waitTime() default 30;

	/**
	 * 自动解锁时间，单位秒
	 * 自动解锁时间一定得大于方法执行时间，否则会导致锁提前释放
	 * 默认100s
	 *
	 * @return int
	 */
	int leaseTime() default 100;

	/**
	 * 忽略所有异常，否则会往外抛
	 *
	 * @return int
	 */
	boolean ignoreException() default false;

	/**
	 * 忽略没有获取到锁的异常，默认为true
	 *
	 * @return int
	 */
	boolean ignoreUnableToAcquiredLockException() default true;
}
