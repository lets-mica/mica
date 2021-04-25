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
import com.alicp.jetcache.anno.support.GlobalCacheConfig;
import com.alicp.jetcache.support.DefaultCacheMonitor;
import com.alicp.jetcache.support.DefaultMetricsManager;
import com.alicp.jetcache.support.StatInfo;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * JetCache CacheMonitorManager
 *
 * @author L.cm
 */
@SuppressWarnings("unchecked")
public class JetCacheMonitorManager implements CacheMonitorManager, InitializingBean, DisposableBean {
	private final DefaultMetricsManager defaultMetricsManager;

	public JetCacheMonitorManager(GlobalCacheConfig globalCacheConfig,
								  Consumer<StatInfo> metricsCallback) {
		this.defaultMetricsManager = new JetCacheMetricsManager(globalCacheConfig.getStatIntervalMinutes(), TimeUnit.MINUTES, metricsCallback);
	}

	@Override
	public void addMonitors(String area, String cacheName, Cache cache) {
		cache = CacheUtil.getAbstractCache(cache);
		if (cache instanceof MultiLevelCache) {
			MultiLevelCache mc = (MultiLevelCache) cache;
			if (mc.caches().length == 2) {
				Cache local = mc.caches()[0];
				Cache remote = mc.caches()[1];
				DefaultCacheMonitor localMonitor = new DefaultCacheMonitor(cacheName + "_local");
				local.config().getMonitors().add(localMonitor);
				DefaultCacheMonitor remoteMonitor = new DefaultCacheMonitor(cacheName + "_remote");
				remote.config().getMonitors().add(remoteMonitor);
				defaultMetricsManager.add(localMonitor, remoteMonitor);
			}
		}
		DefaultCacheMonitor monitor = new DefaultCacheMonitor(cacheName);
		cache.config().getMonitors().add(monitor);
		defaultMetricsManager.add(monitor);
	}

	@Override
	public void destroy() throws Exception {
		defaultMetricsManager.stop();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		defaultMetricsManager.start();
	}

}
