package net.dreamlu;

import net.dreamlu.mica.http.HttpRequest;
import net.dreamlu.mica.http.LogLevel;

public class HttpAsVoidTest {

	public static void test1() {
		HttpRequest.get("https://www.dreamlu.net/")
			.useConsoleLog(LogLevel.BODY)
			.useSSL()
			.execute()
			.asVoid();
	}

	public static void test2() {
		HttpRequest.get("https://www.dreamlu.net/")
			.useConsoleLog(LogLevel.BODY)
			.useSSL()
			.executeAsyncAndJoin()
			.asVoid();
	}

	public static void test3() {
		HttpRequest.get("https://www.dreamlu.net/")
			.useConsoleLog(LogLevel.BODY)
			.useSSL()
			.async()
			.asVoid();
	}

	public static void main(String[] args) {
		test1();
		test2();
		test3();
	}

}
