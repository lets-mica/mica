package net.dreamlu.mica.social.request;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 支付宝登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthAlipayRequest extends BaseAuthRequest {
	private static final AuthSource API_URL = AuthSource.ALIPAY;
	private AlipayClient alipayClient;

	public AuthAlipayRequest(AuthConfig config) {
		super(config, API_URL);
		this.alipayClient = new DefaultAlipayClient(API_URL.accessToken(), config.getClientId(), config.getClientSecret(), "json", "UTF-8", config.getAlipayPublicKey(), "RSA2");
	}

	@Override
	public String authorize(String state) {
		return UriComponentsBuilder.fromUriString(authSource.authorize())
			.queryParam("app_id", config.getClientId())
			.queryParam("scope", "auth_user")
			.queryParam("redirect_uri", config.getRedirectUri())
			.queryParam("state", state)
			.build()
			.toUriString();
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setGrantType("authorization_code");
		request.setCode(code);
		AlipaySystemOauthTokenResponse response = null;
		try {
			response = this.alipayClient.execute(request);
		} catch (Exception e) {
			throw new AuthException("Unable to get token from alipay using code [" + code + "]", e);
		}
		if (!response.isSuccess()) {
			throw new AuthException(response.getSubMsg());
		}
		return AuthToken.builder()
			.accessToken(response.getAccessToken())
			.uid(response.getUserId())
			.expireIn(Integer.parseInt(response.getExpiresIn()))
			.refreshToken(response.getRefreshToken())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
		AlipayUserInfoShareResponse response = null;
		try {
			response = this.alipayClient.execute(request, accessToken);
		} catch (AlipayApiException e) {
			throw new AuthException(e.getErrMsg(), e);
		}
		if (!response.isSuccess()) {
			throw new AuthException(response.getSubMsg());
		}
		String province = response.getProvince();
		String city = response.getCity();
		return AuthUser.builder()
			.uuid(response.getUserId())
			.username(StringUtil.isBlank(response.getUserName()) ? response.getNickName() : response.getUserName())
			.nickname(response.getNickName())
			.avatar(response.getAvatar())
			.location(String.format("%s %s", StringUtil.isBlank(province) ? "" : province, StringUtil.isBlank(city) ? "" : city))
			.gender(AuthUserGender.getRealGender(response.getGender()))
			.token(authToken)
			.source(API_URL)
			.build();
	}

}
