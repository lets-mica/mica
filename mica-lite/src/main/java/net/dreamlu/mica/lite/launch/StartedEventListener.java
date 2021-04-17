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

package net.dreamlu.mica.lite.launch;

import net.dreamlu.mica.core.constant.MicaConstant;
import net.dreamlu.mica.core.log.LogPrintStream;
import net.dreamlu.mica.core.utils.SystemUtil;
import org.springframework.boot.web.context.WebServerApplicationContext;
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
@Configuration(proxyBeanMethods = false)
public class StartedEventListener {

	@Async
	@Order(Ordered.LOWEST_PRECEDENCE - 1)
	@EventListener(WebServerInitializedEvent.class)
	public void afterStart(WebServerInitializedEvent event) {
		WebServerApplicationContext context = event.getApplicationContext();
		Environment environment = context.getEnvironment();
		String appName = environment.getRequiredProperty(MicaConstant.SPRING_APP_NAME_KEY);
		int localPort = event.getWebServer().getPort();
		String profile = StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles());
		System.err.printf("---[%s]---启动完成，当前使用的端口:[%d]，环境变量:[%s]---%n", appName, localPort, profile);
		// 如果有 swagger，打印开发阶段的 swagger ui 地址
		if (ClassUtils.isPresent("springfox.documentation.spring.web.plugins.Docket", null)) {
			System.out.printf("http://localhost:%s/doc.html%n", localPort);
		} else {
			System.out.printf("http://localhost:%s%n", localPort);
		}
		// linux 上将全部的 System.err 和 System.out 替换为log
		if (SystemUtil.isLinux()) {
			System.setOut(LogPrintStream.log(false));
			// 去除 error 的转换，因为 error 会打印成很 N 条
			// System.setErr(LogPrintStream.log(true));
		}
	}

}
