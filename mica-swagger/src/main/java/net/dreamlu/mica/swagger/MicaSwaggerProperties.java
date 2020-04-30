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

package net.dreamlu.mica.swagger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger 配置
 *
 * @author L.cm
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("mica.swagger")
public class MicaSwaggerProperties {

	/**
	 * 是否开启 swagger，默认：true
	 */
	private boolean enabled = true;
	/**
	 * 标题，默认：XXX服务
	 */
	private String title;
	/**
	 * 详情，默认：XXX服务
	 */
	private String description;
	/**
	 * 版本号，默认：V1.0
	 */
	private String version = "V1.0";
	/**
	 * 组织名
	 */
	private String contactUser;
	/**
	 * 组织url
	 */
	private String contactUrl;
	/**
	 * 组织邮箱
	 */
	private String contactEmail;
	/**
	 * 全局统一请求头
	 */
	private final List<Header> headers = new ArrayList<>();
	/**
	 * 全局统一鉴权配置
	 **/
	private final Authorization authorization = new Authorization();

	/**
	 * securitySchemes 支持方式之一 ApiKey
	 */
	@Getter
	@Setter
	public static class Authorization {
		/**
		 * 开启Authorization，默认：false
		 */
		private Boolean enabled = false;
		/**
		 * 鉴权策略ID，对应 SecurityReferences ID，默认：Authorization
		 */
		private String name = "Authorization";
		/**
		 * 鉴权传递的Header参数，默认：TOKEN
		 */
		private String keyName = "TOKEN";
		/**
		 * 需要开启鉴权URL的正则，默认：^.*$
		 */
		private String authRegex = "^.*$";
	}

	/**
	 * 全局通用请求头
	 */
	@Getter
	@Setter
	public static class Header {
		/**
		 * 请求头名
		 */
		private String name;
		/**
		 * 请求头描述
		 */
		private String description;
		/**
		 * 是否必须，默认：false
		 */
		private boolean required = false;
	}
}
