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

package net.dreamlu.mica.core.spel;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存 spEl 提高性能
 *
 * @author L.cm
 */
public class MicaExpressionEvaluator extends CachedExpressionEvaluator {
	private final Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);
	private final Map<AnnotatedElementKey, Method> methodCache = new ConcurrentHashMap<>(64);

	/**
	 * Create an {@link EvaluationContext}.
	 *
	 * @param method      the method
	 * @param args        the method arguments
	 * @param target      the target object
	 * @param targetClass the target class
	 * @return the evaluation context
	 */
	public EvaluationContext createContext(Method method, Object[] args, Object target, Class<?> targetClass, @Nullable BeanFactory beanFactory) {
		Method targetMethod = getTargetMethod(targetClass, method);
		MicaExpressionRootObject rootObject = new MicaExpressionRootObject(method, args, target, targetClass, targetMethod);
		MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(rootObject, targetMethod, args, getParameterNameDiscoverer());
		if (beanFactory != null) {
			evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
		}
		return evaluationContext;
	}

	/**
	 * Create an {@link EvaluationContext}.
	 *
	 * @param method      the method
	 * @param args        the method arguments
	 * @param rootObject  rootObject
	 * @param targetClass the target class
	 * @return the evaluation context
	 */
	public EvaluationContext createContext(Method method, Object[] args, Class<?> targetClass, Object rootObject, @Nullable BeanFactory beanFactory) {
		Method targetMethod = getTargetMethod(targetClass, method);
		MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(rootObject, targetMethod, args, getParameterNameDiscoverer());
		if (beanFactory != null) {
			evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
		}
		return evaluationContext;
	}

	@Nullable
	public Object eval(String expression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
		return eval(expression, methodKey, evalContext, null);
	}

	@Nullable
	public <T> T eval(String expression, AnnotatedElementKey methodKey, EvaluationContext evalContext, @Nullable Class<T> valueType) {
		return getExpression(this.expressionCache, methodKey, expression).getValue(evalContext, valueType);
	}

	@Nullable
	public String evalAsText(String expression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
		return eval(expression, methodKey, evalContext, String.class);
	}

	public boolean evalAsBool(String expression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
		return Boolean.TRUE.equals(eval(expression, methodKey, evalContext, Boolean.class));
	}

	private Method getTargetMethod(Class<?> targetClass, Method method) {
		AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
		return methodCache.computeIfAbsent(methodKey, (key) -> AopUtils.getMostSpecificMethod(method, targetClass));
	}

	/**
	 * Clear all caches.
	 */
	public void clear() {
		this.expressionCache.clear();
		this.methodCache.clear();
	}
}
