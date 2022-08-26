package net.dreamlu.mica;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * redis 测试
 *
 * @author L.cm
 */
@SpringBootTest(properties = {
	"spring.application.name=mica-redis-test",
	"mica.redis.stream.enable=true",
	"mica.redis.rate-limiter.enable=true"
})
class RedisApplicationTests {

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
	void contextLoads() {
//		List<String> test = new ArrayList<>();
//		test.add("test1");
//		test.add("test1");
//		micaRedisCache.lPush("test1", 123, 123);
//		micaRedisCache.lPush("test1", test);
//		micaRedisCache.lPush("test2", Arrays.asList("123", "456"));
//		micaRedisCache.hmGet("test1", 123);
//		micaRedisCache.hmGet("test1", test);
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
