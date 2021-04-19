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

package net.dreamlu.mica.metrics.sentinel;

import com.alibaba.csp.sentinel.metric.extension.MetricExtension;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import net.dreamlu.mica.auto.annotation.AutoService;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Sentinel Metrics Extension
 *
 * @author L.cm
 */
@AutoService(MetricExtension.class)
public class SentinelMetricsExtension implements MetricExtension {
	/**
	 * Prefix used for all Sentinel metric names.
	 */
	public static final String SENTINEL_METRIC_NAME_PREFIX = "undertow";
	/**
	 * Metric name
	 */
	public static final String PASS_REQUESTS_TOTAL 		= SENTINEL_METRIC_NAME_PREFIX + ".pass.requests.total";
	public static final String BLOCK_REQUESTS_TOTAL 	= SENTINEL_METRIC_NAME_PREFIX + ".block.requests.total";
	public static final String SUCCESS_REQUESTS_TOTAL 	= SENTINEL_METRIC_NAME_PREFIX + ".success.requests.total";
	public static final String EXCEPTION_REQUESTS_TOTAL = SENTINEL_METRIC_NAME_PREFIX + ".exception_requests_total";
	public static final String REQUESTS_LATENCY_SECONDS = SENTINEL_METRIC_NAME_PREFIX + ".requests.latency.seconds";
	public static final String CURRENT_THREADS 			= SENTINEL_METRIC_NAME_PREFIX + ".current.threads";
	public static final String DEFAULT_TAT_NAME 		= "resource";
	private final AtomicLong CURRENT_THREAD_COUNT = new AtomicLong(0);

	@Override
	public void addPass(String resource, int n, Object... args) {
		Metrics.counter(PASS_REQUESTS_TOTAL, DEFAULT_TAT_NAME, resource).increment(n);
	}

	@Override
	public void addBlock(String resource, int n, String origin, BlockException ex, Object... args) {
		Metrics.counter(BLOCK_REQUESTS_TOTAL, resource, ex.getClass().getSimpleName(), ex.getRuleLimitApp(), origin).increment(n);
	}

	@Override
	public void addSuccess(String resource, int n, Object... args) {
		Metrics.counter(SUCCESS_REQUESTS_TOTAL, DEFAULT_TAT_NAME, resource).increment(n);
	}

	@Override
	public void addException(String resource, int n, Throwable throwable) {
		Metrics.counter(EXCEPTION_REQUESTS_TOTAL, DEFAULT_TAT_NAME, resource).increment(n);
	}

	@Override
	public void addRt(String resource, long rt, Object... args) {
		Metrics.timer(REQUESTS_LATENCY_SECONDS, DEFAULT_TAT_NAME, resource).record(rt, TimeUnit.MICROSECONDS);
	}

	@Override
	public void increaseThreadNum(String resource, Object... args) {
		Tags tags = Tags.of(DEFAULT_TAT_NAME, resource);
		Metrics.gauge(CURRENT_THREADS, tags, CURRENT_THREAD_COUNT, AtomicLong::incrementAndGet);
	}

	@Override
	public void decreaseThreadNum(String resource, Object... args) {
		Tags tags = Tags.of(DEFAULT_TAT_NAME, resource);
		Metrics.gauge(CURRENT_THREADS, tags, CURRENT_THREAD_COUNT, AtomicLong::decrementAndGet);
	}
}
