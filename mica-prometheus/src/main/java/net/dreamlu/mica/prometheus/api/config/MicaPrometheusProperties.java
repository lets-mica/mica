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

package net.dreamlu.mica.prometheus.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * prometheus http sd 配置
 *
 * @author L.cm
 */
@Getter
@Setter
@ConfigurationProperties(MicaPrometheusProperties.PREFIX)
public class MicaPrometheusProperties {
	public static final String PREFIX = "mica.prometheus";

	/**
	 * http sd basic auth 配置
	 */
	private BasicAuth basicAuth = new BasicAuth();

	/**
	 * http sd basic auth
	 */
	@Getter
	@Setter
	public static class BasicAuth {
		/**
		 * 是否开启 basic auth，默认：false
		 */
		private boolean enabled = false;
		/**
		 * basic auth 用户名
		 */
		private String username;
		/**
		 * basic auth 密码
		 */
		private String password;
	}

}