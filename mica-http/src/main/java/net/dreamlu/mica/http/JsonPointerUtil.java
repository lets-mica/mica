/*
 * Copyright (c) 2019-2029, Dreamlu (596392912@qq.com & www.dreamlu.net).
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

package net.dreamlu.mica.http;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

/**
 * 采用 jackson JSON Pointer 语法快速解析 bean
 *
 * @author L.cm
 */
public class JsonPointerUtil {

	/**
	 * 读取 JsonNode 信息为 java Bean
	 *
	 * @param jsonNode   JsonNode
	 * @param clazz bean Class
	 * @param <T>   泛型
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readValue(final JsonNode jsonNode, final Class<T> clazz) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setUseCache(true);
		enhancer.setContextClass(clazz);
		// 如果类上有 JsonPointer 注解
		JsonPointer jsonPointer = clazz.getAnnotation(JsonPointer.class);
		MethodInterceptor interceptor;
		if (jsonPointer != null) {
			interceptor = new JsonPointerMethodInterceptor(clazz, jsonNode.at(jsonPointer.value()));
		} else {
			interceptor = new JsonPointerMethodInterceptor(clazz, jsonNode);
		}
		enhancer.setCallback(interceptor);
		return (T) enhancer.create();
	}

}
