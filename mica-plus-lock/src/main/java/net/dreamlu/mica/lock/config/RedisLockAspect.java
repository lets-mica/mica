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

package net.dreamlu.mica.lock.config;

import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.core.spel.MicaExpressionEvaluator;
import net.dreamlu.mica.core.utils.CharPool;
import net.dreamlu.mica.lock.annotation.LockType;
import net.dreamlu.mica.lock.annotation.RedisLock;
import net.dreamlu.mica.lock.client.RedisLockClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * redis 分布式锁
 *
 * @author L.cm
 */
@Aspect
@RequiredArgsConstructor
public class RedisLockAspect implements ApplicationContextAware {

	/**
	 * 表达式处理
	 */
	private static final MicaExpressionEvaluator EVALUATOR = new MicaExpressionEvaluator();
	/**
	 * redis 限流服务
	 */
	private final RedisLockClient redisLockClient;
	private ApplicationContext applicationContext;

	/**
	 * AOP 环切 注解 @RedisLock
	 */
	@Around("@annotation(redisLock)")
	public Object aroundRedisLock(ProceedingJoinPoint point, RedisLock redisLock) throws Throwable {
		String lockName = redisLock.value();
		Assert.hasText(lockName, "@RedisLock value must have length; it must not be null or empty");
		// el 表达式
		String lockParam = redisLock.param();
		// 表达式不为空
		String lockKey;
		if (StringUtil.isNotBlank(lockParam)) {
			String evalAsText = evalLockParam(point, lockParam);
			lockKey = lockName + CharPool.COLON + evalAsText;
		} else {
			lockKey = lockName;
		}
		LockType lockType = redisLock.type();
		long waitTime = redisLock.waitTime();
		long leaseTime = redisLock.leaseTime();
		TimeUnit timeUnit = redisLock.timeUnit();
		return redisLockClient.lock(lockKey, lockType, waitTime, leaseTime, timeUnit, point::proceed);
	}

	/**
	 * 计算参数表达式
	 *
	 * @param point     ProceedingJoinPoint
	 * @param lockParam lockParam
	 * @return 结果
	 */
	private String evalLockParam(ProceedingJoinPoint point, String lockParam) {
		MethodSignature ms = (MethodSignature) point.getSignature();
		Method method = ms.getMethod();
		Object[] args = point.getArgs();
		Object target = point.getTarget();
		Class<?> targetClass = target.getClass();
		EvaluationContext context = EVALUATOR.createContext(method, args, target, targetClass, applicationContext);
		AnnotatedElementKey elementKey = new AnnotatedElementKey(method, targetClass);
		return EVALUATOR.evalAsText(lockParam, elementKey, context);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
