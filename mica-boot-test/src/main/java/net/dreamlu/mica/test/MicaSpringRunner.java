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

package net.dreamlu.mica.test;

import net.dreamlu.mica.core.utils.CollectionUtil;
import net.dreamlu.mica.core.utils.SystemUtil;
import net.dreamlu.mica.launcher.LauncherService;
import net.dreamlu.mica.launcher.MicaEnv;
import org.junit.runners.model.InitializationError;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

/**
 * 设置启动参数
 *
 * @author L.cm
 */
public class MicaSpringRunner extends SpringJUnit4ClassRunner {

	public MicaSpringRunner(Class<?> clazz) throws InitializationError, NoSuchFieldException, IllegalAccessException {
		super(clazz);
		setUpTestClass(clazz);
	}

	private void setUpTestClass(Class<?> clazz) throws NoSuchFieldException, IllegalAccessException {
		MicaBootTest micaBootTest = AnnotationUtils.getAnnotation(clazz, MicaBootTest.class);
		if (micaBootTest == null) {
			throw new MicaBootTestException(String.format("%s must be @MicaBootTest .", clazz));
		}
		String appName = micaBootTest.appName();
		String profile = micaBootTest.profile();
		boolean isLocalDev = SystemUtil.isLocalDev();
		Properties props = System.getProperties();
		props.setProperty("spring.application.name", appName);
		props.setProperty("spring.profiles.active", profile);
		props.setProperty("mica.env", profile);
		props.setProperty("mica.is-local", String.valueOf(isLocalDev));
		props.setProperty("spring.banner.location", "classpath:mica_banner.txt");
		System.err.println(String.format("---[junit.test]:[%s]---启动中，读取到的环境变量:[%s]", appName, profile));
		// 是否加载自定义组件
		if (!micaBootTest.enableLoader()) {
			return;
		}
		ServiceLoader<LauncherService> loader = ServiceLoader.load(LauncherService.class);
		SpringApplicationBuilder builder = new SpringApplicationBuilder(clazz);
		Environment env = new StandardEnvironment();
		MicaEnv micaEnv = MicaEnv.of(profile);
		// 启动组件
		CollectionUtil.toList(loader).stream().sorted()
			.forEach(launcherService -> launcherService.launcher(builder, env, appName, micaEnv, isLocalDev));
		// 反射出 builder 中的 props，兼容用户扩展
		Field field = SpringApplicationBuilder.class.getDeclaredField("defaultProperties");
		field.setAccessible(true);
		@SuppressWarnings("unchecked")
		Map<String, Object> defaultProperties = (Map<String, Object>) field.get(builder);
		if (!ObjectUtils.isEmpty(defaultProperties)) {
			props.putAll(defaultProperties);
		}
	}

}
