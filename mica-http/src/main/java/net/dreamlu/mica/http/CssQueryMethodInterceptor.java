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

import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.utils.ConvertUtil;
import net.dreamlu.mica.core.utils.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 代理模型
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class CssQueryMethodInterceptor implements InvocationHandler {
	private final Class<?> clazz;
	private final Element element;

	@Nullable
	@Override
	public Object invoke(Object object, Method method, Object[] args) throws Throwable {
		// 如果是 toString eq 等方法都不准确，故直接返回死值
		if (ReflectionUtils.isToStringMethod(method)) {
			return clazz.toString();
		} else if (ReflectionUtils.isEqualsMethod(method)) {
			return false;
		} else if (ReflectionUtils.isHashCodeMethod(method)) {
			return 1;
		}
		// 只处理 get 方法 is
		String name = method.getName();
		if (!name.startsWith("get")) {
			return method.invoke(object, args);
		}
		Field field = clazz.getDeclaredField(StringUtil.firstCharToLower(name.substring(3)));
		CssQuery annotation = field.getAnnotation(CssQuery.class);
		// 没有注解，不代理
		if (annotation == null) {
			return method.invoke(object, args);
		}
		Class<?> returnType = method.getReturnType();
		boolean isColl = Collection.class.isAssignableFrom(returnType);
		String cssQueryValue = annotation.value();
		// 是否为 bean 中 bean
		boolean isInner = annotation.inner();
		if (isInner) {
			return proxyInner(cssQueryValue, method, returnType, isColl);
		}
		String attrName = annotation.attr();
		Object proxyValue = proxyValue(cssQueryValue, attrName, returnType, isColl);
		if (String.class.isAssignableFrom(returnType)) {
			return proxyValue;
		}
		// 类型转换
		TypeDescriptor typeDescriptor = new TypeDescriptor(field);
		return ConvertUtil.convert(proxyValue, typeDescriptor);
	}

	@Nullable
	private Object proxyValue(String cssQueryValue, String attrName, Class<?> returnType, boolean isColl) {
		if (isColl) {
			Elements elements = Selector.select(cssQueryValue, element);
			Collection<Object> valueList = newColl(returnType);
			if (elements.isEmpty()) {
				return valueList;
			}
			for (Element select : elements) {
				String value = getValue(select, attrName);
				if (value != null) {
					valueList.add(value);
				}
			}
			return valueList;
		}
		Element select = Selector.selectFirst(cssQueryValue, element);
		return getValue(select, attrName);
	}

	private Object proxyInner(String cssQueryValue, Method method, Class<?> returnType, boolean isColl) {
		if (isColl) {
			Elements elements = Selector.select(cssQueryValue, element);
			Collection<Object> valueList = newColl(returnType);
			ResolvableType resolvableType = ResolvableType.forMethodReturnType(method);
			Class<?> innerType = resolvableType.getGeneric(0).resolve();
			if (innerType == null) {
				throw new IllegalArgumentException("Class " + returnType + " 读取泛型失败。");
			}
			for (Element select : elements) {
				valueList.add(DomMapper.readValue(select, innerType));
			}
			return valueList;
		}
		Element select = Selector.selectFirst(cssQueryValue, element);
		return DomMapper.readValue(select, returnType);
	}

	@Nullable
	private String getValue(@Nullable Element element, String attrName) {
		if (element == null) {
			return null;
		}
		if (StringUtil.isBlank(attrName)) {
			return element.outerHtml();
		} else if ("html".equalsIgnoreCase(attrName)) {
			return element.html();
		} else if ("text".equalsIgnoreCase(attrName)) {
			return getText(element);
		} else if ("allText".equalsIgnoreCase(attrName)) {
			return element.text();
		} else {
			return element.attr(attrName);
		}
	}

	private String getText(Element element) {
		return element.childNodes().stream()
			.filter(node -> node instanceof TextNode)
			.map(node -> (TextNode) node)
			.map(TextNode::text)
			.collect(Collectors.joining());
	}

	private Collection<Object> newColl(Class<?> returnType) {
		return Set.class.isAssignableFrom(returnType) ? new HashSet<>() : new ArrayList<>();
	}
}
