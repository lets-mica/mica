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

package net.dreamlu.mica.http;

import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * BaseAuth
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class BaseAuthenticator implements Authenticator {
	private final String userName;
	private final String password;

	@Override
	public Request authenticate(@Nullable Route route, Response response) throws IOException {
		String credential = Credentials.basic(userName, password, StandardCharsets.UTF_8);
		return response.request().newBuilder()
			.header("Authorization", credential)
			.build();
	}
}
