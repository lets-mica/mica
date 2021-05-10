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

package net.dreamlu.mica.jetcache.metrics;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheUtil;
import com.alicp.jetcache.MultiLevelCache;
import com.alicp.jetcache.anno.support.CacheMonitorManager;
import com.alicp.jetcache.support.DefaultCacheMonitor;
import com.alicp.jetcache.support.DefaultMetricsManager;
import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;

/**
 * JetCache CacheMonitorManager
 *
 * @author L.cm
 */
@SuppressWarnings("unchecked")
public class JetCacheMonitorManager implements CacheMonitorManager, InitializingBean, DisposableBean {
	/**
	 * Prefix used for all jetcache metric names.
	 */
	public static final String JETCACHE_METRIC_NAME_PREFIX = "jetcache";
	/**
	 * metrics
	 */
	private static final String METRIC_NAME_JET_CACHE_CACHE_QPS = JETCACHE_METRIC_NAME_PREFIX + ".qps";
	private static final String METRIC_NAME_JET_CACHE_CACHE_RATE = JETCACHE_METRIC_NAME_PREFIX + ".rate";
	private static final String METRIC_NAME_JET_CACHE_CACHE_GET = JETCACHE_METRIC_NAME_PREFIX + ".get";
	private static final String METRIC_NAME_JET_CACHE_CACHE_HIT = JETCACHE_METRIC_NAME_PREFIX + ".hit";
	private static final String METRIC_NAME_JET_CACHE_CACHE_FAIL = JETCACHE_METRIC_NAME_PREFIX + ".fail";
	private static final String METRIC_NAME_JET_CACHE_CACHE_EXPIRE = JETCACHE_METRIC_NAME_PREFIX + ".expire";
	private static final String METRIC_NAME_JET_CACHE_CACHE_AVG_LOAD_TIME = JETCACHE_METRIC_NAME_PREFIX + ".avg.load.time";
	private static final String METRIC_NAME_JET_CACHE_CACHE_MAX_LOAD_TIME = JETCACHE_METRIC_NAME_PREFIX + ".max.load.time";

	@Nullable
	private final DefaultMetricsManager defaultMetricsManager;
	private final MeterRegistry meterRegistry;

	public JetCacheMonitorManager(@Nullable DefaultMetricsManager defaultMetricsManager,
								  MeterRegistry meterRegistry) {
		this.defaultMetricsManager = defaultMetricsManager;
		this.meterRegistry = meterRegistry;
	}

	@Override
	public void addMonitors(String area, String cacheName, Cache cache) {
		cache = CacheUtil.getAbstractCache(cache);
		if (cache instanceof MultiLevelCache) {
			MultiLevelCache mc = (MultiLevelCache) cache;
			if (mc.caches().length == 2) {
				// local cache
				Cache local = mc.caches()[0];
				String localCacheName = cacheName + "_local";
				DefaultCacheMonitor localMonitor = new DefaultCacheMonitor(localCacheName);
				local.config().getMonitors().add(localMonitor);
				registerMeters(meterRegistry, localCacheName, localMonitor);
				// remote cache
				Cache remote = mc.caches()[1];
				String remoteCacheName = cacheName + "_remote";
				DefaultCacheMonitor remoteMonitor = new DefaultCacheMonitor(cacheName + "_remote");
				remote.config().getMonitors().add(remoteMonitor);
				registerMeters(meterRegistry, remoteCacheName, remoteMonitor);
				if (defaultMetricsManager != null) {
					defaultMetricsManager.add(localMonitor, remoteMonitor);
				}
			}
		}
		DefaultCacheMonitor monitor = new DefaultCacheMonitor(cacheName);
		cache.config().getMonitors().add(monitor);
		registerMeters(meterRegistry, cacheName, monitor);
		if (defaultMetricsManager != null) {
			defaultMetricsManager.add(monitor);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (defaultMetricsManager != null) {
			defaultMetricsManager.start();
		}
	}

	@Override
	public void destroy() throws Exception {
		if (defaultMetricsManager != null) {
			defaultMetricsManager.stop();
		}
	}

	private static void registerMeters(MeterRegistry meterRegistry,
									   String cacheName,
									   DefaultCacheMonitor cacheMonitor) {
//		"qps", "rate", "get", "hit", "fail", "expire"
		Gauge.builder(METRIC_NAME_JET_CACHE_CACHE_QPS, cacheMonitor, (monitor) -> monitor.getCacheStat().qps())
			.description("JetCache qps")
			.tag("name", cacheName)
			.register(meterRegistry);
		Gauge.builder(METRIC_NAME_JET_CACHE_CACHE_RATE, cacheMonitor, (monitor) -> monitor.getCacheStat().hitRate())
			.description("JetCache rate")
			.tag("name", cacheName)
			.register(meterRegistry);
		FunctionCounter.builder(METRIC_NAME_JET_CACHE_CACHE_GET, cacheMonitor, (monitor) -> monitor.getCacheStat().getGetCount())
			.description("JetCache get")
			.tag("name", cacheName)
			.register(meterRegistry);
		FunctionCounter.builder(METRIC_NAME_JET_CACHE_CACHE_HIT, cacheMonitor, (monitor) -> monitor.getCacheStat().getGetHitCount())
			.description("JetCache hit")
			.tag("name", cacheName)
			.register(meterRegistry);
		FunctionCounter.builder(METRIC_NAME_JET_CACHE_CACHE_FAIL, cacheMonitor, (monitor) -> monitor.getCacheStat().getGetFailCount())
			.description("JetCache fail")
			.tag("name", cacheName)
			.register(meterRegistry);
		FunctionCounter.builder(METRIC_NAME_JET_CACHE_CACHE_EXPIRE, cacheMonitor, (monitor) -> monitor.getCacheStat().getGetExpireCount())
			.description("JetCache expire")
			.tag("name", cacheName)
			.register(meterRegistry);
//		"avgLoadTime", "maxLoadTime"
		Gauge.builder(METRIC_NAME_JET_CACHE_CACHE_AVG_LOAD_TIME, cacheMonitor, (monitor) -> monitor.getCacheStat().avgLoadTime())
			.description("JetCache avg load time")
			.tag("name", cacheName)
			.register(meterRegistry);
		Gauge.builder(METRIC_NAME_JET_CACHE_CACHE_MAX_LOAD_TIME, cacheMonitor, (monitor) -> monitor.getCacheStat().getMaxLoadTime())
			.description("JetCache max load time")
			.tag("name", cacheName)
			.register(meterRegistry);
	}

}
