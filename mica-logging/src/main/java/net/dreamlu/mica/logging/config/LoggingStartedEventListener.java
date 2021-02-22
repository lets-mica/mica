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

package net.dreamlu.mica.logging.config;

import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.utils.ObjectUtil;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * 项目启动事件通知
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class LoggingStartedEventListener {
	private final MicaLoggingProperties properties;

	@Async
	@Order
	@EventListener(WebServerInitializedEvent.class)
	public void afterStart() {
		// 1. 如果开启 elk，关闭文件日志打印
		MicaLoggingProperties.Logstash logStash = properties.getLogstash();
		if (ObjectUtil.isTrue(logStash.isEnabled())) {
			LoggingUtil.detachAppender(LoggingUtil.FILE_APPENDER_NAME);
			LoggingUtil.detachAppender(LoggingUtil.FILE_ERROR_APPENDER_NAME);
		}
		// 2. 关闭控制台
		MicaLoggingProperties.Console console = properties.getConsole();
		if (ObjectUtil.isFalse(console.isEnabled())) {
			LoggingUtil.detachAppender(LoggingUtil.CONSOLE_APPENDER_NAME);
		}
	}
}
