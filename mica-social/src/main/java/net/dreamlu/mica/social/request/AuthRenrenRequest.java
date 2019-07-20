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

import java.util.Objects;

import static net.dreamlu.mica.core.result.SystemCode.SUCCESS;

/**
 * 人人登录
 *
 * @author hongwei.peng (pengisgood(at)gmail(dot)com),L.cm
 * @version 1.8.1
 * @since 1.8.1
 */
public class AuthRenrenRequest extends AuthDefaultRequest {

	public AuthRenrenRequest(AuthConfig config) {
		super(config, AuthSource.RENREN);
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		return this.getToken(doPostAuthorizationCode(code).asJsonNode());
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		JsonNode jsonNode = HttpRequest.get(authSource.userInfo())
			.query("access_token", authToken.getAccessToken())
			.query("userId", authToken.getOpenId())
			.execute()
			.asJsonNode()
			.get("response");
		return AuthUser.builder()
			.uuid(jsonNode.get("id").asText())
			.avatar(jsonNode.at("/avatar/0/url").asText())
			.username(jsonNode.at("/name").asText())
			.nickname(jsonNode.at("/name").asText())
			.company(jsonNode.at("/work/0/name").asText())
			.gender(getGender(jsonNode))
			.token(authToken)
			.source(authSource)
			.build();
	}

	@Override
	public AuthResponse refresh(AuthToken authToken) {
		JsonNode jsonNode = HttpRequest.post(authSource.refresh())
			.query("client_id", config.getClientId())
			.query("client_secret", config.getClientSecret())
			.query("refresh_token", authToken.getRefreshToken())
			.query("grant_type", "refresh_token")
			.query("redirect_uri", config.getRedirectUri())
			.execute()
			.asJsonNode();
		return AuthResponse.builder()
			.code(SUCCESS.getCode())
			.data(getToken(jsonNode))
			.build();
	}

	private AuthToken getToken(JsonNode jsonNode) {
		if (jsonNode.hasNonNull("error")) {
			throw new AuthException("Failed to get token from Renren: " + jsonNode);
		}
		return AuthToken.builder()
			.tokenType(jsonNode.get("token_type").asText())
			.expireIn(jsonNode.get("expires_in").asInt())
			.accessToken(jsonNode.get("access_token").asText())
			.refreshToken(jsonNode.get("refresh_token").asText())
			.openId(jsonNode.at("/user/id").asText())
			.build();
	}

	private AuthUserGender getGender(JsonNode jsonNode) {
		JsonNode basicInformation = jsonNode.get("basicInformation");
		if (Objects.isNull(basicInformation)) {
			return AuthUserGender.UNKNOWN;
		}
		return AuthUserGender.getRealGender(basicInformation.get("sex").asText());
	}

}
