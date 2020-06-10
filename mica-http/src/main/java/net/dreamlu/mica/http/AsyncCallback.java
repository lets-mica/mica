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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

/**
 * 异步处理
 *
 * @author L.cm
 */
@ParametersAreNonnullByDefault
public class AsyncCallback implements Callback {
	private final AsyncExchange exchange;

	AsyncCallback(AsyncExchange exchange) {
		this.exchange = exchange;
	}

	@Override
	public void onFailure(Call call, IOException e) {
		exchange.onFailure(call.request(), e);
	}

	@Override
	public void onResponse(Call call, Response response) throws IOException {
		try (HttpResponse httpResponse = new HttpResponse(response)) {
			exchange.onResponse(httpResponse);
			if (response.isSuccessful()) {
				exchange.onSuccessful(httpResponse);
			} else {
				exchange.onFailure(call.request(), new IOException(httpResponse.message()));
			}
		}
	}

}
