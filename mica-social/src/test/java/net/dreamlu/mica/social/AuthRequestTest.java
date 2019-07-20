package net.dreamlu.mica.social;

import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.LogLevel;
import net.dreamlu.mica.social.config.AuthConfig;
import net.dreamlu.mica.social.model.AuthResponse;
import net.dreamlu.mica.social.request.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 */
@Ignore
public class AuthRequestTest {

	@Before
	public void setUp() {
		HttpRequest.setGlobalLog(LogLevel.BODY);
	}

	@Test
	public void giteeTest() {
		AuthRequest authRequest = new AuthGiteeRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		authRequest.login("code");
	}

	@Test
	public void githubTest() {
		AuthRequest authRequest = new AuthGithubRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		authRequest.login("code");
	}

	@Test
	public void weiboTest() {
		AuthRequest authRequest = new AuthWeiboRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		authRequest.login("code");
	}

	@Test
	public void dingdingTest() {
		AuthRequest authRequest = new AuthDingTalkRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		authRequest.login("code");
	}

	@Test
	public void baiduTest() {
		AuthRequest authRequest = new AuthBaiduRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		authRequest.login("code");
	}

	@Test
	public void codingTest() {
		AuthRequest authRequest = new AuthCodingRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		authRequest.login("code");
	}

	@Test
	public void tencentCloudTest() {
		AuthRequest authRequest = new AuthTencentCloudRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		authRequest.login("code");
	}

	@Test
	public void oschinaTest() {
		AuthRequest authRequest = new AuthOschinaRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		authRequest.login("code");
	}

	@Test
	public void qqTest() {
		AuthRequest authRequest = new AuthQqRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		AuthResponse login = authRequest.login("code");
	}

	@Test
	public void wechatTest() {
		AuthRequest authRequest = new AuthWeChatRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		AuthResponse login = authRequest.login("code");
	}

	@Test
	public void googleTest() {
		AuthRequest authRequest = new AuthGoogleRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		AuthResponse login = authRequest.login("code");
	}

	@Test
	public void facebookTest() {
		AuthRequest authRequest = new AuthFacebookRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		AuthResponse login = authRequest.login("code");
	}

	@Test
	public void microsoftTest() {
		AuthRequest authRequest = new AuthMicrosoftRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		AuthResponse login = authRequest.login("code");
	}

	@Test
	public void linkedinTest() {
		AuthRequest authRequest = new AuthLinkedinRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		AuthResponse login = authRequest.login("code");
	}

	@Test
	public void miTest() {
		AuthRequest authRequest = new AuthMiRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		AuthResponse login = authRequest.login("code");
	}

	@Test
	public void TeambitionTest() {
		AuthRequest authRequest = new AuthTeambitionRequest(AuthConfig.builder()
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("redirectUri")
			.build());
		// 返回授权页面，可自行调整
		String authorize = authRequest.authorize();
		System.out.println(authorize);
		// 授权登录后会返回一个code，用这个code进行登录
		AuthResponse login = authRequest.login("code");
		System.out.println(login);
	}
}
