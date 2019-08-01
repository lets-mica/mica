package net.dreamlu.mica.laytpl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 验证码配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("mica.laytpl")
public class MicaLayTplProperties {

	/**
	 * 模板分隔符开始，默认：{{
	 */
	private String open = "{{";
	/**
	 * 模板分隔符结束，默认：}}
	 */
	private String close = "}}";
	/**
	 * 模板前缀，默认：classpath:templates/tpl/
	 */
	private String prefix = "classpath:templates/tpl/";
	/**
	 * 缓存模板，默认：true
	 */
	private boolean cache = true;

}
