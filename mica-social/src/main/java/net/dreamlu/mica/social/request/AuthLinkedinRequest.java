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
import org.springframework.web.util.UriComponentsBuilder;


/**
 * 领英登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 */
public class AuthLinkedinRequest extends BaseAuthRequest {

	public AuthLinkedinRequest(AuthConfig config) {
		super(config, AuthSource.LINKEDIN);
	}

	@Override
	public String authorize(String state) {
		return UriComponentsBuilder.fromUriString(authSource.authorize())
			.queryParam("response_type", "code")
			.queryParam("client_id", config.getClientId())
			.queryParam("redirect_uri", config.getRedirectUri())
			.queryParam("state", state)
			.queryParam("scope", "r_liteprofile%20r_emailaddress%20w_member_social")
			.build()
			.toUriString();
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		JsonNode jsonNode = doPostAuthorizationCode(code).asJsonNode();
		return this.getToken(jsonNode);
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		JsonNode userInfoObject = HttpRequest.get(authSource.userInfo())
			.queryEncoded("projection", "(id,firstName,lastName,profilePicture(displayImage~:playableStreams))")
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
		String avatar = userInfoObject.at("/profilePicture/displayImage~/elements/0/identifiers/0/identifier").asText();

		// 获取用户邮箱地址
		String email = this.getUserEmail(accessToken);
		return AuthUser.builder()
			.uuid(userInfoObject.get("id").asText())
			.username(userName)
			.nickname(userName)
			.avatar(avatar)
			.email(email)
			.token(authToken)
			.source(authSource)
			.build();
	}

	private String getUserEmail(String accessToken) {
		return HttpRequest.get("https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))")
			.addHeader("Host", "api.linkedin.com")
			.addHeader("Connection", "Keep-Alive")
			.addHeader("Authorization", "Bearer " + accessToken)
			.execute()
			.asJsonNode()
			.at("/elements/0/handle~0/emailAddress")
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
		JsonNode jsonNode = HttpRequest.post(authSource.refresh())
			.queryEncoded("client_id", config.getClientId())
			.queryEncoded("client_secret", config.getClientSecret())
			.queryEncoded("refresh_token", oldToken.getRefreshToken())
			.queryEncoded("grant_type", "refresh_token")
			.execute()
			.asJsonNode();
		return AuthResponse.builder()
			.code(ResponseStatus.SUCCESS.getCode())
			.data(this.getToken(jsonNode))
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
	 * @param jsonNode JsonNode
	 * @return token对象
	 */
	private AuthToken getToken(JsonNode jsonNode) {
		this.checkResponse(jsonNode);
		return AuthToken.builder()
			.accessToken(jsonNode.get("access_token").asText())
			.expireIn(jsonNode.get("expires_in").asInt())
			.refreshToken(jsonNode.at("/refresh_token").asText())
			.build();
	}
}
