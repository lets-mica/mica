package net.dreamlu.mica;

import net.dreamlu.mica.redis.cache.MicaRedisCache;
import net.dreamlu.mica.redis.cache.RedisCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * redis 测试
 *
 * @author L.cm
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
	"spring.application.name=mica-redis-test",
	"mica.redis.stream.enable=true",
	"mica.redis.rate-limiter.enable=true"
})
public class RedisApplicationTests {

	@SpringBootApplication
	public static class App {

//		@RStreamListener(name = "123")
//		public void test1(Record<String, Object> record) {
//			System.out.println(record);
//		}
//
//		@RStreamListener(name = "name")
//		public void test2(Record<String, Object> record) {
//			System.out.println(record);
//		}
//
//		@RStreamListener(name = "12311")
//		public void xxxx1(Record<String, User> record) {
//			System.out.println(record);
//		}
//
//		@RStreamListener(name = "12312")
//		public void xxxx2(Record<String, User> record) {
//			System.out.println(record);
//		}
//
//		@RStreamListener(name = "bytes", readRawBytes = true)
//		public void xxxx2(MapRecord<String, String, byte[]> record) {
//			System.out.println(record);
//		}

	}

//	@Autowired
//	private RStreamTemplate streamTemplate;
//	@Autowired
//	private RateLimiterClient rateLimiterClient;
//	@Autowired
//	private MicaRedisCache micaRedisCache;

	@Test
	public void contextLoads() {
//		User user = new User();
//		user.setId("123");
//		user.setAge(32);
//		user.setName("哈哈哈");
//		user.setTime(new Date());
//		streamTemplate.send("bytes", "abc", JsonUtil.toJsonAsBytes(user));
//		rateLimiterClient.isAllowed("test:1", 1, 10, TimeUnit.SECONDS);
//		Long bitCount = micaRedisCache.bitCount("mykey", 0, 1, RedisCommand.BitMapModel.BYTE);
//		System.out.println(bitCount);
	}

}
