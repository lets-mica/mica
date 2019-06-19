package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.utils.GlobalAuthUtil;
import net.dreamlu.mica.social.utils.UrlBuilder;

import java.util.Map;

/**
 * Github登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthGithubRequest extends BaseAuthRequest {

	public AuthGithubRequest(AuthConfig config) {
		super(config, AuthSource.GITHUB);
	}

	@Override
	public String authorize() {
		return UrlBuilder.getGithubAuthorizeUrl(config.getClientId(), config.getRedirectUri());
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		String accessTokenUrl = UrlBuilder.getGithubAccessTokenUrl(config.getClientId(), config.getClientSecret(), code, config.getRedirectUri());
		String result = HttpRequest.post(accessTokenUrl)
			.execute()
			.asString();
		Map<String, String> res = GlobalAuthUtil.parseStringToMap(result);
		if (res.containsKey("error")) {
			throw new AuthException(res.get("error") + ":" + res.get("error_description"));
		}
		return AuthToken.builder()
			.accessToken(res.get("access_token"))
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		JsonNode object = HttpRequest.get(UrlBuilder.getGithubUserInfoUrl(accessToken))
			.execute()
			.asJsonNode();
		return AuthUser.builder()
			.uuid(object.get("id").asText())
			.username(object.get("login").asText())
			.avatar(object.get("avatar_url").asText())
			.blog(object.get("blog").asText())
			.nickname(object.get("name").asText())
			.company(object.get("company").asText())
			.location(object.get("location").asText())
			.email(object.get("email").asText())
			.remark(object.get("bio").asText())
			.token(authToken)
			.source(AuthSource.GITHUB)
			.build();
	}
}
