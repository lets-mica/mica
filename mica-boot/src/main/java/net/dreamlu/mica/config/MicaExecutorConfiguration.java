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

package net.dreamlu.mica.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.error.MicaErrorEvent;
import net.dreamlu.mica.core.utils.Exceptions;
import net.dreamlu.mica.props.MicaAsyncProperties;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步处理
 *
 * @author L.cm
 */
@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
@AllArgsConstructor
public class MicaExecutorConfiguration extends AsyncConfigurerSupport {
	private final MicaAsyncProperties properties;
	private final ApplicationEventPublisher publisher;

	@Override
	@Bean(name = "taskExecutor")
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(properties.getCorePoolSize());
		executor.setMaxPoolSize(properties.getMaxPoolSize());
		executor.setQueueCapacity(properties.getQueueCapacity());
		executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
		executor.setThreadNamePrefix("mica-async-executor-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new MicaAsyncExceptionHandler();
	}

	class MicaAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
		@Override
		public void handleUncaughtException(Throwable error, Method method, Object... params) {

			MicaErrorEvent event = new MicaErrorEvent();
			event.setStackTrace(Exceptions.getStackTraceAsString(error));
			event.setExceptionName(error.getClass().getName());
			event.setMessage(error.getMessage());
			StackTraceElement[] elements = error.getStackTrace();
			if (!ObjectUtils.isEmpty(elements)) {
				StackTraceElement element = elements[0];
				event.setClassName(element.getClassName());
				event.setFileName(element.getFileName());
				event.setMethodName(element.getMethodName());
				event.setLineNumber(element.getLineNumber());
			}s
			publisher.publishEvent(event);
		}
	}

}
