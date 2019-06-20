package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Google登录
 *
 * @author yangkai.shen (https://xkcoding.com), L.cm
 * @version 1.3
 * @since 1.3
 */
public class AuthGoogleRequest extends BaseAuthRequest {

	public AuthGoogleRequest(AuthConfig config) {
		super(config, AuthSource.GOOGLE);
	}

	@Override
	public String authorize(String state) {
		return UriComponentsBuilder.fromUriString(authSource.authorize())
			.queryParam("response_type", "code")
			.queryParam("client_id", config.getClientId())
			.queryParam("redirect_uri", config.getRedirectUri())
			.queryParam("state", state)
			.queryParam("scope", "openid%20email%20profile")
			.build()
			.toUriString();
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		JsonNode object = doPostAuthorizationCode(code).asJsonNode();
		if (object.has("error") || object.has("error_description")) {
			throw new AuthException("get google access_token has error:[" + object.get("error").asText() + "], error_description:[" + object
				.get("error_description").asText() + "]");
		}

		return AuthToken.builder()
			.accessToken(object.get("access_token").asText())
			.expireIn(object.get("expires_in").asInt())
			.scope(object.get("scope").asText())
			.tokenType(object.get("token_type").asText())
			.idToken(object.get("id_token").asText())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getIdToken();
		JsonNode object = HttpRequest.get(authSource.userInfo())
			.query("id_token", accessToken)
			.execute()
			.asJsonNode();
		return AuthUser.builder()
			.uuid(object.get("sub").asText())
			.username(object.get("name").asText())
			.avatar(object.get("picture").asText())
			.nickname(object.get("name").asText())
			.location(object.get("locale").asText())
			.email(object.get("email").asText())
			.token(authToken)
			.source(authSource)
			.build();
	}
}
