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
 * oschina登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthOschinaRequest extends BaseAuthRequest {

	public AuthOschinaRequest(AuthConfig config) {
		super(config, AuthSource.OSCHINA);
	}

	@Override
	public String authorize() {
		return UrlBuilder.getOschinaAuthorizeUrl(config.getClientId(), config.getRedirectUri());
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		String accessTokenUrl = UrlBuilder.getOschinaAccessTokenUrl(config.getClientId(), config.getClientSecret(), code, config.getRedirectUri());
		JsonNode accessTokenObject = HttpRequest.post(accessTokenUrl)
			.execute()
			.asJsonNode();
		if (accessTokenObject.has("error")) {
			throw new AuthException("Unable to get token from oschina using code [" + code + "]");
		}
		return AuthToken.builder()
			.accessToken(accessTokenObject.get("access_token").asText())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		JsonNode object = HttpRequest.get(UrlBuilder.getOschinaUserInfoUrl(accessToken))
			.execute()
			.asJsonNode();
		if (object.has("error")) {
			throw new AuthException(object.get("error_description").asText());
		}
		return AuthUser.builder()
			.uuid(object.get("id").asText())
			.username(object.get("name").asText())
			.nickname(object.get("name").asText())
			.avatar(object.get("avatar").asText())
			.blog(object.get("url").asText())
			.location(object.get("location").asText())
			.gender(AuthUserGender.getRealGender(object.get("gender").asText()))
			.email(object.get("email").asText())
			.token(authToken)
			.source(AuthSource.OSCHINA)
			.build();
	}
}
