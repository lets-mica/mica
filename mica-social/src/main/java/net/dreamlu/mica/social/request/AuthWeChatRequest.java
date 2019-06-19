package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthResponse;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;
import net.dreamlu.mica.social.utils.UrlBuilder;

/**
 * 微信登录
 *
 * @author yangkai.shen (https://xkcoding.com), L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthWeChatRequest extends BaseAuthRequest {
	public AuthWeChatRequest(AuthConfig config) {
		super(config, AuthSource.WECHAT);
	}

	@Override
	public String authorize() {
		return UrlBuilder.getWeChatAuthorizeUrl(config.getClientId(), config.getRedirectUri());
	}

	/**
	 * 微信的特殊性，此时返回的信息同时包含 openid 和 access_token
	 *
	 * @param code 授权码
	 * @return 所有信息
	 */
	@Override
	protected AuthToken getAccessToken(String code) {
		String accessTokenUrl = UrlBuilder.getWeChatAccessTokenUrl(config.getClientId(), config.getClientSecret(), code);
		return this.getToken(accessTokenUrl);
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		String openId = authToken.getOpenId();
		JsonNode object = HttpRequest.get(UrlBuilder.getWeChatUserInfoUrl(accessToken, openId))
			.execute()
			.asJsonNode();

		this.checkResponse(object);

		return AuthUser.builder()
			.username(object.get("nickname").asText())
			.nickname(object.get("nickname").asText())
			.avatar(object.get("headimgurl").asText())
			.location(object.get("country").asText() + "-" + object.get("province").asText() + "-" + object.get("city").asText())
			.uuid(openId)
			.gender(AuthUserGender.getRealGender(object.get("sex").asText()))
			.token(authToken)
			.source(AuthSource.WECHAT)
			.build();
	}

	@Override
	public AuthResponse refresh(AuthToken oldToken) {
		String refreshTokenUrl = UrlBuilder.getWeChatRefreshUrl(config.getClientId(), oldToken.getRefreshToken());
		return AuthResponse.builder()
			.code(ResponseStatus.SUCCESS.getCode())
			.data(this.getToken(refreshTokenUrl))
			.build();
	}

	/**
	 * 检查响应内容是否正确
	 *
	 * @param object 请求响应内容
	 */
	private void checkResponse(JsonNode object) {
		if (object.has("errcode")) {
			throw new AuthException(object.get("errcode").asInt(), object.get("errmsg").asText());
		}
	}

	/**
	 * 获取token，适用于获取access_token和刷新token
	 *
	 * @param accessTokenUrl 实际请求token的地址
	 * @return token对象
	 */
	private AuthToken getToken(String accessTokenUrl) {
		JsonNode object = HttpRequest.get(accessTokenUrl)
			.execute()
			.asJsonNode();
		this.checkResponse(object);

		return AuthToken.builder()
			.accessToken(object.get("access_token").asText())
			.refreshToken(object.get("refresh_token").asText())
			.expireIn(object.get("expires_in").asInt())
			.openId(object.get("openid").asText())
			.build();
	}
}
