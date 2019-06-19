package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthResponse;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.utils.GlobalAuthUtil;
import net.dreamlu.mica.social.utils.UrlBuilder;
import okhttp3.FormBody;

/**
 * 微软登录
 *
 * @author yangkai.shen (https://xkcoding.com), L.cm
 * @version 1.5
 * @since 1.5
 */
public class AuthMicrosoftRequest extends BaseAuthRequest {
	public AuthMicrosoftRequest(AuthConfig config) {
		super(config, AuthSource.MICROSOFT);
	}

	@Override
	public String authorize() {
		return UrlBuilder.getMicrosoftAuthorizeUrl(config.getClientId(), config.getRedirectUri());
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		String accessTokenUrl = UrlBuilder.getMicrosoftAccessTokenUrl(config.getClientId(), config.getClientSecret(), config
			.getRedirectUri(), code);

		return getToken(accessTokenUrl);
	}

	/**
	 * 获取token，适用于获取access_token和刷新token
	 *
	 * @param accessTokenUrl 实际请求token的地址
	 * @return token对象
	 */
	private AuthToken getToken(String accessTokenUrl) {
		FormBody.Builder formBuilder = new FormBody.Builder();
		GlobalAuthUtil.parseStringToMap(accessTokenUrl)
			.forEach(formBuilder::add);
		JsonNode object = HttpRequest.post(accessTokenUrl)
			.addHeader("Host", "https://login.microsoftonline.com")
			.addHeader("Content-Type", "application/x-www-form-urlencoded")
			.body(formBuilder.build())
			.execute()
			.asJsonNode();

		this.checkResponse(object);

		return AuthToken.builder()
			.accessToken(object.get("access_token").asText())
			.expireIn(object.get("expires_in").asInt())
			.scope(object.get("scope").asText())
			.tokenType(object.get("token_type").asText())
			.refreshToken(object.get("refresh_token").asText())
			.build();
	}

	private void checkResponse(JsonNode response) {
		if (response.has("error")) {
			throw new AuthException(response.get("error_description").asText());
		}
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String token = authToken.getAccessToken();
		String tokenType = authToken.getTokenType();
		String jwt = tokenType + " " + token;
		JsonNode object = HttpRequest.get(UrlBuilder.getMicrosoftUserInfoUrl())
			.addHeader("Authorization", jwt)
			.execute()
			.asJsonNode();

		return AuthUser.builder()
			.uuid(object.get("id").asText())
			.username(object.get("userPrincipalName").asText())
			.nickname(object.get("displayName").asText())
			.location(object.get("officeLocation").asText())
			.email(object.get("mail").asText())
			.token(authToken)
			.source(AuthSource.MICROSOFT)
			.build();
	}

	/**
	 * 刷新access token （续期）
	 *
	 * @param authToken 登录成功后返回的Token信息
	 * @return AuthResponse
	 */
	@Override
	public AuthResponse refresh(AuthToken authToken) {
		String refreshTokenUrl = UrlBuilder.getMicrosoftRefreshUrl(config.getClientId(), config.getClientSecret(), config
			.getRedirectUri(), authToken.getRefreshToken());

		return AuthResponse.builder().code(ResponseStatus.SUCCESS.getCode()).data(getToken(refreshTokenUrl)).build();
	}
}
