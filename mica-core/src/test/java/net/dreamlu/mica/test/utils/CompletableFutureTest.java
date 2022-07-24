package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * CompletableFutureTest
 *
 * @author L.cm
 */
public class CompletableFutureTest {

	@Test
	public void test1() {
		CompletableFuture<String> future = new CompletableFuture<>();
		String value = "123";
		new Thread(() -> {
			ThreadUtil.sleep(1000);
			future.complete(value);
		}).start();
		Assert.assertEquals(value, future.join());
	}

	@Test(expected = CompletionException.class)
	public void test2() {
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
	}

}
