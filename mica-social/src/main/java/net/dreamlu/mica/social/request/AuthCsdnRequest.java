package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.utils.UrlBuilder;

/**
 * CSDN登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthCsdnRequest extends BaseAuthRequest {

	public AuthCsdnRequest(AuthConfig config) {
		super(config, AuthSource.CSDN);
	}

	@Override
	public String authorize() {
		return UrlBuilder.getCsdnAuthorizeUrl(config.getClientId(), config.getRedirectUri());
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		String accessTokenUrl = UrlBuilder.getCsdnAccessTokenUrl(config.getClientId(), config.getClientSecret(), code, config.getRedirectUri());

		JsonNode accessTokenObject = HttpRequest.post(accessTokenUrl)
			.execute()
			.asJsonNode();
		if (accessTokenObject.has("error_code")) {
			throw new AuthException("Unable to get token from csdn using code [" + code + "]");
		}
		return AuthToken.builder()
			.accessToken(accessTokenObject.get("access_token").asText())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();

		JsonNode object = HttpRequest.get(UrlBuilder.getCsdnUserInfoUrl(accessToken))
			.execute()
			.asJsonNode();
		if (object.has("error_code")) {
			throw new AuthException(object.get("error").asText());
		}
		return AuthUser.builder()
			.uuid(object.get("username").asText())
			.username(object.get("username").asText())
			.remark(object.get("description").asText())
			.blog(object.get("website").asText())
			.token(authToken)
			.source(AuthSource.CSDN)
			.build();
	}
}
