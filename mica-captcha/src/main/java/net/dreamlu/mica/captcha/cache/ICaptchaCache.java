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

package net.dreamlu.mica.captcha.cache;

import net.dreamlu.mica.captcha.core.CaptchaUtil;
import net.dreamlu.mica.core.utils.CharPool;
import org.springframework.lang.Nullable;

/**
 * 验证码缓存
 *
 * <p>
 * 1.单服务可以采用 guava、ehcache、caffeine 等内存缓存。
 * 2.分布式下可以使用 redis 等。
 * </p>
 *
 * @author L.cm
 */
public interface ICaptchaCache {

	/**
	 * 保存缓存
	 *
	 * <p>
	 *     非 spring cache 等启动就确定超时的缓存，重新改方法
	 * </p>
	 *
	 * @param cacheKey    缓存key
	 * @param value       缓存value
	 * @param ttlInMillis ttl
	 */
	default void put(String cacheKey, @Nullable String value, long ttlInMillis) {

	}

	/**
	 * 保存缓存
	 *
	 * @param cacheName 缓存空间
	 * @param uuid      验证码 uuid
	 * @param value     缓存value
	 */
	default void put(String cacheName, String uuid, @Nullable String value) {
		long ttlInMillis = CaptchaUtil.getTTLFormCacheName(cacheName);
		String cacheKey = cacheName + CharPool.COLON + uuid;
		put(cacheKey, value, ttlInMillis);
	}

	/**
	 * 获取并删除缓存，验证码不管成功只能验证一次
	 *
	 * <p>
	 *     非 spring cache 等启动就确定超时的缓存，重新改方法
	 * </p>
	 *
	 * @param cacheKey 缓存空间
	 * @return 验证码
	 */
	@Nullable
	default String getAndRemove(String cacheKey) {
		return null;
	}

	/**
	 * 获取并删除缓存，验证码不管成功只能验证一次
	 *
	 * @param cacheName 缓存空间
	 * @param uuid      验证码 uuid
	 * @return 验证码
	 */
	@Nullable
	default String getAndRemove(String cacheName, String uuid) {
		String cacheKey = cacheName + CharPool.COLON + uuid;
		return getAndRemove(cacheKey);
	}

}
