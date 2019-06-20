package net.dreamlu.mica.social.request;

import lombok.Getter;
import net.dreamlu.http.HttpRequest;
import net.dreamlu.http.HttpResponse;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.config.AuthSource;
import net.dreamlu.mica.social.exception.AuthException;
import net.dreamlu.mica.social.model.AuthResponse;
import net.dreamlu.mica.social.model.AuthToken;
import net.dreamlu.mica.social.model.AuthUser;
import net.dreamlu.mica.social.utils.AuthConfigChecker;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com), L.cm
 * @version 1.0
 * @since 1.8
 */
@Getter
public abstract class BaseAuthRequest implements AuthRequest {
	protected final AuthConfig config;
	protected final AuthSource authSource;

	public BaseAuthRequest(AuthConfig config, AuthSource authSource) {
		this.config = config;
		this.authSource = authSource;
		if (!AuthConfigChecker.isSupportedAuth(config)) {
			throw new AuthException(ResponseStatus.PARAMETER_INCOMPLETE);
		}
	}

	protected abstract AuthToken getAccessToken(String code);

	protected abstract AuthUser getUserInfo(AuthToken authToken);

	@Override
	public AuthResponse login(String code) {
		try {
			AuthToken authToken = this.getAccessToken(code);
			AuthUser user = this.getUserInfo(authToken);
			return AuthResponse.builder().code(ResponseStatus.SUCCESS.getCode()).data(user).build();
		} catch (Exception e) {
			return this.responseError(e);
		}
	}

	private AuthResponse responseError(Exception e) {
		int errorCode = ResponseStatus.FAILURE.getCode();
		if (e instanceof AuthException) {
			errorCode = ((AuthException) e).getErrorCode();
		}
		return AuthResponse.builder().code(errorCode).msg(e.getMessage()).build();
	}

	@Override
	public String authorize(String state) {
		return UriComponentsBuilder.fromUriString(authSource.authorize())
			.queryParam("response_type", "code")
			.queryParam("client_id", config.getClientId())
			.queryParam("redirect_uri", config.getRedirectUri())
			.queryParam("state", state)
			.build()
			.toUriString();
	}

	/**
	 * 通用的 authorizationCode 协议
	 *
	 * @param code code码
	 * @return HttpResponse
	 */
	protected HttpResponse doPostAuthorizationCode(String code) {
		return HttpRequest.post(authSource.accessToken())
			.log()
			.query("code", code)
			.query("client_id", config.getClientId())
			.query("client_secret", config.getClientSecret())
			.query("grant_type", "authorization_code")
			.query("redirect_uri", config.getRedirectUri())
			.execute();
	}

	/**
	 * 通用的 authorizationCode 协议
	 *
	 * @param code code码
	 * @return HttpResponse
	 */
	protected HttpResponse doGetAuthorizationCode(String code) {
		return HttpRequest.get(authSource.accessToken())
			.log()
			.query("code", code)
			.query("client_id", config.getClientId())
			.query("client_secret", config.getClientSecret())
			.query("grant_type", "authorization_code")
			.query("redirect_uri", config.getRedirectUri())
			.execute();
	}

}
