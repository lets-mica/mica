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

import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.captcha.config.MicaCaptchaProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Objects;

/**
 * spring cache 的 captcha cache
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class SpringCacheCaptchaCache implements ICaptchaCache, InitializingBean {
	private final MicaCaptchaProperties properties;
	private final CacheManager cacheManager;

	@Override
	public void put(String cacheName, String uuid, String value) {
		Cache captchaCache = getCache(cacheName);
		captchaCache.put(uuid, value);
	}

	@Override
	public String getAndRemove(String cacheName, String uuid) {
		Cache captchaCache = getCache(cacheName);
		String value = captchaCache.get(uuid, String.class);
		if (value != null) {
			captchaCache.evict(uuid);
		}
		return value;
	}

	/**
	 * 发现 caffeine 中会刷新会导致引用为 null
	 *
	 * @return Cache
	 */
	private Cache getCache(String cacheName) {
		return cacheManager.getCache(cacheName);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String cacheName = properties.getCacheName();
		Cache cache = cacheManager.getCache(cacheName);
		Objects.requireNonNull(cache, "mica-captcha spring cache name " + cacheName + " is null.");
	}

}
