package net.dreamlu.mica.social;

import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.request.*;
import net.dreamlu.mica.social.utils.AuthConfigChecker;
import org.springframework.util.Assert;

/**
 * Auth Request 工厂
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class AuthRequestFactory {
	private final MicaSocialProperties properties;

	/**
	 * 获取 AuthRequest
	 *
	 * @param source source
	 * @return AuthRequest
	 */
	public BaseAuthRequest get(String source) {
		Assert.hasLength(source, "source 不能为空。");
		AuthSource authSource = AuthSource.of(source);
		Assert.notNull(authSource, "不支持的 source 类型："+ source + "。");
		switch (authSource) {
			case QQ:
				return authQqRequest(properties);
			case MI:
				return authMiRequest(properties);
			case CSDN:
				return authCsdnRequest(properties);
			case BAIDU:
				return authBaiduRequest(properties);
			case GITEE:
				return authGiteeRequest(properties);
			case WEIBO:
				return authWeiboRequest(properties);
			case ALIPAY:
				return authAlipayRequest(properties);
			case CODING:
				return authCodingRequest(properties);
			case DOUYIN:
				return authDouyinRequest(properties);
			case GITHUB:
				return authGithubRequest(properties);
			case GOOGLE:
				return authGoogleRequest(properties);
			case TAOBAO:
				return authTaobaoRequest(properties);
			case WECHAT:
				return authWeChatRequest(properties);
			case OSCHINA:
				return authOschinaRequest(properties);
			case TOUTIAO:
				return authToutiaoRequest(properties);
			case DINGTALK:
				return authDingTalkRequest(properties);
			case FACEBOOK:
				return authFacebookRequest(properties);
			case LINKEDIN:
				return authLinkedinRequest(properties);
			case MICROSOFT:
				return authMicrosoftRequest(properties);
			case TENCENT_CLOUD:
				return authTencentCloudRequest(properties);
			default:
				return null;
		}
	}

	private BaseAuthRequest authQqRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getQq();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthQqRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authBaiduRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getBaidu();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthBaiduRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authWeiboRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getWeibo();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthWeiboRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authWeChatRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getWechat();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthWeChatRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authGithubRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getGithub();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthGithubRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authGiteeRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getGitee();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthGiteeRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authCodingRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getCoding();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthCodingRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authTencentCloudRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getTencentCloud();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthTencentCloudRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authOschinaRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getOschina();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthOschinaRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authGoogleRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getGoogle();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthGoogleRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authFacebookRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getFacebook();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthFacebookRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authLinkedinRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getLinkedin();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthLinkedinRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authToutiaoRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getToutiao();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthToutiaoRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authTaobaoRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getTaobao();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthTaobaoRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authCsdnRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getCsdn();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthCsdnRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authMiRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getMi();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthMiRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authMicrosoftRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getMicrosoft();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthMicrosoftRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authAlipayRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getAlipay();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthAlipayRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authDingTalkRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getDingTalk();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthDingTalkRequest(authConfig);
		}
		return null;
	}

	private BaseAuthRequest authDouyinRequest(MicaSocialProperties properties) {
		AuthConfig authConfig = properties.getDouyin();
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthDouyinRequest(authConfig);
		}
		return null;
	}
}
