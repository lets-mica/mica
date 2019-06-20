package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.core.utils.JsonUtil;
import net.dreamlu.mica.core.utils.StringPool;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthResponse;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 小米登录
 *
 * @author yangkai.shen (https://xkcoding.com), L.cm
 */
public class AuthMiRequest extends BaseAuthRequest {
	private static final String PREFIX = "&&&START&&&";

	public AuthMiRequest(AuthConfig config) {
		super(config, AuthSource.MI);
	}

	@Override
	public String authorize(String state) {
		return UriComponentsBuilder.fromUriString(authSource.authorize())
			.queryParam("response_type", "code")
			.queryParam("client_id", config.getClientId())
			.queryParam("redirect_uri", config.getRedirectUri())
			.queryParam("state", state)
			.queryParam("scope", "user/profile%20user/openIdV2%20user/phoneAndEmail")
			// 默认值为true，授权有效期内的用户在已登录情况下，不显示授权页面，直接通过。如果需要用户每次手动授权，设置为false
//			.queryParam("skip_confirm", "false")
			.build()
			.toUriString();
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		String result = doGetAuthorizationCode(code).asString();
		return getToken(result);
	}

	private AuthToken getToken(String result) {
		String jsonStr = StringUtil.replace(result, PREFIX, StringPool.EMPTY);
		JsonNode object = JsonUtil.readTree(jsonStr);
		if (object.has("error")) {
			throw new AuthException(object.get("error_description").asText());
		}
		return AuthToken.builder()
			.accessToken(object.get("access_token").asText())
			.expireIn(object.get("expires_in").asInt())
			.scope(object.get("scope").asText())
			.tokenType(object.get("token_type").asText())
			.refreshToken(object.get("refresh_token").asText())
			.openId(object.get("openId").asText())
			.macAlgorithm(object.get("mac_algorithm").asText())
			.macKey(object.get("mac_key").asText())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		// 获取用户信息
		JsonNode userProfile = HttpRequest.get(authSource.userInfo())
			.log()
			.query("clientId", config.getClientId())
			.query("token", authToken.getAccessToken())
			.execute()
			.asJsonNode();

		if ("error".equalsIgnoreCase(userProfile.get("result").asText())) {
			throw new AuthException(userProfile.get("description").asText());
		}

		JsonNode user = userProfile.get("data");
		AuthUser authUser = AuthUser.builder()
			.uuid(authToken.getOpenId())
			.username(user.get("miliaoNick").asText())
			.nickname(user.get("miliaoNick").asText())
			.avatar(user.at("/miliaoIcon").asText())
			.email(user.at("/mail").asText())
			.token(authToken)
			.source(authSource)
			.build();

		// 获取用户邮箱手机号等信息
		// {"result":"error","code":96007,"description":"scope是无效的、未知的，或格式不正确的"}
		JsonNode userEmailPhone = HttpRequest.get("https://open.account.xiaomi.com/user/phoneAndEmail")
			.query("clientId", config.getClientId())
			.query("token", authToken.getAccessToken())
			.execute()
			.asJsonNode();

		if (!"error".equalsIgnoreCase(userEmailPhone.get("result").asText())) {
			authUser.setEmail(userEmailPhone.at("/data/email").asText());
		}
		return authUser;
	}

	/**
	 * 刷新access token （续期）
	 *
	 * @param authToken 登录成功后返回的Token信息
	 * @return AuthResponse
	 */
	@Override
	public AuthResponse refresh(AuthToken authToken) {
		String result = HttpRequest.get(authSource.refresh())
			.query("client_id", config.getClientId())
			.query("client_secret", config.getClientSecret())
			.query("redirect_uri", config.getRedirectUri())
			.query("refresh_token", authToken.getRefreshToken())
			.query("grant_type", "refresh_token")
			.execute()
			.asString();
		return AuthResponse.builder().code(ResponseStatus.SUCCESS.getCode()).data(getToken(result)).build();
	}
}
