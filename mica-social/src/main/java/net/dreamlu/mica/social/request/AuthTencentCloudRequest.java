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
 * 腾讯云登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 */
public class AuthTencentCloudRequest extends AuthDefaultRequest {

	public AuthTencentCloudRequest(AuthConfig config) {
		super(config, AuthSource.TENCENT_CLOUD);
	}

	@Override
	public String authorize(String state) {
		return UriComponentsBuilder.fromUriString(authSource.authorize())
			.queryParam("response_type", "code")
			.queryParam("client_id", config.getClientId())
			.queryParam("redirect_uri", config.getRedirectUri())
			.queryParam("state", state)
			.queryParam("scope", "user")
			.build()
			.toUriString();
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		JsonNode object = doGetAuthorizationCode(code).asJsonNode();
		if (!object.hasNonNull("access_token")) {
			throw new AuthException("Unable to get token from tencent cloud using code [" + code + "]: " + object.get("msg"));
		}
		return AuthToken.builder()
			.accessToken(object.get("access_token").asText())
			.refreshToken(object.get("refresh_token").asText())
			.expireIn(object.get("expires_in").asInt())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		JsonNode object = HttpRequest.get(authSource.userInfo())
			.queryEncoded("access_token", accessToken)
			.execute()
			.asJsonNode();
		if (object.get("code").asInt() != 0) {
			throw new AuthException(object.get("msg").asText());
		}
		JsonNode data = object.get("data");
		return AuthUser.builder()
			.uuid(data.get("id").asText())
			.username(data.get("name").asText())
			.avatar("https://dev.tencent.com/" + data.at("/avatar").asText())
			.blog("https://dev.tencent.com/" + data.at("/path").asText())
			.nickname(data.get("name").asText())
			.company(data.at("/company").asText())
			.location(data.at("/location").asText())
			.gender(AuthUserGender.getRealGender(data.at("/sex").asText()))
			.email(data.at("/email").asText())
			.remark(data.at("/slogan").asText())
			.token(authToken)
			.source(authSource)
			.build();
	}
}
