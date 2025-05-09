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
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.utils.ConvertUtil;
import net.dreamlu.mica.core.utils.ReflectUtil;
import net.dreamlu.mica.core.utils.StringUtil;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 代理模型
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class JsonPointerMethodInterceptor implements MethodInterceptor {
	private final Class<?> clazz;
	private final JsonNode jsonNode;

	@Nullable
	@Override
	public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		// 如果是 toString eq 等方法都不准确，故直接返回死值
		if (ReflectionUtils.isObjectMethod(method)) {
			return methodProxy.invokeSuper(object, args);
		}
		// 非 bean 方法
		PropertyDescriptor propertyDescriptor = BeanUtils.findPropertyForMethod(method, clazz);
		if (propertyDescriptor == null) {
			return methodProxy.invokeSuper(object, args);
		}
		// 非 read 的方法，只处理 get 方法 is
		if (!method.equals(propertyDescriptor.getReadMethod())) {
			return methodProxy.invokeSuper(object, args);
		}
		// 兼容 lombok bug 强制首字母小写： https://github.com/rzwitserloot/lombok/issues/1861
		String fieldName = StringUtil.firstCharToLower(propertyDescriptor.getDisplayName());
		Field field = ReflectUtil.findField(clazz, fieldName);
		if (field == null) {
			return methodProxy.invokeSuper(object, args);
		}
		JsonPointer jsonPointer = AnnotationUtils.getAnnotation(field, JsonPointer.class);
		// 没有注解，不代理
		if (jsonPointer == null) {
			return methodProxy.invokeSuper(object, args);
		}
		Class<?> returnType = method.getReturnType();
		boolean isColl = Collection.class.isAssignableFrom(returnType);
		String jsonPointerValue = jsonPointer.value();
		// 是否为 bean 中 bean
		boolean isInner = jsonPointer.inner();
		if (isInner) {
			return proxyInner(jsonPointerValue, method, returnType, isColl);
		}
		Object proxyValue = proxyValue(jsonPointerValue, returnType, isColl);
		if (String.class.isAssignableFrom(returnType)) {
			return proxyValue;
		}
		// 用于读取 field 上的注解
		TypeDescriptor typeDescriptor = new TypeDescriptor(field);
		return ConvertUtil.convert(proxyValue, typeDescriptor);
	}

	@Nullable
	private Object proxyValue(String jsonPointerValue, Class<?> returnType, boolean isColl) {
		if (isColl) {
			JsonNode nodes = jsonNode.at(jsonPointerValue);
			Collection<Object> valueList = newColl(returnType);
			if (nodes.isEmpty()) {
				return valueList;
			}
			for (JsonNode node : nodes) {
				String value = getValue(node);
				if (value != null) {
					valueList.add(value);
				}
			}
			return valueList;
		}
		return getValue(jsonNode.at(jsonPointerValue));
	}

	private Object proxyInner(String jsonPointerValue, Method method, Class<?> returnType, boolean isColl) {
		if (isColl) {
			JsonNode nodes = jsonNode.at(jsonPointerValue);
			Collection<Object> valueList = newColl(returnType);
			ResolvableType resolvableType = ResolvableType.forMethodReturnType(method);
			Class<?> innerType = resolvableType.getGeneric(0).resolve();
			if (innerType == null) {
				throw new IllegalArgumentException("Class " + returnType + " 读取泛型失败。");
			}
			for (JsonNode node : nodes) {
				valueList.add(JsonPointerUtil.readValue(node, innerType));
			}
			return valueList;
		}
		return JsonPointerUtil.readValue(jsonNode.at(jsonPointerValue), returnType);
	}

	@Nullable
	private String getValue(@Nullable JsonNode jsonNode) {
		if (jsonNode == null) {
			return null;
		}
		return jsonNode.asText();
	}

	private Collection<Object> newColl(Class<?> returnType) {
		return Set.class.isAssignableFrom(returnType) ? new HashSet<>() : new ArrayList<>();
	}
}
