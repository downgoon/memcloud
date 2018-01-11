/**
 * 
 */
package io.memcloud.stats.notification.impl;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * @author ganghuawang
 * HttpClient模拟Http的GET和POST请求
 */
public class HttpClientHandler {

	public static String notifyGetPage(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String txt = null;
		try {
			if(httpClient == null)
				httpClient = new DefaultHttpClient();
			txt = httpClient.execute(get, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			get.abort();
		}
		return txt;
	}
	
	public static String notifyPostPage(HttpPost post) {
		HttpClient httpClient = new DefaultHttpClient();
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String txt = null;
		try {
			txt = httpClient.execute(post, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.abort();
		}
		return txt;
	}
	
	// 用post方法向服务器请求 并获得响应，因为post方法要封装参数，因此在函数外部封装好传参
	public static HttpResponse postMethod(HttpPost post) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse resp = null;
		try {
			resp = httpClient.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.abort();
		}
		return resp;
	}

	// 用get方法向服务器请求 并获得响应
	public static HttpResponse getMethod(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse resp = null;
		try {
			resp = httpClient.execute(get);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			get.abort();
		}
		return resp;
	}
}
