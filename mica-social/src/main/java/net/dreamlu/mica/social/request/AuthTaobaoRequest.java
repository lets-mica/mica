package net.dreamlu.mica.social.request;

import com.fasterxml.jackson.databind.JsonNode;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.model.AuthUserGender;
import net.dreamlu.mica.social.utils.GlobalAuthUtil;
import net.dreamlu.mica.social.utils.UrlBuilder;

/**
 * 淘宝登录
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 * @version 1.0
 * @since 1.8
 */
public class AuthTaobaoRequest extends BaseAuthRequest {

	public AuthTaobaoRequest(AuthConfig config) {
		super(config, AuthSource.TAOBAO);
	}

	@Override
	public String authorize() {
		return UrlBuilder.getTaobaoAuthorizeUrl(config.getClientId(), config.getRedirectUri());
	}

	@Override
	protected AuthToken getAccessToken(String code) {
		return AuthToken.builder()
			.accessCode(code)
			.build();
	}

	@Override
	protected AuthUser getUserInfo(AuthToken authToken) {
		String accessCode = authToken.getAccessCode();
		JsonNode object = HttpRequest.post(UrlBuilder.getTaobaoAccessTokenUrl(this.config.getClientId(), this.config.getClientSecret(), accessCode, this.config.getRedirectUri()))
			.execute()
			.asJsonNode();
		if (object.has("error")) {
			throw new AuthException(ResponseStatus.FAILURE + ":" + object.get("error_description").asText());
		}
		authToken.setAccessToken(object.get("access_token").asText());
		authToken.setRefreshToken(object.get("refresh_token").asText());
		authToken.setExpireIn(object.get("expires_in").asInt());
		authToken.setUid(object.get("taobao_user_id").asText());
		authToken.setOpenId(object.get("taobao_open_uid").asText());

		String nick = GlobalAuthUtil.urlDecode(object.get("taobao_user_nick").asText());
		return AuthUser.builder()
			.uuid(object.get("taobao_user_id").asText())
			.username(nick)
			.nickname(nick)
			.gender(AuthUserGender.UNKNOW)
			.token(authToken)
			.source(AuthSource.TAOBAO)
			.build();
	}
}
