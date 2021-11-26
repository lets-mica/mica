package net.dreamlu.mica.core.retry;

import java.io.Serializable;

/**
 * Callback interface for an operation that can be retried using a
 *
 * @param <T> the type of object returned by the callback
 * @param <E> the type of exception it declares may be thrown
 * @author Rob Harrop
 * @author Dave Syer
 */
public interface RetryCallback<T, E extends Throwable> extends Serializable {

	/**
	 * Execute an operation with retry semantics. Operations should generally be
	 * idempotent, but implementations may choose to implement compensation semantics when
	 * an operation is retried.
	 *
	 * @return the result of the successful operation.
	 * @throws E of type E if processing fails
	 */
	T call() throws E;

}
