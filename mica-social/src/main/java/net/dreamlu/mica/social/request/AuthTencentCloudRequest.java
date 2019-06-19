package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;
import net.dreamlu.mica.social.utils.UrlBuilder;

/**
 * 腾讯云登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthTencentCloudRequest extends BaseAuthRequest {

	public AuthTencentCloudRequest(AuthConfig config) {
		super(config, AuthSource.TENCENT_CLOUD);
	}

	@Override
	public String authorize() {
		return UrlBuilder.getTencentCloudAuthorizeUrl(config.getClientId(), config.getRedirectUri());
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		String accessTokenUrl = UrlBuilder.getTencentCloudAccessTokenUrl(config.getClientId(), config.getClientSecret(), code);
		JsonNode object = HttpRequest.get(accessTokenUrl)
			.execute()
			.asJsonNode();
		if (object.get("code").asInt() != 0) {
			throw new AuthException("Unable to get token from tencent cloud using code [" + code + "]: " + object.get("msg"));
		}
		return AuthToken.builder()
			.accessToken(object.get("access_token").asText())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		JsonNode object = HttpRequest.get(UrlBuilder.getTencentCloudUserInfoUrl(accessToken))
			.execute()
			.asJsonNode();
		if (object.get("code").asInt() != 0) {
			throw new AuthException(object.get("msg").asText());
		}
		JsonNode data = object.get("data");
		return AuthUser.builder()
			.uuid(data.get("id").asText())
			.username(data.get("name").asText())
			.avatar("https://dev.tencent.com/" + data.get("avatar").asText())
			.blog("https://dev.tencent.com/" + data.get("path").asText())
			.nickname(data.get("name").asText())
			.company(data.get("company").asText())
			.location(data.get("location").asText())
			.gender(AuthUserGender.getRealGender(data.get("sex").asText()))
			.email(data.get("email").asText())
			.remark(data.get("slogan").asText())
			.token(authToken)
			.source(AuthSource.TENCENT_CLOUD)
			.build();
	}
}
