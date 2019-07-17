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
	public AuthRequest get(String source) {
		Assert.hasLength(source, "source 不能为空。");
		AuthSource authSource = AuthSource.of(source);
		Assert.notNull(authSource, "不支持的 source 类型："+ source + "。");
		switch (authSource) {
			case QQ:
				return authQqRequest(properties.getQq());
			case MI:
				return authMiRequest(properties.getMi());
			case CSDN:
				return authCsdnRequest(properties.getCsdn());
			case BAIDU:
				return authBaiduRequest(properties.getBaidu());
			case GITEE:
				return authGiteeRequest(properties.getGitee());
			case WEIBO:
				return authWeiboRequest(properties.getWeibo());
			case ALIPAY:
				return authAlipayRequest(properties.getAlipay());
			case CODING:
				return authCodingRequest(properties.getCoding());
			case DOUYIN:
				return authDouyinRequest(properties.getDouyin());
			case GITHUB:
				return authGithubRequest(properties.getGithub());
			case GOOGLE:
				return authGoogleRequest(properties.getGoogle());
			case TAOBAO:
				return authTaobaoRequest(properties.getTaobao());
			case WECHAT:
				return authWeChatRequest(properties.getWechat());
			case OSCHINA:
				return authOschinaRequest(properties.getOschina());
			case TOUTIAO:
				return authToutiaoRequest(properties.getToutiao());
			case DINGTALK:
				return authDingTalkRequest(properties.getDingTalk());
			case FACEBOOK:
				return authFacebookRequest(properties.getFacebook());
			case LINKEDIN:
				return authLinkedinRequest(properties.getLinkedin());
			case MICROSOFT:
				return authMicrosoftRequest(properties.getMicrosoft());
			case TENCENT_CLOUD:
				return authTencentCloudRequest(properties.getTencentCloud());
			case TEAMBITION:
				return authTeambitionRequest(properties.getTeambition());
			default:
				return null;
		}
	}

	private AuthRequest authQqRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthQqRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authBaiduRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthBaiduRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authWeiboRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthWeiboRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authWeChatRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthWeChatRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authGithubRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthGithubRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authGiteeRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthGiteeRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authCodingRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthCodingRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authTencentCloudRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthTencentCloudRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authOschinaRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthOschinaRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authGoogleRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthGoogleRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authFacebookRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthFacebookRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authLinkedinRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthLinkedinRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authToutiaoRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthToutiaoRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authTaobaoRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthTaobaoRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authCsdnRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthCsdnRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authMiRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthMiRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authMicrosoftRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthMicrosoftRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authAlipayRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthAlipayRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authDingTalkRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthDingTalkRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authDouyinRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthDouyinRequest(authConfig);
		}
		return null;
	}

	private AuthRequest authTeambitionRequest(AuthConfig authConfig) {
		if (AuthConfigChecker.isSupportedAuth(authConfig)) {
			return new AuthTeambitionRequest(authConfig);
		}
		return null;
	}

}
