package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Cooding登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 */
public class AuthCodingRequest extends BaseAuthRequest {

	public AuthCodingRequest(AuthConfig config) {
		super(config, AuthSource.CODING);
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
		JsonNode jsonNode = doGetAuthorizationCode(code).asJsonNode();
		if (jsonNode.get("code").asInt() != 0) {
			throw new AuthException("Unable to get token from coding using code [" + code + "]");
		}
		return AuthToken.builder()
			.accessToken(jsonNode.get("access_token").asText())
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessToken = authToken.getAccessToken();
		JsonNode jsonNode = HttpRequest.get(authSource.userInfo())
			.queryEncoded("access_token", accessToken)
			.execute()
			.asJsonNode();
		if (jsonNode.get("code").asInt() != 0) {
			throw new AuthException(jsonNode.get("msg").asText());
		}
		JsonNode data = jsonNode.get("data");
		return AuthUser.builder()
			.uuid(data.get("id").asText())
			.username(data.get("name").asText())
			.avatar("https://coding.net/" + data.at("/avatar").asText())
			.blog("https://coding.net/" + data.at("/path").asText())
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
