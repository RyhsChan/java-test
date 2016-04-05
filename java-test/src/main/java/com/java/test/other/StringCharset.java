package com.java.test.other;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringCharset {

	public static void stringInfo(String string, Charset charset) {
		System.out.println(string + ", charset: " + charset.name());
		System.out.println("length: " + string.length() + ", bytes length: "
				+ string.getBytes(charset).length);
		System.out.print("bytes: ");
		for (byte b : string.getBytes(charset)) {
			System.out.format("%x ", b);
		}
		System.out.println();
		System.out.println();
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String chinese = "你好，世界!";

		stringInfo(chinese, Charset.defaultCharset());
		stringInfo(new String(chinese.getBytes(StandardCharsets.UTF_16),
				StandardCharsets.UTF_16), StandardCharsets.UTF_16);
	}
}
