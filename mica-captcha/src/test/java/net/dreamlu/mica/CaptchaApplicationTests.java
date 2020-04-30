package net.dreamlu.mica;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 验证码 测试
 *
 * @author L.cm
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Void.class)
@ActiveProfiles("dev")
public class CaptchaApplicationTests {

	@Test
	public void contextLoads() {

	}

}
