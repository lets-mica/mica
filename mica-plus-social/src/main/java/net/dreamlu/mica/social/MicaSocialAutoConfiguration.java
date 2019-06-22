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

import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.request.*;
import net.dreamlu.mica.social.utils.AuthConfigChecker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 第三方社交登录自动配置
 *
 * @author L.cm
 */
@Configuration
@EnableConfigurationProperties(MicaSocialProperties.class)
public class MicaSocialAutoConfiguration {

	@Bean
	public AuthQqRequest authQqRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getQq();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthQqRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthBaiduRequest authBaiduRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getBaidu();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthBaiduRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthWeiboRequest authWeiboRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getWeibo();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthWeiboRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthWeChatRequest weChatRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getWechat();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthWeChatRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthGithubRequest authGithubRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getGithub();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthGithubRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthGiteeRequest authGiteeRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getGitee();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthGiteeRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthCodingRequest authCodingRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getCoding();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthCodingRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthTencentCloudRequest authTencentCloudRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getTencentCloud();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthTencentCloudRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthOschinaRequest authOschinaRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getOschina();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthOschinaRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthGoogleRequest authGoogleRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getGoogle();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthGoogleRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthFacebookRequest authFacebookRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getFacebook();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthFacebookRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthLinkedinRequest authLinkedinRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getLinkedin();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthLinkedinRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthToutiaoRequest authToutiaoRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getToutiao();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthToutiaoRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthCsdnRequest authCsdnRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getCsdn();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthCsdnRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthMiRequest authMiRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getMi();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthMiRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthMicrosoftRequest authMicrosoftRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getMicrosoft();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthMicrosoftRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthAlipayRequest authAlipayRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getAlipay();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthAlipayRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthDingTalkRequest authDingTalkRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getDingTalk();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthDingTalkRequest(authConfig);
		}
		return null;
	}

	@Bean
	public AuthDouyinRequest authDouyinRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getDouyin();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthDouyinRequest(authConfig);
		}
		return null;
	}

}
