/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.springframework.cloud.openfeign;

import feign.Feign;
import feign.Target;
import feign.hystrix.FallbackFactory;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import net.dreamlu.mica.feign.MicaFallbackFactory;
import org.springframework.lang.Nullable;

/**
 * 添加 mica 默认的 fallbackFactory L.cm 2019.01.19
 *
 * @author L.cm
 * @author Spencer Gibb
 * @author Erik Kringen
 */
@SuppressWarnings("unchecked")
class MicaHystrixTargeter implements Targeter {

	@Override
	public <T> T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context,
						Target.HardCodedTarget<T> target) {
		if (!(feign instanceof HystrixFeign.Builder)) {
			return feign.target(target);
		}
		HystrixFeign.Builder builder = (HystrixFeign.Builder) feign;
		SetterFactory setterFactory = getOptional(factory.getName(), context, SetterFactory.class);
		if (setterFactory != null) {
			builder.setterFactory(setterFactory);
		}
		Class<?> fallback = factory.getFallback();
		if (fallback != void.class) {
			return targetWithFallback(factory.getName(), context, target, builder, fallback);
		}
		Class<?> fallbackFactory = factory.getFallbackFactory();
		if (fallbackFactory != void.class) {
			return targetWithFallbackFactory(factory.getName(), context, target, builder, fallbackFactory);
		}
		// mica 默认的 fallbackFactory
		MicaFallbackFactory micaFallbackFactory = new MicaFallbackFactory(target);
		return (T) builder.target(target, micaFallbackFactory);
	}

	private <T> T targetWithFallbackFactory(String feignClientName, FeignContext context,
											Target.HardCodedTarget<T> target,
											HystrixFeign.Builder builder,
											Class<?> fallbackFactoryClass) {
		FallbackFactory<? extends T> fallbackFactory = (FallbackFactory<? extends T>)
			getFromContext("fallbackFactory", feignClientName, context, fallbackFactoryClass, FallbackFactory.class);
		return builder.target(target, fallbackFactory);
	}


	private <T> T targetWithFallback(String feignClientName, FeignContext context,
									 Target.HardCodedTarget<T> target,
									 HystrixFeign.Builder builder, Class<?> fallback) {
		T fallbackInstance = getFromContext("fallback", feignClientName, context, fallback, target.type());
		return builder.target(target, fallbackInstance);
	}

	private <T> T getFromContext(String fallbackMechanism, String feignClientName, FeignContext context, Class<?> beanType,
								 Class<T> targetType) {
		Object fallbackInstance = context.getInstance(feignClientName, beanType);
		if (fallbackInstance == null) {
			throw new IllegalStateException(String.format("No " + fallbackMechanism +
				" instance of type %s found for feign client %s", beanType, feignClientName));
		}

		if (!targetType.isAssignableFrom(beanType)) {
			throw new IllegalStateException(String.format(
				"Incompatible " + fallbackMechanism + " instance. Fallback/fallbackFactory of " +
					"type %s is not assignable to %s for feign client %s", beanType, targetType, feignClientName));
		}
		return (T) fallbackInstance;
	}

	@Nullable
	private <T> T getOptional(String feignClientName, FeignContext context, Class<T> beanType) {
		return context.getInstance(feignClientName, beanType);
	}
}
