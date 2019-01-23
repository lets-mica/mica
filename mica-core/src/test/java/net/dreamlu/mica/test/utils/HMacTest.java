package net.dreamlu.mica.test.utils;

import net.dreamlu.mica.core.utils.$;

public class HMacTest {

	public static void main(String[] args) {
		System.out.println($.md5Hex("1"));
		System.out.println($.md5Hex("1".getBytes()));
		System.out.println();
		System.out.println($.sha1Hex("1"));
		System.out.println($.sha1Hex("1".getBytes()));
		System.out.println();
		System.out.println($.sha224Hex("1"));
		System.out.println($.sha224Hex("1".getBytes()));
		System.out.println();
		System.out.println($.sha256Hex("1"));
		System.out.println($.sha256Hex("1".getBytes()));
		System.out.println();
		System.out.println($.sha384Hex("1"));
		System.out.println($.sha384Hex("1".getBytes()));
		System.out.println();
		System.out.println($.sha512Hex("1"));
		System.out.println($.sha512Hex("1".getBytes()));
		System.out.println();
		System.out.println();
		System.out.println($.hmacMd5Hex("1".getBytes(), "1"));
		System.out.println($.hmacMd5Hex("1", "1"));
		System.out.println();
		System.out.println($.hmacSha1Hex("1".getBytes(), "1"));
		System.out.println($.hmacSha1Hex("1", "1"));
		System.out.println();
		System.out.println($.hmacSha224Hex("1".getBytes(), "1"));
		System.out.println($.hmacSha224Hex("1", "1"));
		System.out.println();
		System.out.println($.hmacSha256Hex("1".getBytes(), "1"));
		System.out.println($.hmacSha256Hex("1", "1"));
		System.out.println();
		System.out.println($.hmacSha384Hex("1".getBytes(), "1"));
		System.out.println($.hmacSha384Hex("1", "1"));
		System.out.println();
		System.out.println($.hmacSha512Hex("1".getBytes(), "1"));
		System.out.println($.hmacSha512Hex("1", "1"));
	}
}
