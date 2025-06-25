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

package net.dreamlu.mica.caffeine.config;

import com.github.benmanes.caffeine.cache.*;
import net.dreamlu.mica.core.utils.StringPool;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * caffeine 缓存自动配置超时时间
 *
 * @author L.cm
 */
public class CaffeineAutoCacheManager extends CaffeineCacheManager {
	private static final VarHandle CACHE_LOADER_VAR_HANDLE;

	static {
		try {
			// 1. 获取 MethodHandles.Lookup 实例（需要模块访问权限）
			MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(CaffeineCacheManager.class, MethodHandles.lookup());
			// 2. 找到字段并创建 VarHandle
			Field cacheLoaderField = CaffeineCacheManager.class.getDeclaredField("cacheLoader");
			CACHE_LOADER_VAR_HANDLE = lookup.unreflectVarHandle(cacheLoaderField);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			throw new IllegalStateException("Failed to initialize CaffeineAutoCacheManager", e);
		}
	}

	@Nullable
	private CaffeineSpec caffeineSpec = null;

	public CaffeineAutoCacheManager() {
		super();
	}

	public CaffeineAutoCacheManager(String... cacheNames) {
		super(cacheNames);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	protected AsyncCacheLoader<Object, Object> getCacheLoader() {
		return (AsyncCacheLoader<Object, Object>) CACHE_LOADER_VAR_HANDLE.get(this);
	}

	@Override
	public void setCaffeine(Caffeine<Object, Object> caffeine) {
		throw new IllegalArgumentException("mica-caffeine not support customization Caffeine bean，you can customize CaffeineSpec bean.");
	}

	@Override
	public void setCaffeineSpec(CaffeineSpec caffeineSpec) {
		super.setCaffeineSpec(caffeineSpec);
		this.caffeineSpec = caffeineSpec;
	}

	@Override
	public void setCacheSpecification(String cacheSpecification) {
		super.setCacheSpecification(cacheSpecification);
		this.caffeineSpec = CaffeineSpec.parse(cacheSpecification);
	}

	/**
	 * Build a common Caffeine Cache instance for the specified cache name,
	 * using the common Caffeine configuration specified on this cache manager.
	 *
	 * @param name the name of the cache
	 * @return the native Caffeine Cache instance
	 * @see #createCaffeineCache
	 */
	@Override
	protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
		String[] cacheArray = name.split(StringPool.HASH);
		if (cacheArray.length < 2) {
			return super.createNativeCaffeineCache(name);
		}
		Caffeine<Object, Object> cacheBuilder = getCaffeine(cacheArray);
		AsyncCacheLoader<Object, Object> cacheLoader = getCacheLoader();
		if (cacheLoader == null) {
			return cacheBuilder.build();
		} else if (cacheLoader instanceof CacheLoader<Object, Object> regularCacheLoader) {
			return cacheBuilder.build(regularCacheLoader);
		} else {
			throw new IllegalStateException("Cannot create regular Caffeine Cache with async-only cache loader: " + cacheLoader);
		}
	}

	@Override
	protected AsyncCache<Object, Object> createAsyncCaffeineCache(String name) {
		String[] cacheArray = name.split(StringPool.HASH);
		if (cacheArray.length < 2) {
			return super.createAsyncCaffeineCache(name);
		}
		Caffeine<Object, Object> cacheBuilder = getCaffeine(cacheArray);
		AsyncCacheLoader<Object, Object> cacheLoader = getCacheLoader();
		return cacheLoader == null ? cacheBuilder.buildAsync() : cacheBuilder.buildAsync(cacheLoader) ;
	}

	private Caffeine<Object, Object> getCaffeine(String[] cacheArray) {
		// 转换时间，支持时间单位例如：300ms，第二个参数是默认单位
		Duration duration = DurationStyle.detectAndParse(cacheArray[1], ChronoUnit.SECONDS);
		if (this.caffeineSpec == null) {
			return Caffeine.newBuilder().expireAfterAccess(duration);
		} else {
			return Caffeine.from(caffeineSpec).expireAfterAccess(duration);
		}
	}
}
