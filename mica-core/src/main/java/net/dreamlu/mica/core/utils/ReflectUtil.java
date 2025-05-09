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

package net.dreamlu.mica.core.utils;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射工具类
 *
 * @author L.cm
 */
public class ReflectUtil extends ReflectionUtils {

	/**
	 * 获取 Bean 的所有 get方法
	 *
	 * @param type 类
	 * @return PropertyDescriptor数组
	 */
	public static PropertyDescriptor[] getBeanGetters(Class<?> type) {
		return getPropertyDescriptors(type, true, false);
	}

	/**
	 * 获取 Bean 的所有 set方法
	 *
	 * @param type 类
	 * @return PropertyDescriptor数组
	 */
	public static PropertyDescriptor[] getBeanSetters(Class<?> type) {
		return getPropertyDescriptors(type, false, true);
	}

	/**
	 * 获取 Bean 的所有 PropertyDescriptor
	 *
	 * @param type  类
	 * @param read  读取方法
	 * @param write 写方法
	 * @return PropertyDescriptor数组
	 */
	public static PropertyDescriptor[] getPropertyDescriptors(Class type, boolean read, boolean write) {
		try {
			PropertyDescriptor[] all = BeanUtils.getPropertyDescriptors(type);
			if (read && write) {
				return all;
			} else {
				List<PropertyDescriptor> properties = new ArrayList<>(all.length);
				for (PropertyDescriptor pd : all) {
					if (read && pd.getReadMethod() != null) {
						properties.add(pd);
					} else if (write && pd.getWriteMethod() != null) {
						properties.add(pd);
					}
				}
				return properties.toArray(new PropertyDescriptor[0]);
			}
		} catch (BeansException ex) {
			throw new CodeGenerationException(ex);
		}
	}

	/**
	 * 获取 bean 的属性信息
	 *
	 * @param propertyType 类型
	 * @param propertyName 属性名
	 * @return {Property}
	 */
	@Nullable
	public static Property getProperty(Class<?> propertyType, String propertyName) {
		PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(propertyType, propertyName);
		if (propertyDescriptor == null) {
			return null;
		}
		return ReflectUtil.getProperty(propertyType, propertyDescriptor, propertyName);
	}

	/**
	 * 获取 bean 的属性信息
	 *
	 * @param propertyType       类型
	 * @param propertyDescriptor PropertyDescriptor
	 * @param propertyName       属性名
	 * @return {Property}
	 */
	public static Property getProperty(Class<?> propertyType, PropertyDescriptor propertyDescriptor, String propertyName) {
		Method readMethod = propertyDescriptor.getReadMethod();
		Method writeMethod = propertyDescriptor.getWriteMethod();
		return new Property(propertyType, readMethod, writeMethod, propertyName);
	}

	/**
	 * 获取 bean 的属性信息
	 *
	 * @param propertyType 类型
	 * @param propertyName 属性名
	 * @return {Property}
	 */
	@Nullable
	public static TypeDescriptor getTypeDescriptor(Class<?> propertyType, String propertyName) {
		Property property = ReflectUtil.getProperty(propertyType, propertyName);
		if (property == null) {
			return null;
		}
		return new TypeDescriptor(property);
	}

	/**
	 * 获取 类属性信息
	 *
	 * @param propertyType       类型
	 * @param propertyDescriptor PropertyDescriptor
	 * @param propertyName       属性名
	 * @return {Property}
	 */
	public static TypeDescriptor getTypeDescriptor(Class<?> propertyType, PropertyDescriptor propertyDescriptor, String propertyName) {
		Method readMethod = propertyDescriptor.getReadMethod();
		Method writeMethod = propertyDescriptor.getWriteMethod();
		Property property = new Property(propertyType, readMethod, writeMethod, propertyName);
		return new TypeDescriptor(property);
	}

	/**
	 * 如果存在指定类型的注解，则返回该元素对应的注解，否则返回 null。
	 * 注意：此方法返回的任何注解均为声明注解（declaration annotation）。不继承父类或接口的注解。
	 * 若需继承行为，需使用其他方法（如 {@link MergedAnnotations#from(AnnotatedElement)}）。
	 * <br/>
	 * 注解说明：
	 * <ul>
	 * 	<li>声明注解（Declaration Annotation）：直接附加在类、方法、字段等声明上的注解（例如 {@link java.lang.Override}）。</li>
	 * 	<li>类型注解（Type Annotation）：附加在类型上的注解{@link ElementType#TYPE_USE}</li>
	 * </ul>
	 *
	 * @param clazz           类
	 * @param fieldName       属性名
	 * @param annotationClass 注解
	 * @param <T>             注解泛型{@link Class}
	 * @return {@link Annotation}注解
	 */
	@Nullable
	public static <T extends Annotation> T getAnnotation(Class<?> clazz, String fieldName, Class<T> annotationClass) {
		Field field = findField(clazz, fieldName);
		if (field == null) {
			return null;
		}
		return field.getAnnotation(annotationClass);
	}

	/**
	 * 获取指定类及其超类中的字段. 直至 Object.
	 * 同名字段的顺序：本类、超类、超类的超类...
	 *
	 * @param clazz 类信息
	 * @return {@link List<Field>} 属性列表
	 * @see Class#getDeclaredFields()
	 * @see Class#getSuperclass() 获取超类
	 * @see ReflectionUtils#findMethod(Class, String) 查找本类或超类中的方法
	 */
	public static List<Field> getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		while (clazz != null && clazz != Object.class) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	/**
	 * 重写 getField 的方法，用于处理 setAccessible 的问题
	 *
	 * @param field  Field
	 * @param target Object
	 * @return {@link Object}
	 * @see ReflectionUtils#makeAccessible(Field)
	 * @see ReflectionUtils#getField(Field, Object)
	 */
	@Nullable
	public static Object getField(Field field, @Nullable Object target) {
		makeAccessible(field);
		return ReflectionUtils.getField(field, target);
	}

	/**
	 * 重写 getField 的方法，用于处理 setAccessible 的问题
	 *
	 * @param fieldName Field name
	 * @param target    Object
	 * @return {@link Object}
	 * @throws IllegalArgumentException 如果未找到该属性
	 */
	@Nullable
	public static Object getField(String fieldName, @Nullable Object target) throws IllegalArgumentException {
		if (target == null) {
			return null;
		}
		Class<?> targetClass = target.getClass();
		Field field = findField(targetClass, fieldName);
		if (field == null) {
			throw new IllegalArgumentException(fieldName + " not in" + targetClass);
		}
		return getField(field, target);
	}

	/**
	 * 重写 setField 的方法，用于处理 setAccessible 的问题
	 *
	 * @param field  Field
	 * @param target Object
	 * @param value  value
	 * @see ReflectionUtils#makeAccessible(Field)
	 * @see ReflectionUtils#setField(Field, Object, Object)
	 */
	public static void setField(Field field, @Nullable Object target, @Nullable Object value) {
		makeAccessible(field);
		ReflectionUtils.setField(field, target, value);
	}

	/**
	 * 重写 invokeMethod 的方法，用于处理 setAccessible 的问题
	 *
	 * @param method Method
	 * @param target Object
	 * @return value
	 * @see ReflectionUtils#makeAccessible(Method)
	 * @see ReflectionUtils#invokeMethod(Method, Object, Object...)
	 */
	@Nullable
	public static Object invokeMethod(Method method, @Nullable Object target) {
		return invokeMethod(method, target, new Object[0]);
	}

	/**
	 * 重写 invokeMethod 的方法，用于处理 setAccessible 的问题
	 *
	 * @param method Method
	 * @param target Object
	 * @param args   args
	 * @return value
	 * @see ReflectionUtils#makeAccessible(Method)
	 * @see ReflectionUtils#invokeMethod(Method, Object, Object...)
	 */
	@Nullable
	public static Object invokeMethod(Method method, @Nullable Object target, @Nullable Object... args) {
		makeAccessible(method);
		return ReflectionUtils.invokeMethod(method, target, args);
	}

	/**
	 * 尝试在给定的类中查找具有给定名称的字段.搜索所有超类直至 Object.
	 * 请使用： {@link #findField(Class, String)}
	 *
	 * @param clazz     类信息
	 * @param fieldName 属性名
	 * @return 相应的 Field 字段, 如果未找到返回 {@code null}
	 * @see Class#getSuperclass() 获取超类
	 * @deprecated 3.4.4，请使用 {@link #findField(Class, String)}
	 */
	@Deprecated(since = "3.4.4", forRemoval = true)
	@Nullable
	public static Field getField(Class<?> clazz, String fieldName) {
		return findField(clazz, fieldName);
	}
}
