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

package net.dreamlu.mica.metrics.undertow;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.FunctionTimer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.TimeGauge;
import io.undertow.server.handlers.MetricsHandler;
import io.undertow.servlet.api.MetricsCollector;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;

import static io.undertow.server.handlers.MetricsHandler.MetricResult;

/**
 * Undertow Metrics
 *
 * @author L.cm
 */
public class UndertowMetrics implements MetricsCollector, ApplicationListener<ApplicationStartedEvent> {
	private final Map<String, MetricsHandler> metricsHandlers;

	public UndertowMetrics() {
		metricsHandlers = new HashMap<>();
	}

	@Override
	public void registerMetric(String servletName, MetricsHandler handler) {
		metricsHandlers.put(servletName, handler);
	}

	private void bindTimer(MeterRegistry registry, String name, String desc, String servletName, MetricResult metricResult, ToLongFunction<MetricResult> countFunc, ToDoubleFunction<MetricResult> consumer) {
		FunctionTimer.builder(name, metricResult, countFunc, consumer, TimeUnit.MILLISECONDS)
			.tag("servlet", servletName)
			.description(desc)
			.register(registry);
	}

	private void bindTimeGauge(MeterRegistry registry, String name, String desc, String servletName, MetricResult metricResult, ToDoubleFunction<MetricResult> consumer) {
		TimeGauge.builder(name, metricResult, TimeUnit.MILLISECONDS, consumer)
			.tag("servlet", servletName)
			.description(desc)
			.register(registry);
	}

	private void bindCounter(MeterRegistry registry, String name, String desc, String servletName, MetricResult metricResult, ToDoubleFunction<MetricResult> consumer) {
		FunctionCounter.builder(name, metricResult, consumer)
			.tag("servlet", servletName)
			.description(desc)
			.register(registry);
	}

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		MeterRegistry registry = event.getApplicationContext().getBean(MeterRegistry.class);
		for (Map.Entry<String, MetricsHandler> handlerEntry : metricsHandlers.entrySet()) {
			MetricResult metricResult = handlerEntry.getValue().getMetrics();
			String servletName = handlerEntry.getKey();
			bindTimer(registry, "undertow_requests", "Request duration", servletName, metricResult, MetricResult::getTotalRequests, MetricsHandler.MetricResult::getTotalRequestTime);
			bindTimeGauge(registry, "undertow_request_time_max", "Maximum time spent handling requests", servletName, metricResult, MetricResult::getMaxRequestTime);
			bindTimeGauge(registry, "undertow_request_time_min", "Minimum time spent handling requests", servletName, metricResult, MetricResult::getMinRequestTime);
			bindCounter(registry, "undertow_request_total_errors", "Total number of error requests", servletName, metricResult, MetricResult::getTotalErrors);
		}
	}
}
