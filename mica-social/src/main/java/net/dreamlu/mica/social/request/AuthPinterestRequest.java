package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Pinterest登录
 *
 * @author hongwei.peng (pengisgood(at)gmail(dot)com), L.cm
 * @version 1.9.0
 * @since 1.9.0
 */
public class AuthPinterestRequest extends AuthDefaultRequest {

	private static final String FAILURE = "failure";

	public AuthPinterestRequest(AuthConfig config) {
		super(config, AuthSource.PINTEREST);
	}

	@Override
	public String authorize(String state) {
		return UriComponentsBuilder.fromUriString(authSource.authorize())
			.queryParam("response_type", "code")
			.queryParam("client_id", config.getClientId())
			.queryParam("redirect_uri", config.getRedirectUri())
			.queryParam("state", state)
			.queryParam("scope", "read_public")
			.build()
			.toUriString();
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		JsonNode jsonNode = doPostAuthorizationCode(code).asJsonNode();
		this.checkResponse(jsonNode);
		return AuthToken.builder()
			.accessToken(jsonNode.get("access_token").asText())
			.tokenType(jsonNode.get("token_type").asText())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		JsonNode jsonNode = HttpRequest.get(authSource.userInfo())
			.query("access_token", authToken.getAccessToken())
			.query("fields", "id,username,first_name,last_name,bio,image")
			.execute()
			.asJsonNode();
		this.checkResponse(jsonNode);
		JsonNode dataNode = jsonNode.get("data");
		return AuthUser.builder()
			.uuid(dataNode.get("id").asText())
			.avatar(dataNode.at("/image/60x60/url").asText())
			.username(dataNode.at("/username").asText())
			.nickname(dataNode.at("/first_name").asText() + " " + dataNode.at("/last_name").asText())
			.gender(AuthUserGender.UNKNOWN)
			.remark(dataNode.at("/bio").asText())
			.token(authToken)
			.source(authSource)
			.build();
	}

	/**
	 * 检查响应内容是否正确
	 *
	 * @param object 请求响应内容
	 */
	private void checkResponse(JsonNode object) {
		if (object.hasNonNull("status") && FAILURE.equals(object.get("status").asText())) {
			throw new AuthException(object.get("message").asText());
		}
	}

}
