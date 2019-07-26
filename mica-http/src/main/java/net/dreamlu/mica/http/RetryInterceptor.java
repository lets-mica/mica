package net.dreamlu.mica.http;

import net.dreamlu.mica.core.utils.ThreadUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * 重试拦截器，应对代理问题
 *
 * @author dream.lu
 */
public class RetryInterceptor implements Interceptor {
	private final RetryPolicy retryPolicy;

	public RetryInterceptor(RetryPolicy retryPolicy) {
		this.retryPolicy = retryPolicy;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		return retryOnException(retryPolicy, () -> chain.proceed(request));
	}

	/**
	 * 在遇到异常时尝试重试
	 *
	 * @param retryPolicy   重试策略
	 * @param retryCallable 重试回调
	 * @param <V>           泛型
	 * @return V 结果
	 */
	private static <V> V retryOnException(RetryPolicy retryPolicy,
										  Callable<V> retryCallable) {
		final int maxAttempts = retryPolicy.getMaxAttempts();
		final long sleepMillis = retryPolicy.getSleepMillis();
		Throwable lastException = null;
		try {
			for (int i = 0; i < maxAttempts; i++) {
				try {
					lastException = null;
					return retryCallable.call();
				} catch (Throwable e) {
					lastException = e;
				}
				if (sleepMillis > 0) {
					ThreadUtil.sleep(sleepMillis);
				}
			}
		} catch (Throwable e) {
			lastException = e;
		}
		if (lastException == null) {
			throw new RetryException("Exception in retry");
		}
		throw new RetryException("Exception in retry", lastException);
	}

	public static class RetryException extends RuntimeException {

		RetryException(String msg, Throwable cause) {
			super(msg, cause);
		}

		RetryException(String message) {
			super(message);
		}
	}
}
