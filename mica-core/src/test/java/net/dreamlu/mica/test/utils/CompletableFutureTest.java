package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * CompletableFutureTest
 *
 * @author L.cm
 */
class CompletableFutureTest {

	@Test
	void test1() {
		CompletableFuture<String> future = new CompletableFuture<>();
		String value = "123";
		new Thread(() -> {
			ThreadUtil.sleep(1000);
			future.complete(value);
		}).start();
		Assertions.assertEquals(value, future.join());
	}

	@Test
	void test2() {
		Assertions.assertThrows(CompletionException.class, () -> {
			CompletableFuture<String> future = new CompletableFuture<>();
			new Thread(() -> {
				try {
					int a = 1 / 0;
					future.complete("123");
				} catch (Exception e) {
					future.completeExceptionally(e);
				}
			}).start();
			System.out.println(future.join());
		});
	}

}
