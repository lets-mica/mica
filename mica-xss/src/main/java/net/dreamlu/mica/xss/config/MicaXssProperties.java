/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
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

package net.dreamlu.mica.xss.config;

import lombok.Getter;
import lombok.Setter;
import net.dreamlu.mica.xss.core.XssType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Xss配置类
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(MicaXssProperties.PREFIX)
public class MicaXssProperties {
	public static final String PREFIX = "mica.xss";

	/**
	 * 开启xss
	 */
	private boolean enabled = true;
	/**
	 * 全局：对文件进行首尾 trim
	 */
	@Deprecated
	private boolean trimText = true;
	/**
	 * 全局：{@link XssType#FORM}配置
	 */
	private FormConfig form = new FormConfig();
	/**
	 * 全局：{@link XssType#JACKSON}配置
	 */
	private JacksonConfig jackson = new JacksonConfig();
	/**
	 * 模式：clear 清理（默认），escape 转义
	 */
	private Mode mode = Mode.CLEAR;
	/**
	 * [clear 专用] prettyPrint，默认关闭： 保留换行
	 */
	private boolean prettyPrint = false;
	/**
	 * [clear 专用] 使用转义，默认关闭
	 */
	private boolean enableEscape = false;
	/**
	 * 拦截的路由，默认为空
	 */
	private List<String> pathPatterns = new ArrayList<>();
	/**
	 * 放行的路由，默认为空
	 */
	private List<String> pathExcludePatterns = new ArrayList<>();

	public enum Mode {
		/**
		 * 清理
		 */
		CLEAR,
		/**
		 * 转义
		 */
		ESCAPE,
		/**
		 * 校验，抛出异常
		 */
		VALIDATE
	}

	@Getter
	@Setter
	public static class FormConfig {
		/**
		 * 对字符串首尾 trim
		 */
		boolean trimText = true;
		/**
		 * 对字符串去除特殊字符
		 */
		String charsToDelete = "";
		/**
		 * 对字符串trim后，如果为空字符串，转null
		 */
		boolean emptyAsNull = false;
		/**
		 * [charsToDelete, trimText, emptyAsNull]三个规则，对集合中的元素也生效
		 */
		boolean enableInCollection = false;
	}

	@Getter
	@Setter
	public static class JacksonConfig {
		/**
		 * 对字符串首尾 trim
		 */
		boolean trimText = true;
		/**
		 * 对字符串去除特殊字符
		 */
		String charsToDelete = "";
		/**
		 * 对字符串trim后，如果为空字符串，转null
		 */
		boolean emptyAsNull = false;
	}
}
