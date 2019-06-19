package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthResponse;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.utils.UrlBuilder;


/**
 * 抖音登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthDouyinRequest extends BaseAuthRequest {

	public AuthDouyinRequest(AuthConfig config) {
		super(config, AuthSource.DOUYIN);
	}

	@Override
	public String authorize() {
		return UrlBuilder.getDouyinAuthorizeUrl(config.getClientId(), config.getRedirectUri());
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		String accessTokenUrl = UrlBuilder.getDouyinAccessTokenUrl(config.getClientId(), config.getClientSecret(), code);
		return this.getToken(accessTokenUrl);
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		String openId = authToken.getOpenId();

		JsonNode object = HttpRequest.get(UrlBuilder.getDouyinUserInfoUrl(accessToken, openId))
			.execute()
			.asJsonNode();
		JsonNode userInfoObject = this.checkResponse(object);

		return AuthUser.builder()
			.uuid(userInfoObject.get("open_id").asText())
			.username(userInfoObject.get("nickname").asText())
			.nickname(userInfoObject.get("nickname").asText())
			.avatar(userInfoObject.get("avatar").asText())
			.token(authToken)
			.source(AuthSource.DOUYIN)
			.build();
	}

	@Override
	public AuthResponse refresh(AuthToken oldToken) {
		String refreshTokenUrl = UrlBuilder.getDouyinRefreshUrl(config.getClientId(), oldToken.getRefreshToken());
		return AuthResponse.builder()
			.code(ResponseStatus.SUCCESS.getCode())
			.data(this.getToken(refreshTokenUrl))
			.build();
	}

	/**
	 * 检查响应内容是否正确
	 *
	 * @param object 请求响应内容
	 * @return 实际请求数据的json对象
	 */
	private JsonNode checkResponse(JsonNode object) {
		String message = object.get("message").asText();
		JsonNode data = object.get("data");
		int errorCode = data.get("error_code").asInt();
		if ("error".equals(message) || errorCode != 0) {
			throw new AuthException(errorCode, data.get("description").asText());
		}
		return data;
	}

	/**
	 * 获取token，适用于获取access_token和刷新token
	 *
	 * @param accessTokenUrl 实际请求token的地址
	 * @return token对象
	 */
	private AuthToken getToken(String accessTokenUrl) {
		JsonNode object = HttpRequest.post(accessTokenUrl)
			.execute()
			.asJsonNode();
		JsonNode accessTokenObject = this.checkResponse(object);
		return AuthToken.builder()
			.accessToken(accessTokenObject.get("access_token").asText())
			.openId(accessTokenObject.get("open_id").asText())
			.expireIn(accessTokenObject.get("expires_in").asInt())
			.refreshToken(accessTokenObject.get("refresh_token").asText())
			.scope(accessTokenObject.get("scope").asText())
			.build();
	}
}
