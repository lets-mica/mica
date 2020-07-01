package net.dreamlu.mica.http;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * 采用 CompletableFuture 简化异步使用
 *
 * @author L.cm
 */
@ParametersAreNonnullByDefault
public class CompletableCallback implements Callback {
	private final CompletableFuture<ResponseSpec> future;

	public CompletableCallback(CompletableFuture<ResponseSpec> future) {
		this.future = future;
	}

	@Override
	public void onFailure(Call call, IOException e) {
		// 此处封装一个 failResponse？是否有必要？好像没有必要
		future.completeExceptionally(e);
	}

	@Override
	public void onResponse(Call call, Response response) throws IOException {
		try (BytesResponse bytesResponse = new BytesResponse(response)) {
			future.complete(bytesResponse);
		}
	}
}
