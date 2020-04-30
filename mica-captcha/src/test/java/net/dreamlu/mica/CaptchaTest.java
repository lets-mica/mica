package net.dreamlu.mica;

import net.dreamlu.mica.captcha.core.Captcha;
import net.dreamlu.mica.captcha.enums.CaptchaType;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 验证码测试
 *
 * @author L.cm
 */
public class CaptchaTest {

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			Captcha captcha = new Captcha(CaptchaType.MATH);
			final int aa = i;
			String code = captcha.generate(() -> {
				try {
					return new FileOutputStream("/Users/lcm/Desktop/test/" + aa + ".jpg");
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
			});
			System.out.println(code);
		}
	}
}
