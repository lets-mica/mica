package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Stack Overflow登录
 *
 * @author hongwei.peng (pengisgood(at)gmail(dot)com),L.cm
 * @version 1.9.0
 * @since 1.9.0
 */
public class AuthStackOverflowRequest extends AuthDefaultRequest {

	public AuthStackOverflowRequest(AuthConfig config) {
		super(config, AuthSource.STACK_OVERFLOW);
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		JsonNode jsonNode = HttpRequest.post(authSource.accessToken())
			.formBuilder()
			.add("code", code)
			.add("client_id", config.getClientId())
			.add("client_secret", config.getClientSecret())
			.add("grant_type", "authorization_code")
			.add("redirect_uri", config.getRedirectUri())
			.execute()
			.asJsonNode();

		this.checkResponse(jsonNode);

		return AuthToken.builder()
			.accessToken(jsonNode.get("access_token").asText())
			.expireIn(jsonNode.get("expires").asInt())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		JsonNode jsonNode = HttpRequest.get(authSource.userInfo())
			.query("access_token", authToken.getAccessToken())
			.query("site", "stackoverflow")
			.query("key", this.config.getStackOverflowKey())
			.execute()
			.asJsonNode();
		this.checkResponse(jsonNode);
		JsonNode userObj = jsonNode.at("/items/0");
		return AuthUser.builder()
			.uuid(userObj.get("user_id").asText())
			.avatar(userObj.at("/profile_image").asText())
			.location(userObj.at("/location").asText())
			.nickname(userObj.at("/display_name").asText())
			.blog(userObj.at("/website_url").asText())
			.gender(AuthUserGender.UNKNOWN)
			.token(authToken)
			.source(authSource)
			.build();
	}

	@Override
	public String authorize(String state) {
		return UriComponentsBuilder.fromUriString(authSource.authorize())
			.queryParam("response_type", "code")
			.queryParam("client_id", config.getClientId())
			.queryParam("redirect_uri", config.getRedirectUri())
			.queryParam("scope", "read_inbox")
			.queryParam("state", state.concat("#wechat_redirect"))
			.build()
			.toUriString();
	}

	/**
	 * 检查响应内容是否正确
	 *
	 * @param jsonNode 请求响应内容
	 */
	private void checkResponse(JsonNode jsonNode) {
		if (jsonNode.hasNonNull("error")) {
			throw new AuthException(jsonNode.get("error_description").asText());
		}
	}
}
