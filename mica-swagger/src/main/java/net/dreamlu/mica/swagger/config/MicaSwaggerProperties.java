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

package net.dreamlu.mica.swagger.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import springfox.documentation.service.AuthorizationScope;

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
@ConfigurationProperties(MicaSwaggerProperties.PREFIX)
public class MicaSwaggerProperties {
	public static final String PREFIX = "mica.swagger";

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
	 * api key 认证
	 **/
	private final Authorization authorization = new Authorization();
	/**
	 * oauth2 认证
	 */
	private final Oauth2 oauth2 = new Oauth2();

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
		 * 鉴权传递的Header参数，默认：Authorization
		 */
		private String keyName = "Authorization";
		/**
		 * 需要开启鉴权URL的正则，默认：/**
		 */
		private List<String> pathPatterns = new ArrayList<>();
	}

	/**
	 * oauth2 认证
	 */
	@Getter
	@Setter
	public static class Oauth2 {
		/**
		 * 开启Oauth2，默认：false
		 */
		private Boolean enabled = false;
		/**
		 * oath2 名称，默认：oauth2
		 */
		private String name = "oauth2";
		/**
		 * clientId name
		 */
		private String clientIdName;
		/**
		 * clientSecret name
		 */
		private String clientSecretName;
		/**
		 * authorize url
		 */
		private String authorizeUrl;
		/**
		 * token url
		 */
		private String tokenUrl;
		/**
		 * token name，默认：access_token
		 */
		private String tokenName = "access_token";
		/**
		 * 授权类型
		 */
		private GrantTypes grantType = GrantTypes.AUTHORIZATION_CODE;
		/**
		 * oauth2 scope 列表
		 */
		private List<AuthorizationScope> scopes = new ArrayList<>();
		/**
		 * 需要开启鉴权URL的正则，默认：/**
		 */
		private List<String> pathPatterns = new ArrayList<>();
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

	/**
	 * oauth2 认证类型
	 */
	public enum GrantTypes {
		/**
		 * authorization_code
		 */
		AUTHORIZATION_CODE,
		/**
		 * client_credentials
		 */
		CLIENT_CREDENTIALS,
		/**
		 * implicit
		 */
		IMPLICIT,
		/**
		 * Password
		 */
		PASSWORD;
	}
}
