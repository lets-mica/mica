package net.dreamlu;

import net.dreamlu.mica.http.HttpLogger;
import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.LogLevel;

import java.io.InputStream;

/**
 * 文件上传 demo
 *
 * @author L.cm
 */
public class HttpFileUploadDemo {

	public static void main(String[] args) {
		// 设置全局日志级别
		HttpRequest.setGlobalLog(HttpLogger.Console, LogLevel.HEADERS);
		// 1. 下载文件流，注意： mica-http CompletableFuture 异步不会自动关流，其他都会自动关闭
		InputStream inputStream = HttpRequest.get("http://www.baidu.com/img/PCdong_eab05f3d3a8e54ca5a0817f09b39d463.gif")
			.executeAsyncAndJoin()
			.asStream();
		// 2. 上传文件流
		String html = HttpRequest.post("http://1.w2wz.com/upload.php")
			.multipartFormBuilder()
			// 上传流，上传完毕后会自动关闭流
			.add("uploadimg", "test.gif", inputStream)
			.execute()
			.asString();
		System.out.println(html);
	}

}
