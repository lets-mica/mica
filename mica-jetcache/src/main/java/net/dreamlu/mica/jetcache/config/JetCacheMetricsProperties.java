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

package net.dreamlu.mica.jetcache.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * jetcache metrics 配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(JetCacheMetricsProperties.PREFIX)
public class JetCacheMetricsProperties {
	public static final String PREFIX = "jetcache.metrics";

	/**
	 * 开启 jetcache metrics，默认：true
	 */
	private boolean enabled = true;
	/**
	 * 开启 StatInfoLogger
	 */
	private boolean enabledStatInfoLogger;
	/**
	 * StatInfoLogger 打印明细，默认：false
	 */
	private boolean verboseLog = false;

}
