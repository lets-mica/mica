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
import okhttp3.Request;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 异步执行器
 *
 * @author L.cm
 */
@ParametersAreNonnullByDefault
public class AsyncExchange {
	private final Call call;
	@Nullable
	private Consumer<ResponseSpec> successConsumer;
	@Nullable
	private Consumer<ResponseSpec> responseConsumer;
	@Nullable
	private BiConsumer<Request, HttpException> failedBiConsumer;

	AsyncExchange(Call call) {
		this.call = call;
		this.successConsumer = null;
		this.responseConsumer = null;
		this.failedBiConsumer = null;
	}

	public void onSuccessful(Consumer<ResponseSpec> consumer) {
		this.successConsumer = consumer;
		this.execute();
	}

	public void onResponse(Consumer<ResponseSpec> consumer) {
		this.responseConsumer = consumer;
		this.execute();
	}

	public AsyncExchange onFailed(BiConsumer<Request, HttpException> biConsumer) {
		this.failedBiConsumer = biConsumer;
		return this;
	}

	private void execute() {
		call.enqueue(new AsyncCallback(this));
	}

	protected void onResponse(HttpResponse response) {
		if (responseConsumer != null) {
			responseConsumer.accept(response);
		}
	}

	protected void onSuccessful(HttpResponse response) {
		if (successConsumer != null) {
			successConsumer.accept(response);
		}
	}

	protected void onFailure(Request request, IOException e) {
		if (failedBiConsumer != null) {
			failedBiConsumer.accept(request, new HttpException(request, e));
		}
	}

	protected void onFailure(HttpResponse response) {
		if (failedBiConsumer != null) {
			failedBiConsumer.accept(response.rawRequest(), new HttpException(response));
		}
	}

}
