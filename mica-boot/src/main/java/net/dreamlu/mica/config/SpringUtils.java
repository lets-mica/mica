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

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.Nullable;

/**
 * spring 工具类
 *
 * @author L.cm
 */
public class SpringUtils implements ApplicationContextAware {
	@Nullable
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringUtils.context = context;
	}

	@Nullable
	public static <T> T getBean(Class<T> clazz){
		if (context == null) { return null; }
		return context.getBean(clazz);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName){
		if (context == null) { return null; }
		return (T) context.getBean(beanName);
	}

	@Nullable
	public static <T> T getBean(String beanName, Class<T> clazz) {
		if (context == null) { return null; }
		return context.getBean(beanName, clazz);
	}

	@Nullable
	public static ApplicationContext getContext(){
		return context;
	}

	public static void publishEvent(ApplicationEvent event) {
		if (context == null) { return ; }
		context.publishEvent(event);
	}

}
