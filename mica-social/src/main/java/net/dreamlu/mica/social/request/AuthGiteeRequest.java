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
 * Gitee登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthGiteeRequest extends BaseAuthRequest {

	public AuthGiteeRequest(AuthConfig config) {
		super(config, AuthSource.GITEE);
	}

	@Override
	public String authorize() {
		return UrlBuilder.getGiteeAuthorizeUrl(config.getClientId(), config.getRedirectUri());
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		String accessTokenUrl = UrlBuilder.getGiteeAccessTokenUrl(config.getClientId(), config.getClientSecret(), code, config.getRedirectUri());
		JsonNode accessTokenObject = HttpRequest.post(accessTokenUrl)
			.execute()
			.asJsonNode();
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
		JsonNode object = HttpRequest.get(UrlBuilder.getGiteeUserInfoUrl(accessToken))
			.execute()
			.asJsonNode();
		return AuthUser.builder()
			.uuid(object.get("id").asText())
			.username(object.get("login").asText())
			.avatar(object.get("avatar_url").asText())
			.blog(object.get("blog").asText())
			.nickname(object.get("name").asText())
			.company(object.get("company").asText())
			.location(object.get("address").asText())
			.email(object.get("email").asText())
			.remark(object.get("bio").asText())
			.token(authToken)
			.source(AuthSource.GITEE)
			.build();
	}
}
