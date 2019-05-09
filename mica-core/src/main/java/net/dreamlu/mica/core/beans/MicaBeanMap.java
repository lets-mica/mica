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

package net.dreamlu.mica.core.beans;

import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.AbstractClassGenerator;
import org.springframework.cglib.core.ReflectUtils;

import java.security.ProtectionDomain;

/**
 * 重写 cglib BeanMap，支持链式bean
 *
 * @author L.cm
 */
public abstract class MicaBeanMap extends BeanMap {
	protected MicaBeanMap() {}

	protected MicaBeanMap(Object bean) {
		super(bean);
	}

	public static MicaBeanMap create(Object bean) {
		MicaGenerator gen = new MicaGenerator();
		gen.setBean(bean);
		return gen.create();
	}

	/**
	 * newInstance
	 *
	 * @param o Object
	 * @return MicaBeanMap
	 */
	@Override
	public abstract MicaBeanMap newInstance(Object o);

	public static class MicaGenerator extends AbstractClassGenerator {
		private static final Source SOURCE = new Source(MicaBeanMap.class.getName());

		private Object bean;
		private Class beanClass;
		private int require;

		public MicaGenerator() {
			super(SOURCE);
		}

		/**
		 * Set the bean that the generated map should reflect. The bean may be swapped
		 * out for another bean of the same type using {@link #setBean}.
		 * Calling this method overrides any value previously set using {@link #setBeanClass}.
		 * You must call either this method or {@link #setBeanClass} before {@link #create}.
		 * @param bean the initial bean
		 */
		public void setBean(Object bean) {
			this.bean = bean;
			if (bean != null) {
				beanClass = bean.getClass();
			}
		}

		/**
		 * Set the class of the bean that the generated map should support.
		 * You must call either this method or {@link #setBeanClass} before {@link #create}.
		 * @param beanClass the class of the bean
		 */
		public void setBeanClass(Class beanClass) {
			this.beanClass = beanClass;
		}

		/**
		 * Limit the properties reflected by the generated map.
		 * @param require any combination of {@link #REQUIRE_GETTER} and
		 * {@link #REQUIRE_SETTER}; default is zero (any property allowed)
		 */
		public void setRequire(int require) {
			this.require = require;
		}

		@Override
		protected ClassLoader getDefaultClassLoader() {
			return beanClass.getClassLoader();
		}

		@Override
		protected ProtectionDomain getProtectionDomain() {
			return ReflectUtils.getProtectionDomain(beanClass);
		}

		/**
		 * Create a new instance of the <code>BeanMap</code>. An existing
		 * generated class will be reused if possible.
		 * @return {MicaBeanMap}
		 */
		public MicaBeanMap create() {
			if (beanClass == null) {
				throw new IllegalArgumentException("Class of bean unknown");
			}
			setNamePrefix(beanClass.getName());
			MicaBeanMapKey key = new MicaBeanMapKey(beanClass, require);
			return (MicaBeanMap)super.create(key);
		}

		@Override
		public void generateClass(ClassVisitor v) throws Exception {
			new MicaBeanMapEmitter(v, getClassName(), beanClass, require);
		}

		@Override
		protected Object firstInstance(Class type) {
			return ((BeanMap)ReflectUtils.newInstance(type)).newInstance(bean);
		}

		@Override
		protected Object nextInstance(Object instance) {
			return ((BeanMap)instance).newInstance(bean);
		}
	}

}
