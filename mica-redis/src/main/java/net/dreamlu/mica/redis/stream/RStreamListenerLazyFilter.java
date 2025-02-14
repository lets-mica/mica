/**
 * Copyright (c) 2018-2099, DreamLu 卢春梦 (qq596392912@gmail.com).
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

package net.dreamlu.mica.redis.stream;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.LazyInitializationExcludeFilter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * mqtt 客户端订阅延迟加载排除
 *
 * @author L.cm
 */
public class RStreamListenerLazyFilter implements LazyInitializationExcludeFilter {

	@Override
	public boolean isExcluded(String beanName, BeanDefinition beanDefinition, Class<?> beanType) {
		// 类上有注解的情况
		RStreamListener subscribe = AnnotationUtils.findAnnotation(beanType, RStreamListener.class);
		if (subscribe != null) {
			return true;
		}
		// 方法上的注解
		List<Method> methodList = new ArrayList<>();
		ReflectionUtils.doWithMethods(beanType, method -> {
			RStreamListener clientSubscribe = AnnotationUtils.findAnnotation(method, RStreamListener.class);
			if (clientSubscribe != null) {
				methodList.add(method);
			}
		}, ReflectionUtils.USER_DECLARED_METHODS);
		return !methodList.isEmpty();
	}

}
