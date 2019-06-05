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

package net.dreamlu.mica.log;

import net.dreamlu.mica.launcher.MicaLogLevel;
import net.dreamlu.mica.props.MicaProperties;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * 项目启动事件通知
 *
 * @author L.cm
 */
@Configuration
public class StartedEventListener {

	@Async
	@Order
	@EventListener(WebServerInitializedEvent.class)
	public void afterStart(WebServerInitializedEvent event) {
		WebServerApplicationContext applicationContext = event.getApplicationContext();

		// 非本地 将 全部的 System.err 和 System.out 替换为log
		MicaProperties micaProperties = applicationContext.getBean(MicaProperties.class);
		if (!micaProperties.getIsLocal()) {
			System.setOut(LogPrintStream.out());
			System.setErr(LogPrintStream.err());
		}

		ConfigurableEnvironment environment = (ConfigurableEnvironment) applicationContext.getEnvironment();
		// 如果用户设置过则遵从用户的配置规则
		if (!environment.containsProperty(MicaLogLevel.CONSOLE_LOG_ENABLED_PROP)) {
			// 关闭控制台的日志打印
			Map<String, Object> systemProperties = environment.getSystemProperties();
			systemProperties.put(MicaLogLevel.CONSOLE_LOG_ENABLED_PROP, false);
		}
	}
}
