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

import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.binder.BaseUnits;
import io.undertow.Undertow;
import io.undertow.server.ConnectorStatistics;
import io.undertow.server.session.SessionManagerStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
import org.springframework.boot.web.embedded.undertow.UndertowWebServer;
import org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.xnio.management.XnioWorkerMXBean;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Undertow Metrics
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class UndertowMetrics implements ApplicationListener<ApplicationStartedEvent> {
	/**
	 * Prefix used for all Undertow metric names.
	 */
	public static final String UNDERTOW_METRIC_NAME_PREFIX = "undertow";
	/**
	 * XWorker
	 */
	private static final String METRIC_NAME_X_WORK_WORKER_POOL_CORE_SIZE 		= UNDERTOW_METRIC_NAME_PREFIX + ".xwork.worker.pool.core.size";
	private static final String METRIC_NAME_X_WORK_WORKER_POOL_MAX_SIZE 		= UNDERTOW_METRIC_NAME_PREFIX + ".xwork.worker.pool.max.size";
	private static final String METRIC_NAME_X_WORK_WORKER_POOL_SIZE 			= UNDERTOW_METRIC_NAME_PREFIX + ".xwork.worker.pool.size";
	private static final String METRIC_NAME_X_WORK_WORKER_THREAD_BUSY_COUNT 	= UNDERTOW_METRIC_NAME_PREFIX + ".xwork.worker.thread.busy.count";
	private static final String METRIC_NAME_X_WORK_IO_THREAD_COUNT 				= UNDERTOW_METRIC_NAME_PREFIX + ".xwork.io.thread.count";
	private static final String METRIC_NAME_X_WORK_WORKER_QUEUE_SIZE 			= UNDERTOW_METRIC_NAME_PREFIX + ".xwork.worker.queue.size";
	/**
	 * connectors
	 */
	private static final String METRIC_NAME_CONNECTORS_REQUESTS_COUNT 			= UNDERTOW_METRIC_NAME_PREFIX + ".connectors.requests.count";
	private static final String METRIC_NAME_CONNECTORS_REQUESTS_ERROR_COUNT 	= UNDERTOW_METRIC_NAME_PREFIX + ".connectors.requests.error.count";
	private static final String METRIC_NAME_CONNECTORS_REQUESTS_ACTIVE 			= UNDERTOW_METRIC_NAME_PREFIX + ".connectors.requests.active";
	private static final String METRIC_NAME_CONNECTORS_REQUESTS_ACTIVE_MAX 		= UNDERTOW_METRIC_NAME_PREFIX + ".connectors.requests.active.max";
	private static final String METRIC_NAME_CONNECTORS_BYTES_SENT 				= UNDERTOW_METRIC_NAME_PREFIX + ".connectors.bytes.sent";
	private static final String METRIC_NAME_CONNECTORS_BYTES_RECEIVED 			= UNDERTOW_METRIC_NAME_PREFIX + ".connectors.bytes.received";
	private static final String METRIC_NAME_CONNECTORS_PROCESSING_TIME 			= UNDERTOW_METRIC_NAME_PREFIX + ".connectors.processing.time";
	private static final String METRIC_NAME_CONNECTORS_PROCESSING_TIME_MAX 		= UNDERTOW_METRIC_NAME_PREFIX + ".connectors.processing.time.max";
	private static final String METRIC_NAME_CONNECTORS_CONNECTIONS_ACTIVE 		= UNDERTOW_METRIC_NAME_PREFIX + ".connectors.connections.active";
	private static final String METRIC_NAME_CONNECTORS_CONNECTIONS_ACTIVE_MAX 	= UNDERTOW_METRIC_NAME_PREFIX + ".connectors.connections.active.max";
	/**
	 * session
	 */
	private static final String METRIC_NAME_SESSIONS_ACTIVE_MAX 				= UNDERTOW_METRIC_NAME_PREFIX + ".sessions.active.max";
	private static final String METRIC_NAME_SESSIONS_ACTIVE_CURRENT 			= UNDERTOW_METRIC_NAME_PREFIX + ".sessions.active.current";
	private static final String METRIC_NAME_SESSIONS_CREATED 					= UNDERTOW_METRIC_NAME_PREFIX + ".sessions.created";
	private static final String METRIC_NAME_SESSIONS_EXPIRED 					= UNDERTOW_METRIC_NAME_PREFIX + ".sessions.expired";
	private static final String METRIC_NAME_SESSIONS_REJECTED 					= UNDERTOW_METRIC_NAME_PREFIX + ".sessions.rejected";
	private static final String METRIC_NAME_SESSIONS_ALIVE_MAX 					= UNDERTOW_METRIC_NAME_PREFIX + ".sessions.alive.max";

	private static final Field UNDERTOW_FIELD;
	private final Iterable<Tag> tags;

	public UndertowMetrics() {
		this.tags = Collections.emptyList();
	}

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		ConfigurableApplicationContext applicationContext = event.getApplicationContext();
		// find UndertowWebServer
		UndertowWebServer undertowWebServer = findUndertowWebServer(applicationContext);
		if (undertowWebServer == null) {
			return;
		}
		Undertow undertow = getUndertow(undertowWebServer);
		XnioWorkerMXBean xWorker = undertow.getWorker().getMXBean();
		MeterRegistry registry = applicationContext.getBean(MeterRegistry.class);
		// xWorker 指标
		registerXWorker(registry, xWorker);
		// 连接信息指标
		List<Undertow.ListenerInfo> listenerInfoList = undertow.getListenerInfo();
		listenerInfoList.forEach(listenerInfo -> registerConnectorStatistics(registry, listenerInfo));
		// 如果是 web 监控，添加 session 指标
		if (undertowWebServer instanceof UndertowServletWebServer) {
			SessionManagerStatistics statistics = ((UndertowServletWebServer) undertowWebServer).getDeploymentManager()
				.getDeployment()
				.getSessionManager()
				.getStatistics();
			registerSessionStatistics(registry, statistics);
		}
	}

	private void registerXWorker(MeterRegistry registry, XnioWorkerMXBean workerMXBean) {
		Gauge.builder(METRIC_NAME_X_WORK_WORKER_POOL_CORE_SIZE, workerMXBean, XnioWorkerMXBean::getCoreWorkerPoolSize)
			.description("XWork core worker pool size")
			.tags(tags)
			.tag("name", workerMXBean.getName())
			.register(registry);
		Gauge.builder(METRIC_NAME_X_WORK_WORKER_POOL_MAX_SIZE, workerMXBean, XnioWorkerMXBean::getMaxWorkerPoolSize)
			.description("XWork max worker pool size")
			.tags(tags)
			.tag("name", workerMXBean.getName())
			.register(registry);
		Gauge.builder(METRIC_NAME_X_WORK_WORKER_POOL_SIZE, workerMXBean, XnioWorkerMXBean::getWorkerPoolSize)
			.description("XWork worker pool size")
			.tags(tags)
			.tag("name", workerMXBean.getName())
			.register(registry);
		Gauge.builder(METRIC_NAME_X_WORK_WORKER_THREAD_BUSY_COUNT, workerMXBean, XnioWorkerMXBean::getBusyWorkerThreadCount)
			.description("XWork busy worker thread count")
			.tags(tags)
			.tag("name", workerMXBean.getName())
			.register(registry);
		Gauge.builder(METRIC_NAME_X_WORK_IO_THREAD_COUNT, workerMXBean, XnioWorkerMXBean::getIoThreadCount)
			.description("XWork io thread count")
			.tags(tags)
			.tag("name", workerMXBean.getName())
			.register(registry);
		Gauge.builder(METRIC_NAME_X_WORK_WORKER_QUEUE_SIZE, workerMXBean, XnioWorkerMXBean::getWorkerQueueSize)
			.description("XWork worker queue size")
			.tags(tags)
			.tag("name", workerMXBean.getName())
			.register(registry);
	}

	private void registerConnectorStatistics(MeterRegistry registry, Undertow.ListenerInfo listenerInfo) {
		String protocol = listenerInfo.getProtcol();
		ConnectorStatistics statistics = listenerInfo.getConnectorStatistics();
		Gauge.builder(METRIC_NAME_CONNECTORS_REQUESTS_COUNT, statistics, ConnectorStatistics::getRequestCount)
			.tags(tags)
			.tag("protocol", protocol)
			.register(registry);
		Gauge.builder(METRIC_NAME_CONNECTORS_REQUESTS_ERROR_COUNT, statistics, ConnectorStatistics::getErrorCount)
			.tags(tags)
			.tag("protocol", protocol)
			.register(registry);
		Gauge.builder(METRIC_NAME_CONNECTORS_REQUESTS_ACTIVE, statistics, ConnectorStatistics::getActiveRequests)
			.tags(tags)
			.tag("protocol", protocol)
			.baseUnit(BaseUnits.CONNECTIONS)
			.register(registry);
		Gauge.builder(METRIC_NAME_CONNECTORS_REQUESTS_ACTIVE_MAX, statistics, ConnectorStatistics::getMaxActiveRequests)
			.tags(tags)
			.tag("protocol", protocol)
			.baseUnit(BaseUnits.CONNECTIONS)
			.register(registry);

		Gauge.builder(METRIC_NAME_CONNECTORS_BYTES_SENT, statistics, ConnectorStatistics::getBytesSent)
			.tags(tags)
			.tag("protocol", protocol)
			.baseUnit(BaseUnits.BYTES)
			.register(registry);
		Gauge.builder(METRIC_NAME_CONNECTORS_BYTES_RECEIVED, statistics, ConnectorStatistics::getBytesReceived)
			.tags(tags)
			.tag("protocol", protocol)
			.baseUnit(BaseUnits.BYTES)
			.register(registry);

		Gauge.builder(METRIC_NAME_CONNECTORS_PROCESSING_TIME, statistics, (s) -> TimeUnit.NANOSECONDS.toMillis(s.getProcessingTime()))
			.tags(tags)
			.tag("protocol", protocol)
			.baseUnit(BaseUnits.MILLISECONDS)
			.register(registry);
		Gauge.builder(METRIC_NAME_CONNECTORS_PROCESSING_TIME_MAX, statistics, (s) -> TimeUnit.NANOSECONDS.toMillis(s.getMaxProcessingTime()))
			.tags(tags)
			.tag("protocol", protocol)
			.baseUnit(BaseUnits.MILLISECONDS)
			.register(registry);

		Gauge.builder(METRIC_NAME_CONNECTORS_CONNECTIONS_ACTIVE, statistics, ConnectorStatistics::getActiveConnections)
			.tags(tags)
			.tag("protocol", protocol)
			.baseUnit(BaseUnits.CONNECTIONS)
			.register(registry);
		Gauge.builder(METRIC_NAME_CONNECTORS_CONNECTIONS_ACTIVE_MAX, statistics, ConnectorStatistics::getMaxActiveConnections)
			.tags(tags)
			.tag("protocol", protocol)
			.baseUnit(BaseUnits.CONNECTIONS)
			.register(registry);
	}

	private void registerSessionStatistics(MeterRegistry registry, SessionManagerStatistics statistics) {
		Gauge.builder(METRIC_NAME_SESSIONS_ACTIVE_MAX, statistics, SessionManagerStatistics::getMaxActiveSessions)
			.tags(tags)
			.baseUnit(BaseUnits.SESSIONS)
			.register(registry);

		Gauge.builder(METRIC_NAME_SESSIONS_ACTIVE_CURRENT, statistics, SessionManagerStatistics::getActiveSessionCount)
			.tags(tags)
			.baseUnit(BaseUnits.SESSIONS)
			.register(registry);

		FunctionCounter.builder(METRIC_NAME_SESSIONS_CREATED, statistics, SessionManagerStatistics::getCreatedSessionCount)
			.tags(tags)
			.baseUnit(BaseUnits.SESSIONS)
			.register(registry);

		FunctionCounter.builder(METRIC_NAME_SESSIONS_EXPIRED, statistics, SessionManagerStatistics::getExpiredSessionCount)
			.tags(tags)
			.baseUnit(BaseUnits.SESSIONS)
			.register(registry);

		FunctionCounter.builder(METRIC_NAME_SESSIONS_REJECTED, statistics, SessionManagerStatistics::getRejectedSessions)
			.tags(tags)
			.baseUnit(BaseUnits.SESSIONS)
			.register(registry);

		TimeGauge.builder(METRIC_NAME_SESSIONS_ALIVE_MAX, statistics, TimeUnit.SECONDS, SessionManagerStatistics::getHighestSessionCount)
			.tags(tags)
			.register(registry);
	}

	static {
		UNDERTOW_FIELD = ReflectionUtils.findField(UndertowWebServer.class, "undertow");
		Objects.requireNonNull(UNDERTOW_FIELD, "UndertowWebServer class field undertow not exist.");
		ReflectionUtils.makeAccessible(UNDERTOW_FIELD);
	}

	private static Undertow getUndertow(UndertowWebServer undertowWebServer) {
		return (Undertow) ReflectionUtils.getField(UNDERTOW_FIELD, undertowWebServer);
	}

	private static UndertowWebServer findUndertowWebServer(ConfigurableApplicationContext applicationContext) {
		WebServer webServer;
		if (applicationContext instanceof ReactiveWebServerApplicationContext) {
			webServer = ((ReactiveWebServerApplicationContext) applicationContext).getWebServer();
		} else if (applicationContext instanceof ServletWebServerApplicationContext) {
			webServer = ((ServletWebServerApplicationContext) applicationContext).getWebServer();
		} else {
			return null;
		}
		if (webServer instanceof UndertowWebServer) {
			return (UndertowWebServer) webServer;
		}
		return null;
	}

}
