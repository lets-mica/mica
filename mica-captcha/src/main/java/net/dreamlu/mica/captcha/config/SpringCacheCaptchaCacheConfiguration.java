package net.dreamlu.mica.captcha.config;

import net.dreamlu.mica.captcha.cache.ICaptchaCache;
import net.dreamlu.mica.captcha.cache.SpringCacheCaptchaCache;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;

/**
 * 验证码 spring cache 配置
 *
 * @author L.cm
 */
@AutoConfiguration
public class SpringCacheCaptchaCacheConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public ICaptchaCache captchaCache(MicaCaptchaProperties properties,
									  CacheManager cacheManager) {
		return new SpringCacheCaptchaCache(properties, cacheManager);
	}

}
