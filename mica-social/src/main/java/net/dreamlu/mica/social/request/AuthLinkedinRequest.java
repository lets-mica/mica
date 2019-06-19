package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthResponse;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.utils.UrlBuilder;


/**
 * 领英登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthLinkedinRequest extends BaseAuthRequest {

	public AuthLinkedinRequest(AuthConfig config) {
		super(config, AuthSource.LINKEDIN);
	}

	@Override
	public String authorize() {
		return UrlBuilder.getLinkedinAuthorizeUrl(config.getClientId(), config.getRedirectUri());
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		String accessTokenUrl = UrlBuilder.getLinkedinAccessTokenUrl(config.getClientId(), config.getClientSecret(), code, config.getRedirectUri());
		return this.getToken(accessTokenUrl);
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		JsonNode userInfoObject = HttpRequest.get(UrlBuilder.getLinkedinUserInfoUrl())
			.addHeader("Host", "api.linkedin.com")
			.addHeader("Connection", "Keep-Alive")
			.addHeader("Authorization", "Bearer " + accessToken)
			.execute()
			.asJsonNode();

		this.checkResponse(userInfoObject);

		// 组装用户名
		String firstName, lastName;
		// 获取firstName
		if (userInfoObject.has("localizedFirstName")) {
			firstName = userInfoObject.get("localizedFirstName").asText();
		} else {
			firstName = getUserName(userInfoObject, "firstName");
		}
		// 获取lastName
		if (userInfoObject.has("localizedLastName")) {
			lastName = userInfoObject.get("localizedLastName").asText();
		} else {
			lastName = getUserName(userInfoObject, "lastName");
		}
		String userName = firstName + " " + lastName;

		// 获取用户头像
		String avatar = userInfoObject.get("/profilePicture/displayImage~/elements/0/identifiers/0/identifier").asText();

		// 获取用户邮箱地址
		String email = this.getUserEmail(accessToken);
		return AuthUser.builder()
			.uuid(userInfoObject.get("id").asText())
			.username(userName)
			.nickname(userName)
			.avatar(avatar)
			.email(email)
			.token(authToken)
			.source(AuthSource.LINKEDIN)
			.build();
	}

	private String getUserEmail(String accessToken) {
		return HttpRequest.get("https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))")
			.addHeader("Host", "api.linkedin.com")
			.addHeader("Connection", "Keep-Alive")
			.addHeader("Authorization", "Bearer " + accessToken)
			.execute()
			.asJsonNode()
			.at("/elements/0/handle~/emailAddress")
			.asText();
	}

	private String getUserName(JsonNode userInfoObject, String nameKey) {
		String firstName;
		JsonNode firstNameObj = userInfoObject.get(nameKey);
		JsonNode localizedObj = firstNameObj.get("localized");
		JsonNode preferredLocaleObj = firstNameObj.get("preferredLocale");
		firstName = localizedObj.get(preferredLocaleObj.get("language").asText() + "_" + preferredLocaleObj.get("country").asText()).asText();
		return firstName;
	}

	@Override
	public AuthResponse refresh(AuthToken oldToken) {
		if (StringUtil.isBlank(oldToken.getRefreshToken())) {
			throw new AuthException(ResponseStatus.UNSUPPORTED);
		}
		String refreshTokenUrl = UrlBuilder.getLinkedinRefreshUrl(config.getClientId(), config.getClientSecret(), oldToken.getRefreshToken());
		return AuthResponse.builder()
			.code(ResponseStatus.SUCCESS.getCode())
			.data(this.getToken(refreshTokenUrl))
			.build();
	}

	private void checkResponse(JsonNode userInfoObject) {
		if (userInfoObject.has("error")) {
			throw new AuthException(userInfoObject.get("error_description").asText());
		}
	}

	/**
	 * 获取token，适用于获取access_token和刷新token
	 *
	 * @param accessTokenUrl 实际请求token的地址
	 * @return token对象
	 */
	private AuthToken getToken(String accessTokenUrl) {
		JsonNode accessTokenObject = HttpRequest.post(accessTokenUrl)
			.addHeader("Host", "www.linkedin.com")
			.addHeader("Content-Type", "application/x-www-form-urlencoded")
			.execute()
			.asJsonNode();

		this.checkResponse(accessTokenObject);

		return AuthToken.builder()
			.accessToken(accessTokenObject.get("access_token").asText())
			.expireIn(accessTokenObject.get("expires_in").asInt())
			.refreshToken(accessTokenObject.get("refresh_token").asText())
			.build();
	}
}
