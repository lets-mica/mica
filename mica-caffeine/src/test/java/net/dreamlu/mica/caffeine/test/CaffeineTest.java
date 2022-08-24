package net.dreamlu.mica.caffeine.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

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
	void contextLoads() {
		Assertions.assertNotNull(cacheManager);
		Cache cache = cacheManager.getCache("test#5m");
		Assertions.assertNotNull(cache);
	}

}
