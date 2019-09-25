package net.dreamlu.mica.laytpl;

import lombok.Getter;
import lombok.Setter;
import net.dreamlu.mica.core.utils.DateUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * LayTpl配置
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
	/**
	 * 数字格式化，默认：#.00
	 */
	private String numPattern = "#.00";
	/**
	 * Date 日期格式化，默认："yyyy-MM-dd HH:mm:ss"
	 */
	private String datePattern = DateUtil.PATTERN_DATETIME;
	/**
	 * java8 LocalTime时间格式化，默认："HH:mm:ss"
	 */
	private String localTimePattern = DateUtil.PATTERN_TIME;
	/**
	 * java8 LocalDate日期格式化，默认："yyyy-MM-dd"
	 */
	private String localDatePattern = DateUtil.PATTERN_DATE;
	/**
	 * java8 LocalDateTime日期时间格式化，默认："yyyy-MM-dd HH:mm:ss"
	 */
	private String localDateTimePattern = DateUtil.PATTERN_DATETIME;
}
