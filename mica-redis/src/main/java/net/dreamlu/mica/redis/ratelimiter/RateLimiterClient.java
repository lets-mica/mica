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

package net.dreamlu.mica.redis.ratelimiter;

import net.dreamlu.mica.core.function.CheckedSupplier;
import net.dreamlu.mica.core.utils.Exceptions;

import java.util.concurrent.TimeUnit;

/**
 * RateLimiter 限流 Client
 *
 * @author L.cm
 */
public interface RateLimiterClient {

	/**
	 * 服务是否被限流
	 *
	 * @param key 自定义的key，请保证唯一
	 * @param max 支持的最大请求
	 * @param ttl 时间,单位默认为秒（seconds）
	 * @return 是否允许
	 */
	default boolean isAllowed(String key, long max, long ttl) {
		return this.isAllowed(key, max, ttl, TimeUnit.SECONDS);
	}

	/**
	 * 服务是否被限流
	 *
	 * @param key      自定义的key，请保证唯一
	 * @param max      支持的最大请求
	 * @param ttl      时间
	 * @param timeUnit 时间单位
	 * @return 是否允许
	 */
	boolean isAllowed(String key, long max, long ttl, TimeUnit timeUnit);

	/**
	 * 服务限流，被限制时抛出 RateLimiterException 异常，需要自行处理异常
	 *
	 * @param key      自定义的key，请保证唯一
	 * @param max      支持的最大请求
	 * @param ttl      时间
	 * @param supplier Supplier 函数式
	 * @return 函数执行结果
	 */
	default <T> T allow(String key, long max, long ttl, CheckedSupplier<T> supplier) {
		return allow(key, max, ttl, TimeUnit.SECONDS, supplier);
	}

	/**
	 * 服务限流，被限制时抛出 RateLimiterException 异常，需要自行处理异常
	 *
	 * @param key      自定义的key，请保证唯一
	 * @param max      支持的最大请求
	 * @param ttl      时间
	 * @param supplier Supplier 函数式
	 * @return 函数执行结果
	 */
	default <T> T allow(String key, long max, long ttl, TimeUnit timeUnit, CheckedSupplier<T> supplier) {
		boolean isAllowed = this.isAllowed(key, max, ttl, timeUnit);
		if (isAllowed) {
			try {
				return supplier.get();
			} catch (Throwable e) {
				throw Exceptions.unchecked(e);
			}
		}
		throw new RateLimiterException(key, max, ttl, timeUnit);
	}
}
