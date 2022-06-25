package net.dreamlu.mica.caffeine.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
	"spring.application.name=mica-caffeine-test"
})
public class CaffeineTest {

	@SpringBootApplication
	public static class App {

	}

	@Autowired
	private CacheManager cacheManager;

	@Test
	public void contextLoads() {
		Assert.assertNotNull(cacheManager);
		Cache cache = cacheManager.getCache("test#5m");
		Assert.assertNotNull(cache);
	}

}
