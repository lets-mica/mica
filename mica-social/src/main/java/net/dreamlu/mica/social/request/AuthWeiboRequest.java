package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.core.utils.INetUtil;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;


/**
 * 微博登录
 *
 * @author L.cm
 */
public class AuthWeiboRequest extends AuthDefaultRequest {

	public AuthWeiboRequest(AuthConfig config) {
		super(config, AuthSource.WEIBO);
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		JsonNode accessTokenObject = doPostAuthorizationCode(code).asJsonNode();
		if (accessTokenObject.has("error")) {
			throw new AuthException("Unable to get token from weibo using code [" + code + "]:" + accessTokenObject.get("error_description").asText());
		}
		return AuthToken.builder()
			.accessToken(accessTokenObject.get("access_token").asText())
			.uid(accessTokenObject.get("uid").asText())
			.expireIn(accessTokenObject.get("remind_in").asInt())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		String uid = authToken.getUid();
		String oauthParam = String.format("uid=%s&access_token=%s", uid, accessToken);
		JsonNode jsonNode = HttpRequest.get(authSource.userInfo())
			.queryEncoded("uid", uid)
			.queryEncoded("access_token", accessToken)
			.addHeader("Authorization", "OAuth2 " + oauthParam)
			.addHeader("API-RemoteIP", INetUtil.getHostIp())
			.execute()
			.asJsonNode();
		if (jsonNode.has("error")) {
			throw new AuthException(jsonNode.get("error").asText());
		}
		return AuthUser.builder()
			.uuid(jsonNode.get("id").asText())
			.username(jsonNode.get("name").asText())
			.avatar(jsonNode.at("/profile_image_url").asText())
			.blog(StringUtil.isBlank(jsonNode.at("/url").asText()) ? "https://weibo.com/" + jsonNode.at("/profile_url").asText() : jsonNode.at("/url").asText())
			.nickname(jsonNode.at("/screen_name").asText())
			.location(jsonNode.at("/location").asText())
			.remark(jsonNode.at("/description").asText())
			.gender(AuthUserGender.getRealGender(jsonNode.at("/gender").asText()))
			.token(authToken)
			.source(authSource)
			.build();
	}
}
