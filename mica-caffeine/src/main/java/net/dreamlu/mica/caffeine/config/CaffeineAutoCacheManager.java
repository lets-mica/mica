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

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.dreamlu.mica.core.utils.ReflectUtil;
import net.dreamlu.mica.core.utils.StringPool;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * caffeine 缓存自动配置超时时间
 *
 * @author L.cm
 */
public class CaffeineAutoCacheManager extends CaffeineCacheManager {
    private static Field cacheBuilderField;
    private static Field cacheLoaderField;

    static {
        cacheBuilderField = Objects.requireNonNull(ReflectUtil.getField(CaffeineCacheManager.class, "cacheBuilder"));
        cacheBuilderField.setAccessible(true);
        cacheLoaderField = Objects.requireNonNull(ReflectUtil.getField(CaffeineCacheManager.class, "cacheLoader"));
        cacheLoaderField.setAccessible(true);
    }

    public CaffeineAutoCacheManager() {
        super();
    }

    public CaffeineAutoCacheManager(String... cacheNames) {
        super(cacheNames);
    }

    @SuppressWarnings("unchecked")
    protected Caffeine<Object, Object> getCacheBuilder() {
        try {
            return (Caffeine<Object, Object>) cacheBuilderField.get(this);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected CacheLoader<Object, Object> getCacheLoader() {
        try {
            return (CacheLoader<Object, Object>) cacheLoaderField.get(this);
        } catch (IllegalAccessException e) {
            return null;
        }
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
        // 转换时间，支持时间单位例如：300ms，第二个参数是默认单位
        Duration duration = DurationStyle.detectAndParse(cacheArray[1], ChronoUnit.SECONDS);
        Caffeine<Object, Object> cacheBuilder = getCacheBuilder();
        CacheLoader<Object, Object> cacheLoader = getCacheLoader();
        if (cacheLoader == null) {
            return cacheBuilder.expireAfterAccess(duration).build();
        } else {
            return cacheBuilder.expireAfterAccess(duration).build(cacheLoader);
        }
    }

}
