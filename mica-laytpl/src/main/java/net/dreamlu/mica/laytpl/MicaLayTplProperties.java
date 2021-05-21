/*
 * Copyright (c) 2019-2029, Dreamlu (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
@ConfigurationProperties(MicaLayTplProperties.PREFIX)
public class MicaLayTplProperties {
	public static final String PREFIX = "mica.laytpl";

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
