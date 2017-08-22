package com.maoshen.component.http;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class OkHttpUtils {
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	/**
	 * okhttp
	 * @param url
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static String postByJson(String url, String json) throws Exception {
		OkHttpClient client = new OkHttpClient();
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}

}
