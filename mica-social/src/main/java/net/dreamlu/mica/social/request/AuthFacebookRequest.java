package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;

/**
 * Facebook登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 */
public class AuthFacebookRequest extends AuthDefaultRequest {

	public AuthFacebookRequest(AuthConfig config) {
		super(config, AuthSource.FACEBOOK);
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		JsonNode object = doPostAuthorizationCode(code).asJsonNode();
		if (object.has("error")) {
			throw new AuthException(object.at("/error/message").asText());
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
		JsonNode object = HttpRequest.get(authSource.userInfo())
			.queryEncoded("access_token", accessToken)
			.queryEncoded("fields", "id,name,birthday,gender,hometown,email,devices,picture.width(400)")
			.execute()
			.asJsonNode();
		if (object.has("error")) {
			throw new AuthException(object.get("/error/message").asText());
		}
		return AuthUser.builder()
			.uuid(object.get("id").asText())
			.username(object.get("name").asText())
			.nickname(object.get("name").asText())
			.avatar(object.at("/picture/data/url").asText())
			.location(object.at("/locale").asText())
			.email(object.at("/email").asText())
			.gender(AuthUserGender.getRealGender(object.at("/gender").asText()))
			.token(authToken)
			.source(authSource)
			.build();
	}

}
