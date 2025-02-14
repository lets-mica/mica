package net.dreamlu.mica.caffeine.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.concurrent.TimeUnit;

@SpringBootTest(properties = {
	"spring.application.name=mica-caffeine-test"
})
class CaffeineTest {

	@SpringBootApplication
	public static class App {

	}

	@Autowired
	private CacheManager cacheManager;

	@Test
	void contextLoads() throws InterruptedException {
		Assertions.assertNotNull(cacheManager);
		Cache cache = cacheManager.getCache("test#3s");
		String cacheKey = "key";
		cache.put(cacheKey, "aaa");
		Assertions.assertNotNull(cache.get(cacheKey));
		TimeUnit.SECONDS.sleep(3);
		Assertions.assertNull(cache.get(cacheKey));
	}

}
