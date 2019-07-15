package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;

/**
 * Gitee登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 */
public class AuthGiteeRequest extends BaseAuthRequest {

	public AuthGiteeRequest(AuthConfig config) {
		super(config, AuthSource.GITEE);
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		JsonNode accessTokenObject = doPostAuthorizationCode(code).asJsonNode();
		if (accessTokenObject.has("error")) {
			throw new AuthException("Unable to get token from gitee using code [" + code + "]");
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
		return AuthUser.builder()
			.uuid(object.get("id").asText())
			.username(object.get("login").asText())
			.avatar(object.at("/avatar_url").asText())
			.blog(object.at("/blog").asText())
			.nickname(object.at("/name").asText())
			.company(object.at("/company").asText())
			.location(object.at("/address").asText())
			.email(object.at("/email").asText())
			.remark(object.at("/bio").asText())
			.token(authToken)
			.source(authSource)
			.build();
	}
}
