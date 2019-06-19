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
import net.dreamlu.mica.social.utils.UrlBuilder;

import java.text.MessageFormat;

/**
 * 小米登录
 *
 * @author yangkai.shen (https://xkcoding.com), L.cm
 * @version 1.5
 * @since 1.5
 */
public class AuthMiRequest extends BaseAuthRequest {
	private static final String PREFIX = "&&&START&&&";

	public AuthMiRequest(AuthConfig config) {
		super(config, AuthSource.MI);
	}

	@Override
	public String authorize() {
		return UrlBuilder.getMiAuthorizeUrl(config.getClientId(), config.getRedirectUri());
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		String accessTokenUrl = UrlBuilder.getMiAccessTokenUrl(config.getClientId(), config.getClientSecret(), config.getRedirectUri(), code);
		return getToken(accessTokenUrl);
	}

	private AuthToken getToken(String accessTokenUrl) {
		String result = HttpRequest.get(accessTokenUrl)
			.execute()
			.asString();
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
		JsonNode userProfile = HttpRequest.get(UrlBuilder.getMiUserInfoUrl(config.getClientId(), authToken.getAccessToken()))
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
			.avatar(user.get("miliaoIcon").asText())
			.email(user.get("mail").asText())
			.token(authToken)
			.source(AuthSource.MI)
			.build();

		// 获取用户邮箱手机号等信息
		String emailPhoneUrl = MessageFormat.format("{0}?clientId={1}&token={2}", "https://open.account.xiaomi.com/user/phoneAndEmail", config
			.getClientId(), authToken.getAccessToken());

		JsonNode userEmailPhone = HttpRequest.get(emailPhoneUrl)
			.execute()
			.asJsonNode();
		if ("error".equalsIgnoreCase(userEmailPhone.get("result").asText())) {
			JsonNode emailPhone = userEmailPhone.get("data");
			authUser.setEmail(emailPhone.get("email").asText());
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
		String miRefreshUrl = UrlBuilder.getMiRefreshUrl(config.getClientId(), config.getClientSecret(), config.getRedirectUri(), authToken.getRefreshToken());
		return AuthResponse.builder().code(ResponseStatus.SUCCESS.getCode()).data(getToken(miRefreshUrl)).build();
	}
}
