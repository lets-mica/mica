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

package net.dreamlu.mica.core.spring;

import org.jspecify.annotations.Nullable;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.ResolvableType;

/**
 * spring 工具类
 *
 * @author L.cm
 */
@SuppressWarnings("unchecked")
public class SpringContextUtil implements ApplicationContextAware {
	@Nullable
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringContextUtil.context = context;
	}

	@Nullable
	public static <T> T getBean(Class<T> clazz) {
		if (context == null) {
			return null;
		}
		return context.getBean(clazz);
	}

	@Nullable
	public static <T> T getBean(String beanName) {
		if (context == null) {
			return null;
		}
		return (T) context.getBean(beanName);
	}

	@Nullable
	public static <T> T getBean(String beanName, Class<T> clazz) {
		if (context == null) {
			return null;
		}
		return context.getBean(beanName, clazz);
	}

	@Nullable
	public static <T> ObjectProvider<T> getBeanProvider(Class<T> clazz) {
		if (context == null) {
			return null;
		}
		return context.getBeanProvider(clazz);
	}

	@Nullable
	public static <T> ObjectProvider<T> getBeanProvider(ResolvableType resolvableType) {
		if (context == null) {
			return null;
		}
		return context.getBeanProvider(resolvableType);
	}

	@Nullable
	public static ApplicationContext getContext() {
		return context;
	}

	public static void publishEvent(ApplicationEvent event) {
		publishEvent((Object) event);
	}

	public static void publishEvent(Object event) {
		if (context == null) {
			return;
		}
		context.publishEvent(event);
	}

	/**
	 * 获取aop代理对象
	 *
	 * @return 代理对象
	 */
	public static <T> T getCurrentProxy() {
		return (T) AopContext.currentProxy();
	}

}
