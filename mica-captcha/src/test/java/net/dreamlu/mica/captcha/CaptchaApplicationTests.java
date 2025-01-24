package net.dreamlu.mica.captcha;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ActiveProfiles;

/**
 * 验证码 测试
 *
 * @author L.cm
 */
@SpringBootTest
@ActiveProfiles("dev")
class CaptchaApplicationTests {

	@SpringBootApplication
	@EnableCaching
	public static class CaptchaApplication {

		public static void main(String[] args) {
			SpringApplication.run(CaptchaApplication.class);
		}

	}


	@Test
	void contextLoads() {

	}

}
