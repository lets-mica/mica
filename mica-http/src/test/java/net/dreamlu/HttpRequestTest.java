/*
 * Copyright (c) 2019-2029, Dreamlu (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dreamlu;

import net.dreamlu.mica.core.ssl.TrustAllHostNames;
import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.LogLevel;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public class HttpRequestTest {

	public static void main(String[] args) {
		String json = HttpRequest.get("https://www.baidu.com/home/xman/data/tipspluslist")
			.useConsoleLog()
			.executeAsync()
			.join()
			.asString();
		System.out.println(json);
	}

	public static void sslTest() throws Exception {
		InputStream isTrustCa = HttpRequestTest.class.getResourceAsStream("/cert/ca.jks");
		InputStream isSelfCert = HttpRequestTest.class.getResourceAsStream("/cert/outgoing.CertwithKey.pkcs12");

		KeyStore selfCert = KeyStore.getInstance("pkcs12");
		selfCert.load(isSelfCert, "password".toCharArray());
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
		kmf.init(selfCert, "password".toCharArray());
		KeyStore caCert = KeyStore.getInstance("jks");
		caCert.load(isTrustCa, "caPassword".toCharArray());
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
		tmf.init(caCert);
		SSLContext sc = SSLContext.getInstance("TLS");

		TrustManager[] trustManagers = tmf.getTrustManagers();
		X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
		sc.init(kmf.getKeyManagers(), trustManagers, (SecureRandom) null);

		// 1. 全局配置证书
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
			.sslSocketFactory(sc.getSocketFactory(), trustManager)
			.hostnameVerifier(TrustAllHostNames.INSTANCE);
		HttpRequest.setHttpClient(builder.build());

		// 2. 单次请求配置证书
		HttpRequest.get("https://123.xxx")
			.useConsoleLog(LogLevel.BODY)
			.sslSocketFactory(sc.getSocketFactory(), trustManager)
			.execute()
			.asString();
	}
}
