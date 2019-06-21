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

package net.dreamlu.mica.social;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import net.dreamlu.mica.social.config.AuthConfig;

/**
 * 第三方社交登录配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("mica.social")
public class MicaSocialProperties {
	/**
	 * QQ 配置
	 */
	private AuthConfig qq;

	/**
	 * github 配置
	 */
	private AuthConfig github;

	/**
	 * 微信 配置
	 */
	private AuthConfig wechat;

	/**
	 * Google 配置
	 */
	private AuthConfig google;

	/**
	 * Microsoft 配置
	 */
	private AuthConfig microsoft;

	/**
	 * Mi 配置
	 */
	private AuthConfig mi;
}
