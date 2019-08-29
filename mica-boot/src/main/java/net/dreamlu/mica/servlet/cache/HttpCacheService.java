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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Objects;

/**
 * Http Cache 服务
 *
 * @author L.cm
 */
public class HttpCacheService implements InitializingBean {
	private final MicaHttpCacheProperties properties;
	private final CacheManager cacheManager;
	private Cache cache;

	public HttpCacheService(MicaHttpCacheProperties properties, CacheManager cacheManager) {
		this.properties = properties;
		this.cacheManager = cacheManager;
	}

	public boolean get(String key) {
		Boolean result = cache.get(key, Boolean.class);
		return Boolean.TRUE.equals(result);
	}

	public void set(String key) {
		cache.put(key, Boolean.TRUE);
	}

	public void remove(String key) {
		cache.evict(key);
	}

	public void clear() {
		cache.clear();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Objects.requireNonNull(cacheManager, "cacheManager must not be null!");
		String cacheName = properties.getCacheName();
		this.cache = cacheManager.getCache(cacheName);
		Objects.requireNonNull(this.cache, "HttpCacheCache cacheName: " + cacheName + " is not config.");
	}
}
