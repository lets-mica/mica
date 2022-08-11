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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
	public static PropertyDescriptor[] getBeanGetters(Class type) {
		return getPropertyDescriptors(type, true, false);
	}

	/**
	 * 获取 Bean 的所有 set方法
	 *
	 * @param type 类
	 * @return PropertyDescriptor数组
	 */
	public static PropertyDescriptor[] getBeanSetters(Class type) {
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
	 * 获取 类属性
	 *
	 * @param clazz     类信息
	 * @param fieldName 属性名
	 * @return Field
	 */
	@Nullable
	public static Field getField(Class<?> clazz, String fieldName) {
		while (clazz != Object.class) {
			try {
				return clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}

	/**
	 * 获取 所有 field 属性上的注解
	 *
	 * @param clazz           类
	 * @param fieldName       属性名
	 * @param annotationClass 注解
	 * @param <T>             注解泛型
	 * @return 注解
	 */
	@Nullable
	public static <T extends Annotation> T getAnnotation(Class<?> clazz, String fieldName, Class<T> annotationClass) {
		Field field = ReflectUtil.getField(clazz, fieldName);
		if (field == null) {
			return null;
		}
		return field.getAnnotation(annotationClass);
	}


	/**
	 * 重写 setField 的方法，用于处理 setAccessible 的问题
	 *
	 * @param field  Field
	 * @param target Object
	 * @param value  value
	 */
	public static void setField(Field field, @Nullable Object target, @Nullable Object value) {
		makeAccessible(field);
		ReflectionUtils.setField(field, target, value);
	}

	/**
	 * 重写 setField 的方法，用于处理 setAccessible 的问题
	 *
	 * @param field  Field
	 * @param target Object
	 * @return value
	 */
	@Nullable
	public static Object getField(Field field, @Nullable Object target) {
		makeAccessible(field);
		return ReflectionUtils.getField(field, target);
	}

	/**
	 * 重写 setField 的方法，用于处理 setAccessible 的问题
	 *
	 * @param fieldName Field name
	 * @param target    Object
	 * @return value
	 */
	@Nullable
	public static Object getField(String fieldName, @Nullable Object target) {
		if (target == null) {
			return null;
		}
		Class<?> targetClass = target.getClass();
		Field field = getField(targetClass, fieldName);
		if (field == null) {
			throw new IllegalArgumentException(fieldName + " not in" + targetClass);
		}
		return getField(field, target);
	}

	/**
	 * 重写 invokeMethod 的方法，用于处理 setAccessible 的问题
	 *
	 * @param method Method
	 * @param target Object
	 * @return value
	 */
	@Nullable
	public static Object invokeMethod(Method method, @Nullable Object target) {
		return ReflectUtil.invokeMethod(method, target, new Object[0]);
	}

	/**
	 * 重写 invokeMethod 的方法，用于处理 setAccessible 的问题
	 *
	 * @param method Method
	 * @param target Object
	 * @param args   args
	 * @return value
	 */
	@Nullable
	public static Object invokeMethod(Method method, @Nullable Object target, @Nullable Object... args) {
		makeAccessible(method);
		return ReflectionUtils.invokeMethod(method, target, args);
	}

}
