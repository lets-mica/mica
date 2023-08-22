package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.StringUtil;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class StringTest {

	public static void main(String[] args) {
		long startNs1 = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			System.out.println(StringUtil.getUUID());
		}
		long tookMs1 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs1);
		long startNs2 = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			System.out.println(UUID.randomUUID());
		}
		long tookMs2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs2);
		long startNs3 = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			System.out.println(StringUtil.getUUID());
		}
		long tookMs3 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs3);
		long startNs4 = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			System.out.println(StringUtil.getNanoId());
		}
		long tookMs4 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs4);
		long startNs5 = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			System.out.println(StringUtil.getSafeUUID());
		}
		long tookMs5 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs5);
		long startNs6 = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			System.out.println(StringUtil.getSafeNanoId());
		}
		long tookMs6 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs6);
		long startNs7 = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			System.out.println(UUID.randomUUID());
		}
		long tookMs7 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs7);
		System.out.println("time1:" + tookMs1);
		System.out.println("time2:" + tookMs2);
		System.out.println("time3:" + tookMs3);
		System.out.println("time4:" + tookMs4);
		System.out.println("time5:" + tookMs5);
		System.out.println("time6:" + tookMs6);
		System.out.println("time7:" + tookMs7);
	}
}
