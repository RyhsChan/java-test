package com.java.test.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class Https {

	public static void main(String args[]) throws Exception {

		String url = "https://api.weather.com/v2/turbo/vt1observation?units=m&language=zh-CN&geocode=22.59,113.95&format=json&apiKey=c1ea9f47f6a88b9acb43aba7faf389d4";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
		System.out.println("Response Code : "
				+ response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		System.out.println(result);
	}
}
