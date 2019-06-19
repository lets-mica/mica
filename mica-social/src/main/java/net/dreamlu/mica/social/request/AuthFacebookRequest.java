package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;
import net.dreamlu.mica.social.utils.UrlBuilder;

/**
 * Facebook登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthFacebookRequest extends BaseAuthRequest {

	public AuthFacebookRequest(AuthConfig config) {
		super(config, AuthSource.FACEBOOK);
	}

	@Override
	public String authorize() {
		return UrlBuilder.getFacebookAuthorizeUrl(config.getClientId(), config.getRedirectUri());
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		String accessTokenUrl = UrlBuilder.getFacebookAccessTokenUrl(config.getClientId(), config.getClientSecret(), code, config.getRedirectUri());

		JsonNode object = HttpRequest.post(accessTokenUrl)
			.execute()
			.asJsonNode();
		if (object.has("error")) {
			throw new AuthException(object.get("error").get("message").asText());
		}

		return AuthToken.builder()
			.accessToken(object.get("access_token").asText())
			.expireIn(object.get("expires_in").asInt())
			.tokenType(object.get("token_type").asText())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		JsonNode object = HttpRequest.get(UrlBuilder.getFacebookUserInfoUrl(accessToken))
			.execute()
			.asJsonNode();
		if (object.has("error")) {
			throw new AuthException(object.get("error").get("message").asText());
		}
		return AuthUser.builder()
			.uuid(object.get("id").asText())
			.username(object.get("name").asText())
			.nickname(object.get("name").asText())
			.avatar(object.get("/picture/data/url").asText())
			.location(object.get("locale").asText())
			.email(object.get("email").asText())
			.gender(AuthUserGender.getRealGender(object.get("gender").asText()))
			.token(authToken)
			.source(AuthSource.FACEBOOK)
			.build();
	}

}
