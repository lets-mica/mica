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

import net.dreamlu.mica.core.utils.CollectionUtil;
import net.dreamlu.mica.core.utils.SystemUtil;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

/**
 * 项目启动器，搞定环境变量问题
 *
 * @author L.cm
 */
public class MicaApplication {

	/**
	 * Create an application context
	 *
	 * @param appName application name
	 * @param source  The sources
	 * @param args    args the command line arguments
	 * @return an application context created from the current state
	 */
	public static ConfigurableApplicationContext run(String appName, Class source, String... args) {
		SpringApplicationBuilder builder = createSpringApplicationBuilder(appName, source, args);
		return builder.run(args);
	}

	/**
	 * Create an application context
	 *
	 * @param appName application name
	 * @param source  The sources
	 * @param args    args the command line arguments
	 * @return an application context created from the current state
	 */
	public static SpringApplicationBuilder createSpringApplicationBuilder(String appName, Class source, String... args) {
		Assert.hasText(appName, "args appName is blank");
		// 读取环境变量，使用spring boot的规则
		ConfigurableEnvironment env = new StandardEnvironment();
		MutablePropertySources propertySources = env.getPropertySources();
		propertySources.addFirst(new SimpleCommandLinePropertySource(args));
		propertySources.addLast(new MapPropertySource(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, env.getSystemProperties()));
		propertySources.addLast(new SystemEnvironmentPropertySource(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, env.getSystemEnvironment()));
		// 获取配置的环境变量
		String[] activeProfiles = env.getActiveProfiles();
		// 判断环境:dev、test、ontest、prod
		List<String> profiles = Arrays.asList(activeProfiles);
		// 预设的环境
		List<String> presetProfiles = MicaEnv.getEnvList();
		// 交集
		presetProfiles.retainAll(profiles);
		// 当前使用
		List<String> activeProfileList = new ArrayList<>(profiles);
		Function<Object[], String> joinFun = StringUtils::arrayToCommaDelimitedString;
		SpringApplicationBuilder builder = new SpringApplicationBuilder(source);
		String profile;
		if (presetProfiles.isEmpty()) {
			// 默认dev开发
			profile = MicaEnv.DEV.getName();
			activeProfileList.add(profile);
			builder.profiles(profile);
		} else if (presetProfiles.size() == 1) {
			profile = presetProfiles.get(0);
		} else {
			// 同时存在dev、test、ontest、prod环境时
			throw new IllegalArgumentException("同时存在环境变量:[" + joinFun.apply(activeProfiles) + "]");
		}
		// 添加启动目录打印
		String startJarPath = MicaApplication.class.getResource("/").getPath().split("!")[0];
		String activePros = joinFun.apply(activeProfileList.toArray());
		System.err.println(String.format("---[%s]---启动中，读取到的环境变量:[%s]，jar地址:[%s]---", appName, activePros, startJarPath));
		// 代码部署于 linux 上，工作默认为 mac 和 Windows
		boolean isLocalDev = SystemUtil.isLocalDev();
		// 设定到 sys 中供 log4j2 中使用 yml 配置无效
		System.setProperty("spring.application.name", appName);
		System.setProperty("mica.env", profile);
		// 默认的属性配置，级别低于 yml
		Properties defaultProperties = new Properties();
		defaultProperties.setProperty("mica.is-local", String.valueOf(isLocalDev));
		defaultProperties.setProperty("spring.banner.location", "classpath:banner.txt");
		// 预设请求日志级别
		MicaEnv micaEnv = MicaEnv.of(profile);
		// 使用 builder 的 props，优先级低，mica.log.request.level=xxx
		defaultProperties.setProperty(MicaLogLevel.REQ_LOG_PROPS_PREFIX + ".level", micaEnv.getReqLogLevel().name());
		// 加载自定义 SPI 组件
		ServiceLoader<LauncherService> loader = ServiceLoader.load(LauncherService.class);
		CollectionUtil.toList(loader).stream().sorted()
			.forEach(launcherService -> launcherService.launcher(builder, env, appName, micaEnv, isLocalDev));
		builder.properties(defaultProperties);
		return builder;
	}

}
