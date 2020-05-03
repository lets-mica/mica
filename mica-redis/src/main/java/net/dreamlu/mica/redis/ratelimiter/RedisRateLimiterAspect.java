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
import net.dreamlu.mica.core.spel.MicaExpressionEvaluator;
import net.dreamlu.mica.core.utils.CharPool;
import net.dreamlu.mica.core.utils.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
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
	private final MicaExpressionEvaluator evaluator = new MicaExpressionEvaluator();
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
		String limitKey = limiter.value();
		Assert.hasText(limitKey, "@RateLimiter value must have length; it must not be null or empty");
		// el 表达式
		String limitParam = limiter.param();
		// 表达式不为空
		String rateKey;
		if (StringUtil.isNotBlank(limitParam)) {
			String evalAsText = evalLimitParam(point, limitParam);
			rateKey = limitKey + CharPool.COLON + evalAsText;
		} else {
			rateKey = limitKey;
		}
		long max = limiter.max();
		long ttl = limiter.ttl();
		TimeUnit timeUnit = limiter.timeUnit();
		return rateLimiterClient.allow(rateKey, max, ttl, timeUnit, point::proceed);
	}

	/**
	 * 计算参数表达式
	 *
	 * @param point      ProceedingJoinPoint
	 * @param limitParam limitParam
	 * @return 结果
	 */
	private String evalLimitParam(ProceedingJoinPoint point, String limitParam) {
		MethodSignature ms = (MethodSignature) point.getSignature();
		Method method = ms.getMethod();
		Object[] args = point.getArgs();
		Object target = point.getTarget();
		Class<?> targetClass = target.getClass();
		EvaluationContext context = evaluator.createContext(method, args, target, targetClass, applicationContext);
		AnnotatedElementKey elementKey = new AnnotatedElementKey(method, targetClass);
		return evaluator.evalAsText(limitParam, elementKey, context);
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
