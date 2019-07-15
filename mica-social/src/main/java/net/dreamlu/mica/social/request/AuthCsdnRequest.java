package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;

/**
 * CSDN登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 */
public class AuthCsdnRequest extends BaseAuthRequest {
	public AuthCsdnRequest(AuthConfig config) {
		super(config, AuthSource.CSDN);
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		JsonNode accessTokenObject = doPostAuthorizationCode(code).asJsonNode();
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
		JsonNode object = HttpRequest.get(authSource.userInfo())
			.queryEncoded("access_token", accessToken)
			.execute()
			.asJsonNode();
		if (object.has("error_code")) {
			throw new AuthException(object.get("error").asText());
		}
		return AuthUser.builder()
			.uuid(object.get("username").asText())
			.username(object.get("username").asText())
			.remark(object.at("/description").asText())
			.blog(object.at("/website").asText())
			.token(authToken)
			.source(authSource)
			.build();
	}
}
