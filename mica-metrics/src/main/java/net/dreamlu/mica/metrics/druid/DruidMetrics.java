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

package net.dreamlu.mica.metrics.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.JdbcConnectionStat;
import com.alibaba.druid.stat.JdbcDataSourceStat;
import io.micrometer.core.instrument.FunctionTimer;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * druid Metrics
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class DruidMetrics implements MeterBinder {
	/**
	 * Prefix used for all Druid metric names.
	 */
	public static final String HIKARI_METRIC_NAME_PREFIX = "druid";

	private static final String METRIC_CATEGORY = "name";
	private static final String METRIC_NAME_CONNECT_TIME_MAX = HIKARI_METRIC_NAME_PREFIX + "_connections_connect_time_max";
	private static final String METRIC_NAME_ALIVE_TIME_MAX = HIKARI_METRIC_NAME_PREFIX + "_connections_alive_time_max";
	private static final String METRIC_NAME_ALIVE_TIME_MIN = HIKARI_METRIC_NAME_PREFIX + "_connections_alive_time_min";

	private static final String METRIC_NAME_CONNECT_COUNT = HIKARI_METRIC_NAME_PREFIX + "_connections_connect_count";
	private static final String METRIC_NAME_ACTIVE_COUNT = HIKARI_METRIC_NAME_PREFIX + "_connections_active_count";
	private static final String METRIC_NAME_CLOSE_COUNT = HIKARI_METRIC_NAME_PREFIX + "_connections_close_count";
	private static final String METRIC_NAME_ERROR_COUNT = HIKARI_METRIC_NAME_PREFIX + "_connections_error_count";
	private static final String METRIC_NAME_CONNECT_ERROR_COUNT = HIKARI_METRIC_NAME_PREFIX + "_connections_connect_error_count";
	private static final String METRIC_NAME_COMMIT_COUNT = HIKARI_METRIC_NAME_PREFIX + "_connections_commit_count";
	private static final String METRIC_NAME_ROLLBACK_COUNT = HIKARI_METRIC_NAME_PREFIX + "_connections_rollback_count";

	private final Map<String, DruidDataSource> druidDataSourceMap;
	private final Iterable<Tag> tags;

	public DruidMetrics(Map<String, DruidDataSource> druidDataSourceMap) {
		this(druidDataSourceMap, Collections.emptyList());
	}

	@Override
	public void bindTo(MeterRegistry meterRegistry) {
		druidDataSourceMap.forEach((name, dataSource) -> {
			JdbcDataSourceStat dsStats = dataSource.getDataSourceStat();
			JdbcConnectionStat connectionStat = dsStats.getConnectionStat();
			// time
			FunctionTimer.builder(METRIC_NAME_CONNECT_TIME_MAX, connectionStat, (x) -> 1L, JdbcConnectionStat::getConnectMillisMax, TimeUnit.MILLISECONDS)
				.description("Connection connect max time")
				.tags(tags)
				.tag(METRIC_CATEGORY, name)
				.register(meterRegistry);
			FunctionTimer.builder(METRIC_NAME_ALIVE_TIME_MAX, connectionStat, (x) -> 1L, JdbcConnectionStat::getAliveMillisMax, TimeUnit.MILLISECONDS)
				.description("Connection alive max time")
				.tags(tags)
				.tag(METRIC_CATEGORY, name)
				.register(meterRegistry);
			FunctionTimer.builder(METRIC_NAME_ALIVE_TIME_MIN, connectionStat, (x) -> 1L, JdbcConnectionStat::getAliveMillisMin, TimeUnit.MILLISECONDS)
				.description("Connection alive min time")
				.tags(tags)
				.tag(METRIC_CATEGORY, name)
				.register(meterRegistry);
			// count
			Gauge.builder(METRIC_NAME_ACTIVE_COUNT, connectionStat, JdbcConnectionStat::getActiveCount)
				.description("Connection active count")
				.tags(tags)
				.tag(METRIC_CATEGORY, name)
				.register(meterRegistry);
			Gauge.builder(METRIC_NAME_CONNECT_COUNT, connectionStat, JdbcConnectionStat::getConnectCount)
				.description("Connection connect count")
				.tags(tags)
				.tag(METRIC_CATEGORY, name)
				.register(meterRegistry);
			Gauge.builder(METRIC_NAME_CLOSE_COUNT, connectionStat, JdbcConnectionStat::getCloseCount)
				.description("Connection close count")
				.tags(tags)
				.tag(METRIC_CATEGORY, name)
				.register(meterRegistry);
			Gauge.builder(METRIC_NAME_ERROR_COUNT, connectionStat, JdbcConnectionStat::getErrorCount)
				.description("Connection error count")
				.tags(tags)
				.tag(METRIC_CATEGORY, name)
				.register(meterRegistry);
			Gauge.builder(METRIC_NAME_CONNECT_ERROR_COUNT, connectionStat, JdbcConnectionStat::getConnectErrorCount)
				.description("Connection connect error count")
				.tags(tags)
				.tag(METRIC_CATEGORY, name)
				.register(meterRegistry);
			Gauge.builder(METRIC_NAME_COMMIT_COUNT, connectionStat, JdbcConnectionStat::getCommitCount)
				.description("Connecting commit count")
				.tags(tags)
				.tag(METRIC_CATEGORY, name)
				.register(meterRegistry);
			Gauge.builder(METRIC_NAME_ROLLBACK_COUNT, connectionStat, JdbcConnectionStat::getRollbackCount)
				.description("Connection rollback count")
				.tags(tags)
				.tag(METRIC_CATEGORY, name)
				.register(meterRegistry);
		});
	}
}
