package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthResponse;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;


/**
 * Teambition授权登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com) L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthTeambitionRequest extends AuthDefaultRequest {

	public AuthTeambitionRequest(AuthConfig config) {
		super(config, AuthSource.TEAMBITION);
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		JsonNode jsonNode = HttpRequest.post(authSource.accessToken())
			.formBuilder()
			.add("client_id", config.getClientId())
			.add("client_secret", config.getClientSecret())
			.add("code", code)
			.add("grant_type", "code")
			.execute()
			.asJsonNode();

		this.checkResponse(jsonNode);

		return AuthToken.builder()
			.accessToken(jsonNode.get("access_token").asText())
			.refreshToken(jsonNode.get("refresh_token").asText())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		JsonNode jsonNode = HttpRequest.get(authSource.userInfo())
			.addHeader("Authorization", "OAuth2 " + accessToken)
			.execute()
			.asJsonNode();

		this.checkResponse(jsonNode);

		authToken.setUid(jsonNode.get("_id").asText());
		return AuthUser.builder()
			.uuid(jsonNode.get("_id").asText())
			.username(jsonNode.get("name").asText())
			.nickname(jsonNode.get("name").asText())
			.avatar(jsonNode.at("/avatarUrl").asText())
			.blog(jsonNode.at("/website").asText())
			.location(jsonNode.at("/location").asText())
			.email(jsonNode.at("/email").asText())
			.gender(AuthUserGender.UNKNOWN)
			.token(authToken)
			.source(authSource)
			.build();
	}

	@Override
	public AuthResponse refresh(AuthToken oldToken) {
		String uid = oldToken.getUid();
		String refreshToken = oldToken.getRefreshToken();
		JsonNode jsonNode = HttpRequest.post(authSource.refresh())
			.formBuilder()
			.add("_userId", uid)
			.add("refresh_token", refreshToken)
			.execute()
			.asJsonNode();

		this.checkResponse(jsonNode);

		return AuthResponse.builder()
			.code(ResponseStatus.SUCCESS.getCode())
			.data(AuthToken.builder()
				.accessToken(jsonNode.get("access_token").asText())
				.refreshToken(jsonNode.get("refresh_token").asText())
				.build())
			.build();
	}

	/**
	 * 检查响应内容是否正确
	 *
	 * @param jsonNode 请求响应内容
	 */
	private void checkResponse(JsonNode jsonNode) {
		if ((jsonNode.has("message") && jsonNode.has("name"))) {
			throw new AuthException(jsonNode.get("name").asText() + ", " + jsonNode.get("message").asText());
		}
	}
}
