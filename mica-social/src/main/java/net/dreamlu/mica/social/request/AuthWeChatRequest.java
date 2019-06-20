package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.http.HttpResponse;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthResponse;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 微信登录
 *
 * @author yangkai.shen (https://xkcoding.com), L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthWeChatRequest extends BaseAuthRequest {
	public AuthWeChatRequest(AuthConfig config) {
		super(config, AuthSource.WECHAT);
	}

	@Override
	public String authorize(String state) {
		return UriComponentsBuilder.fromUriString(authSource.authorize())
			.queryParam("response_type", "code")
			.queryParam("appid", config.getClientId())
			.queryParam("redirect_uri", config.getRedirectUri())
			.queryParam("scope", "snsapi_login")
			.queryParam("state", state.concat("#wechat_redirect"))
			.build()
			.toUriString();
	}

	/**
	 * 微信的特殊性，此时返回的信息同时包含 openid 和 access_token
	 *
	 * @param code 授权码
	 * @return 所有信息
	 */
	@Override
	protected AuthToken getAccessToken(String code) {
		JsonNode object = doGetAuthorizationCode(code).asJsonNode();
		return this.getToken(object);
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		String openId = authToken.getOpenId();
		JsonNode object = HttpRequest.get(authSource.userInfo())
			.query("access_token", accessToken)
			.query("openid", openId)
			.query("lang", "zh_CN")
			.execute()
			.asJsonNode();
		this.checkResponse(object);
		return AuthUser.builder()
			.username(object.get("nickname").asText())
			.nickname(object.get("nickname").asText())
			.avatar(object.get("headimgurl").asText())
			.location(object.get("country").asText() + "-" + object.get("province").asText() + "-" + object.get("city").asText())
			.uuid(openId)
			.gender(AuthUserGender.getRealGender(object.get("sex").asText()))
			.token(authToken)
			.source(authSource)
			.build();
	}

	@Override
	public AuthResponse refresh(AuthToken oldToken) {
		JsonNode jsonNode = HttpRequest.get(authSource.refresh())
			.query("appid", config.getClientId())
			.query("grant_type", "refresh_token")
			.query("refresh_token", oldToken.getRefreshToken())
			.execute()
			.asJsonNode();
		return AuthResponse.builder()
			.code(ResponseStatus.SUCCESS.getCode())
			.data(this.getToken(jsonNode))
			.build();
	}

	/**
	 * 检查响应内容是否正确
	 *
	 * @param object 请求响应内容
	 */
	private void checkResponse(JsonNode object) {
		if (object.has("errcode")) {
			throw new AuthException(object.get("errcode").asInt(), object.get("errmsg").asText());
		}
	}

	/**
	 * 获取token，适用于获取access_token和刷新token
	 *
	 * @param object JsonNode
	 * @return token对象
	 */
	private AuthToken getToken(JsonNode object) {
		this.checkResponse(object);
		return AuthToken.builder()
			.accessToken(object.get("access_token").asText())
			.refreshToken(object.get("refresh_token").asText())
			.expireIn(object.get("expires_in").asInt())
			.openId(object.get("openid").asText())
			.build();
	}

	/**
	 * 微信使用的 appid 和 secret
	 *
	 * @param code code码
	 * @return HttpResponse
	 */
	@Override
	protected HttpResponse doGetAuthorizationCode(String code) {
		return HttpRequest.get(authSource.accessToken())
			.log()
			.query("code", code)
			.query("appid", config.getClientId())
			.query("secret", config.getClientSecret())
			.query("grant_type", "authorization_code")
			.query("redirect_uri", config.getRedirectUri())
			.execute();
	}
}
