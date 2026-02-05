package net.dreamlu.mica.ddddocr.config;

import net.dreamlu.mica.ddddocr.core.OCREngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

/**
 * OCR 引擎配置
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
public class OCREngineConfiguration {

	@Bean
	public OCREngine ocrEngine(ResourceLoader resourceLoader) {
		return new OCREngine(resourceLoader);
	}

}
