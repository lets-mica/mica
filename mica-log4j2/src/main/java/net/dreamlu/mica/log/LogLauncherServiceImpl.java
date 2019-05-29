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

import com.google.auto.service.AutoService;
import net.dreamlu.mica.launcher.LauncherService;
import net.dreamlu.mica.launcher.MicaEnv;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

/**
 * 日志启动器
 *
 * @author L.cm
 */
@AutoService(LauncherService.class)
public class LogLauncherServiceImpl implements LauncherService {

	@Override
	public void launcher(SpringApplicationBuilder builder, Environment env, String appName, MicaEnv micaEnv, boolean isLocalDev) {
		// 读取系统配置的日志目录，默认为项目下 logs
		String logBase = env.getProperty("LOGGING_PATH", "logs");
		// 用于 spring boot admin 中展示日志
		System.setProperty("logging.file", String.format("%s/%s/info.log", logBase, appName));
		// 配置区分环境的日志
		System.setProperty("logging.config", String.format("classpath:log/log4j2_%s.xml", micaEnv.getLogFileLevel()));
		// RocketMQ-Client 4.2.0 Log4j2 配置文件冲突问题解决：https://www.jianshu.com/p/b30ae6dd3811
		System.setProperty("rocketmq.client.log.loadconfig", "false");
		//  RocketMQ-Client 4.3 设置默认为 slf4j
		System.setProperty("rocketmq.client.logUseSlf4j", "true");
		// 非本地 将 全部的 System.err 和 System.out 替换为log
		if (!isLocalDev) {
			System.setOut(LogPrintStream.out());
			System.setErr(LogPrintStream.err());
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
