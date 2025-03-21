package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * uuid 测试
 *
 * @author L.cm
 */
class UUIDTest {
	private static final int size = 1000_0000;

	@Test
	void test() throws InterruptedException {
		ExecutorService service = Executors.newFixedThreadPool(100);
		Set<String> uuidSet = ConcurrentHashMap.newKeySet();
		for (int i = 0; i < size; i++) {
			service.submit(() -> {
				String nanoId = StringUtil.getNanoId();
				if (uuidSet.contains(nanoId)) {
					System.out.println("-----------存在冲突-------");
				} else {
					uuidSet.add(nanoId);
				}
			});
		}
		service.awaitTermination(10, TimeUnit.SECONDS);
		Assertions.assertEquals(size, uuidSet.size());
		service.shutdown();
	}

	@Test
	void testUUID() throws InterruptedException {
		ExecutorService service = Executors.newFixedThreadPool(100);
		Set<String> uuidSet = ConcurrentHashMap.newKeySet();
		for (int i = 0; i < size; i++) {
			service.submit(() -> {
				String uuid = StringUtil.getUUID();
				if (uuidSet.contains(uuid)) {
					System.out.println("-----------存在冲突-------");
				} else {
					uuidSet.add(uuid);
				}
			});
		}
		service.awaitTermination(20, TimeUnit.SECONDS);
		Assertions.assertEquals(size, uuidSet.size());
		service.shutdown();
	}

	@Test
	void testID() throws InterruptedException {
		ExecutorService service = Executors.newFixedThreadPool(100);
		Set<String> uuidSet = ConcurrentHashMap.newKeySet();
		for (int i = 0; i < size; i++) {
			service.submit(() -> {
				String uuid = StringUtil.getFastId(10);
				if (uuidSet.contains(uuid)) {
					System.out.println("-----------存在冲突-------");
				} else {
					uuidSet.add(uuid);
				}
			});
		}
		service.awaitTermination(10, TimeUnit.SECONDS);
		Assertions.assertEquals(size, uuidSet.size());
		service.shutdown();
	}

}
