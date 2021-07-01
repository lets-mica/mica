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

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 异步执行器
 *
 * @author L.cm
 */
public class AsyncExchange {
	private final static Consumer<ResponseSpec> DEFAULT_CONSUMER = (r) -> {};
	private final static BiConsumer<Request, IOException> DEFAULT_FAIL_CONSUMER = (r, e) -> {};
	private final Call call;
	private Consumer<ResponseSpec> successConsumer;
	private Consumer<ResponseSpec> responseConsumer;
	private BiConsumer<Request, IOException> failedBiConsumer;

	AsyncExchange(Call call) {
		this.call = call;
		this.successConsumer = DEFAULT_CONSUMER;
		this.responseConsumer = DEFAULT_CONSUMER;
		this.failedBiConsumer = DEFAULT_FAIL_CONSUMER;
	}

	public void onSuccessful(Consumer<ResponseSpec> consumer) {
		this.successConsumer = consumer;
		this.execute();
	}

	public void onResponse(Consumer<ResponseSpec> consumer) {
		this.responseConsumer = consumer;
		this.execute();
	}

	public AsyncExchange onFailed(BiConsumer<Request, IOException> biConsumer) {
		this.failedBiConsumer = biConsumer;
		return this;
	}

	private void execute() {
		call.enqueue(new AsyncCallback(this));
	}

	protected void onResponse(HttpResponse httpResponse) {
		responseConsumer.accept(httpResponse);
	}

	protected void onSuccessful(HttpResponse httpResponse) {
		successConsumer.accept(httpResponse);
	}

	protected void onFailure(Request request, IOException e) {
		failedBiConsumer.accept(request, e);
	}

}
