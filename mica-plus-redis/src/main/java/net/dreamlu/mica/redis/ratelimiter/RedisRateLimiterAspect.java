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

package net.dreamlu.mica.redis.ratelimiter;

import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.utils.CharPool;
import net.dreamlu.mica.core.utils.ClassUtil;
import net.dreamlu.mica.core.utils.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.MethodParameter;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * redis 限流
 *
 * @author L.cm
 */
@Aspect
@RequiredArgsConstructor
public class RedisRateLimiterAspect implements ApplicationContextAware {
	/**
	 * 表达式处理
	 */
	private static final ExpressionParser SP_EL_PARSER = new SpelExpressionParser();
	/**
	 * redis 限流服务
	 */
	private final RedisRateLimiterClient rateLimiterClient;
	private ApplicationContext applicationContext;

	/**
	 * AOP 环切 注解 @RateLimiter
	 */
	@Around("@annotation(limiter)")
	public Object aroundRateLimiter(ProceedingJoinPoint point, RateLimiter limiter) throws Throwable {
		MethodSignature ms = (MethodSignature) point.getSignature();
		Method method = ms.getMethod();
		String limitKey = limiter.value();
		Assert.hasText(limitKey, "@RateLimiter value must have length; it must not be null or empty");
		// el 表达式
		String limitParam = limiter.param();
		// 表达式不为空
		String rateKey;
		if (StringUtil.isNotBlank(limitParam)) {
			Expression expression = SP_EL_PARSER.parseExpression(limitParam);
			StandardEvaluationContext context = getEvaluationContext(point, method);
			rateKey = limitKey + CharPool.COLON + expression.getValue(context, String.class);
		} else {
			rateKey = limitKey;
		}
		long max = limiter.max();
		long ttl = limiter.ttl();
		TimeUnit timeUnit = limiter.timeUnit();
		return rateLimiterClient.allow(rateKey, max, ttl, timeUnit, point::proceed);
	}

	/**
	 * 获取方法上的参数
	 *
	 * @param point 切点
	 * @return {SimpleEvaluationContext}
	 */
	private StandardEvaluationContext getEvaluationContext(ProceedingJoinPoint point, Method method) {
		// 初始化Sp el表达式上下文
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 设置表达式支持spring bean
		context.setBeanResolver(new BeanFactoryResolver(applicationContext));
		Object[] args = point.getArgs();
		for (int i = 0; i < args.length; i++) {
			// 读取方法参数
			MethodParameter methodParam = ClassUtil.getMethodParameter(method, i);
			Object value = args[i];
			// 设置方法参数名和值为sp el变量
			context.setVariable(methodParam.getParameterName(), value);
		}
		return context;
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
