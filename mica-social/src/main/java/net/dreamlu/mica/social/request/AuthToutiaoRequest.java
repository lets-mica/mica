package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthToutiaoErrorCode;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 今日头条登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 */
public class AuthToutiaoRequest extends BaseAuthRequest {

	public AuthToutiaoRequest(AuthConfig config) {
		super(config, AuthSource.TOUTIAO);
	}

	@Override
	public String authorize(String state) {
		return UriComponentsBuilder.fromUriString(authSource.authorize())
			.queryParam("response_type", "code")
			.queryParam("client_key", config.getClientId())
			.queryParam("redirect_uri", config.getRedirectUri())
			.queryParam("state", state)
			.queryParam("auth_only", "1")
			.queryParam("display", "0")
			.build()
			.toUriString();
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		JsonNode object = HttpRequest.get(authSource.accessToken())
			.query("code", code)
			.query("client_key", config.getClientId())
			.query("client_secret", config.getClientSecret())
			.query("grant_type", "authorization_code")
			.query("redirect_uri", config.getRedirectUri())
			.execute()
			.asJsonNode();
		if (object.has("error_code")) {
			throw new AuthException(AuthToutiaoErrorCode.getErrorCode(object.get("error_code").asInt()).getDesc());
		}
		return AuthToken.builder()
			.accessToken(object.get("access_token").asText())
			.expireIn(object.get("expires_in").asInt())
			.openId(object.get("open_id").asText())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		JsonNode userProfile = HttpRequest.get(authSource.userInfo())
			.query("client_key", config.getClientId())
			.query("access_token", authToken.getAccessToken())
			.execute()
			.asJsonNode();
		if (userProfile.has("error_code")) {
			throw new AuthException(AuthToutiaoErrorCode.getErrorCode(userProfile.get("error_code").asInt()).getDesc());
		}
		JsonNode user = userProfile.get("data");
		boolean isAnonymousUser = user.get("uid_type").asInt() == 14;
		String anonymousUserName = "匿名用户";
		return AuthUser.builder()
			.uuid(user.get("uid").asText())
			.username(isAnonymousUser ? anonymousUserName : user.get("screen_name").asText())
			.nickname(isAnonymousUser ? anonymousUserName : user.get("screen_name").asText())
			.avatar(user.get("avatar_url").asText())
			.remark(user.get("description").asText())
			.gender(AuthUserGender.getRealGender(user.get("gender").asText()))
			.token(authToken)
			.source(authSource)
			.build();
	}
}
