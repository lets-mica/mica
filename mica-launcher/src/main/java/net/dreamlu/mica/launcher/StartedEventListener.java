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

package net.dreamlu.mica.launcher;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * 项目启动事件通知
 *
 * @author L.cm
 */
@Configuration
public class StartedEventListener {

	@Async
	@Order(Ordered.LOWEST_PRECEDENCE -1)
	@EventListener(WebServerInitializedEvent.class)
	public void afterStart(WebServerInitializedEvent event) {
		Environment environment = event.getApplicationContext().getEnvironment();
		String appName = environment.getProperty("spring.application.name");
		int localPort = event.getWebServer().getPort();
		String profile = StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles());
		System.err.println(String.format("---[%s]---启动完成，当前使用的端口:[%d]，环境变量:[%s]---", appName, localPort, profile));
		// 如果有 swagger，打印开发阶段的 swagger ui 地址
		if (ClassUtils.isPresent("springfox.documentation.spring.web.plugins.Docket", null)) {
			System.out.println(String.format("http://localhost:%s/doc.html", localPort));
		} else {
			System.out.println(String.format("http://localhost:%s", localPort));
		}
	}
}
