package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.StringUtil;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class StringTest {

	public static void main(String[] args) {
		long startNs1 = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			System.out.println(StringUtil.getUUID());
		}
		long tookMs1 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs1);
		long startNs2 = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			System.out.println(UUID.randomUUID().toString());
		}
		long tookMs2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs2);
		System.out.println("time1:" + tookMs1);
		System.out.println("time2:" + tookMs2);
	}
}
