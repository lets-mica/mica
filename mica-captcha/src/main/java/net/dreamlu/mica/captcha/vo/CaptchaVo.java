package net.dreamlu.mica.captcha.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 验证码模型
 *
 * @author L.cm
 */
@Getter
@RequiredArgsConstructor
public class CaptchaVo {
	private final String uuid;
	private final String base64;
}
