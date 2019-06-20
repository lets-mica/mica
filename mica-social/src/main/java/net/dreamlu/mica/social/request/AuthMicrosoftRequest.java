package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthResponse;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import org.springframework.web.util.UriComponentsBuilder;

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
	public String authorize(String state) {
		return UriComponentsBuilder.fromUriString(authSource.authorize())
			.queryParam("response_type", "code")
			.queryParam("client_id", config.getClientId())
			.queryParam("redirect_uri", config.getRedirectUri())
			.queryParam("state", state)
			.queryParam("response_mode", "query")
			.queryParam("scope", "offline_access%20user.read%20mail.read")
			.build()
			.toUriString();
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		JsonNode jsonNode = HttpRequest.post(authSource.accessToken())
			.formBuilder()
			.add("client_id", config.getClientId())
			.add("client_secret", config.getClientSecret())
			.add("redirect_uri", config.getRedirectUri())
			.add("scope", "user.read mail.read")
			.add("code", code)
			.add("grant_type", "authorization_code")
			.execute()
			.asJsonNode();
		return getToken(jsonNode);
	}

	/**
	 * 获取token，适用于获取access_token和刷新token
	 *
	 * @param object JsonNode
	 * @return token对象
	 */
	private AuthToken getToken(JsonNode object) {
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
		JsonNode object = HttpRequest.get(authSource.userInfo())
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
		JsonNode jsonNode = HttpRequest.post(authSource.accessToken())
			.formBuilder()
			.add("client_id", config.getClientId())
			.add("client_secret", config.getClientSecret())
			.add("redirect_uri", config.getRedirectUri())
			.add("scope", "user.read mail.read")
			.add("refresh_token", authToken.getRefreshToken())
			.add("grant_type", "refresh_token")
			.execute()
			.asJsonNode();
		return AuthResponse.builder().code(ResponseStatus.SUCCESS.getCode()).data(getToken(jsonNode)).build();
	}
}
