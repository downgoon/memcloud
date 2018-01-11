package  io.memcloud.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {

	private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
	
	private HttpClient httpClient;
	
	private void init() {
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		httpClient.getParams().setVersion(HttpVersion.HTTP_1_1);
	}
	
	public HttpResult post(String url, String charset, NameValuePair[] data) throws Exception {
		return post(url, charset, data, null);
	}

	public HttpResult post(String url, String charset, NameValuePair[] data, Map<String, String> headerMap) throws Exception {
		PostMethod post = new PostMethod(url);
		HttpMethodParams hmp = new HttpMethodParams();
		hmp.setContentCharset(charset);
		post.setParams(hmp);
		setRequestHeader(post, headerMap);
		if (data != null) {
			post.setRequestBody(data);
		}
		return getResponseBodyAsStream(post, charset);
	}

	public HttpResult postXml(String url, String charset, String xml) throws Exception {
		return postXml(url, charset, xml, null);
	}

	public HttpResult postXml(String url, String charset, String xml, Map<String, String> headerMap) throws Exception {
		PostMethod post = new PostMethod(url);
		HttpMethodParams hmp = new HttpMethodParams();
		hmp.setContentCharset(charset);
		post.setParams(hmp);
		setRequestHeader(post, headerMap);
		post.setRequestEntity(new StringRequestEntity(xml, "text/xml", charset));
		return getResponseBodyAsStream(post, charset);
	}

	public HttpResult postFile(String url, String charset, NameValuePair[] data, Part[] parts) throws Exception {
		return postFile(url, charset, data, parts, null);
	}

	public HttpResult postFile(String url, String charset, NameValuePair[] data, Part[] parts, Map<String, String> headerMap) throws Exception {
		PostMethod post = new PostMethod(url);
		HttpMethodParams hmp = new HttpMethodParams();
		hmp.setContentCharset(charset);
		post.setParams(hmp);
		setRequestHeader(post, headerMap);
		if (data != null) {
			post.setRequestBody(data);
		}
		if (parts != null) {
			post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));
		}
		return getResponseBodyAsStream(post, charset);
	}
	
	public HttpResult postFile(String url, String charset, String filePathName) throws Exception {
		PostMethod post = new PostMethod(url);
		HttpMethodParams hmp = new HttpMethodParams();
		hmp.setContentCharset(charset);
		post.setParams(hmp);
		File file = new File(filePathName);
		post.setRequestEntity(new InputStreamRequestEntity(new FileInputStream(file), file.length(), "application/octet-stream"));
		return getResponseBodyAsStream(post, charset);
	}

	public HttpResult get(String url) throws Exception {
		return get(url, "UTF-8", null);
	}
	
	public HttpResult get(String url, String charset) throws Exception {
		return get(url, charset, null);
	}

	public HttpResult get(String url, String charset, Map<String, String> headerMap) throws Exception {
		GetMethod get = new GetMethod(url);
		HttpMethodParams hmp = new HttpMethodParams();
		hmp.setContentCharset(charset);
		setRequestHeader(get, headerMap);
		get.setParams(hmp);
		return getResponseBodyAsStream(get, charset);
	}

	public int head(String url) throws Exception {
		return head(url, null);
	}

	public int head(String url, Map<String, String> headerMap) throws Exception {
		HeadMethod head = new HeadMethod(url);
		int stateCode = 0;
		try {
			setRequestHeader(head, headerMap);
			stateCode = httpClient.executeMethod(head);
		} finally {
			if (head != null) {
				head.releaseConnection();
			}
		}
		return stateCode;
	}

	private void setRequestHeader(HttpMethod method, Map<String, String> map) {
		// method.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE
		// 6.0; Windows NT 5.1; SV1)");
		if (map != null) {
			for (Map.Entry<String, String> m : map.entrySet()) {
				method.setRequestHeader(m.getKey(), m.getValue());
			}
		}
	}

	/**
	 * 下载文件到指定路径
	 * 
	 * @param url
	 * @param savePathFileName
	 * @throws Exception
	 */
	public void getAndSaveData(String url, String savePathFileName) throws Exception {
		GetMethod httpMethod = new GetMethod(url);
		HttpMethodParams hmp = new HttpMethodParams();
		httpMethod.setParams(hmp);
		BufferedInputStream in = null;
		BufferedOutputStream fos = null;
		File file = null;
		try {
			file = new File(savePathFileName);
			// 建立文件目录结构
			file.getParentFile().mkdirs();
			fos = new BufferedOutputStream(new FileOutputStream(file));
			int result = httpClient.executeMethod(httpMethod);
			if (result == 200 || result == 301 || result == 302) {
				in = new BufferedInputStream(httpMethod.getResponseBodyAsStream());
				byte[] b = new byte[4096];
				int count;
				while ((count = in.read(b)) > 0) {
					fos.write(b, 0, count);
				}
				fos.flush();
			} else {
				throw new Exception("出错，错误http状态代码:" + result + "  url=" + url + "  savePathFileName=" + savePathFileName);
			}
		} finally {
			if (httpMethod != null) {
				httpMethod.releaseConnection();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
				in = null;
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				fos = null;
			}
			if (file != null) {
				file = null;
			}
		}
	}

	private HttpResult getResponseBodyAsStream(HttpMethod httpMethod, String charset) throws Exception {
		StringBuffer sb = new StringBuffer();
		int result;
		BufferedReader in = null;
		try {
			result = httpClient.executeMethod(httpMethod);
			in = new BufferedReader(new InputStreamReader(httpMethod.getResponseBodyAsStream(), charset));
			String line = null;
			while ((line = in.readLine()) != null) {
				sb.append(line).append("\r\n");
			}
		} finally {
			if (httpMethod != null) {
				httpMethod.releaseConnection();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
				in = null;
			}
		}
		HttpResult httpResult = new HttpResult();
		httpResult.setStateCode(result);
		httpResult.setContent(sb.toString());
		return httpResult;
	}


	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	/** 静态默认对象初始化*/
	private static HttpUtil _instance = new HttpUtil();
	static {
		MultiThreadedHttpConnectionManager httpClientManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = httpClientManager.getParams();
        params.setStaleCheckingEnabled(true);
        params.setMaxTotalConnections(200);
        params.setDefaultMaxConnectionsPerHost(80);
        params.setConnectionTimeout(10000);
        params.setSoTimeout(6000);//超时时间6秒
        
		HttpClient client = new HttpClient(httpClientManager);
		
		_instance.setHttpClient(client);
		_instance.init();
	}
	
	public static String doGet(String url) {
		return doGet(url, "UTF-8");
	}
	/** HTTP响应包头和包体的二元组 */
	public static class HttpPair {
		public int statusCode;
		public String body;
		public Cookie[] cookies;
		public Map<String,String> cookiesMap;
	}
	/** REFER: http://www.java-tips.org/other-api-tips/httpclient/how-to-use-http-cookies.html */
	public static HttpPair doGetCookie(String url, int connTimeoutSec) throws Exception {
		HttpPair httpPair = new HttpPair();
		
		// Get initial state object
        HttpState initialState = new HttpState();

        // Get HTTP client instance
        HttpClient httpclient = new HttpClient();
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(connTimeoutSec*1000);
        httpclient.setState(initialState);
        
        // RFC 2101 cookie management spec is used per default
        // to parse, validate, format & match cookies
        httpclient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        
        // A different cookie management spec can be selected
        // when desired
        //httpclient.getParams().setCookiePolicy(CookiePolicy.NETSCAPE);
        // Netscape Cookie Draft spec is provided for completeness
        // You would hardly want to use this spec in real life situations
        // httppclient.getParams().setCookiePolicy(
        //   CookiePolicy.BROWSER_COMPATIBILITY);
        // Compatibility policy is provided in order to mimic cookie
        // management of popular web browsers that is in some areas
        // not 100% standards compliant
        
        // Get HTTP GET method
        GetMethod getMethod = new GetMethod(url);
        
        // Execute HTTP GET
        // 注意服务端的响应是： "Transfer-Encoding: chunked[\r][\n]"
        int statusCode = httpclient.executeMethod(getMethod);
        httpPair.statusCode = statusCode;
        httpPair.body = getMethod.getResponseBodyAsString();
        httpPair.cookies = httpclient.getState().getCookies();//Get all the cookies//cookies[i].toExternalForm()
        //getMethod.getResponseHeader("Set-Cookie");
        httpPair.cookiesMap = new HashMap<String, String>();
        if(httpPair.cookies != null && httpPair.cookies.length > 0) {
            for (int i = 0; i < httpPair.cookies.length; i++) {
            	//httpPair.cookies[i].toExternalForm()
            	httpPair.cookiesMap.put(httpPair.cookies[i].getName(), httpPair.cookies[i].getValue());
            }
        }
        
        httpclient.getState().clearCookies();
        
        // Release current connection to the connection pool 
        // once you are done
        getMethod.releaseConnection();
        
        return httpPair;
	}
	
	
	
	public static String doPost(String url,Map<String,String> map) {
		return doPost(url,"utf-8",map);
	}
	
	public static String doPost(String url, String charset,Map<String,String> map) {
		if(map==null) 
			map = new HashMap<String,String>();
		NameValuePair[] nameValuePair = new NameValuePair[map.size()];
		int i=0;
		for(Map.Entry<String, String> entry:map.entrySet()){
			nameValuePair[i++] = new NameValuePair(entry.getKey(),entry.getValue());
		}
		HttpResult hr = null;
		try {
			hr = _instance.post(url, charset, nameValuePair);
		} catch (Exception e) {
			log.warn("HTTP连接异常，异常信息："+e.getMessage()+"，URL："+url);
			return null;
		}
		if(hr != null) {
			if(hr.getStateCode() == 200) {
				return hr.getContent();
			} else {
				log.warn("HTTP响应异常，返回码："+hr.getStateCode()+",URL："+url+"，结果："
						+(hr.getContent() != null ? hr.getContent().substring(0, Math.min(hr.getContent().length(), 80)):"NULL"));
				return "";
			}
		} else {
			return "";
		}
	}
	
	public static String doGet(String url, String charset) {
		HttpResult hr = null;
		try {
			hr = _instance.get(url, charset);
		} catch (Exception e) {
			log.warn("HTTP连接异常，异常信息："+e.getMessage()+"，URL："+url);
			return null;
		}
		if(hr != null) {
			if(hr.getStateCode() == 200) {
				return hr.getContent();
			} else {
				log.warn("HTTP响应异常，返回码："+hr.getStateCode()+",URL："+url+"，结果："
						+(hr.getContent() != null ? hr.getContent().substring(0, Math.min(hr.getContent().length(), 80)):"NULL"));
				return "";
			}
		} else {
			return "";
		}
	}
	
	public static HttpPair doGetBasicAuth(String url, int connTimeoutSec, String username, String password) 
	throws HttpException, IOException 
	{
		HttpPair httpPair = new HttpPair();
		
		// Get initial state object
        HttpState initialState = new HttpState();
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        initialState.setCredentials(AuthScope.ANY, defaultcreds);

        // Get HTTP client instance
        HttpClient httpclient = new HttpClient();
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(connTimeoutSec*1000);
        httpclient.setState(initialState);
        
        // Get HTTP GET method
        GetMethod getMethod = new GetMethod(url);
        
        // Execute HTTP GET
        // 注意服务端的响应是： "Transfer-Encoding: chunked[\r][\n]"
        int statusCode = httpclient.executeMethod(getMethod);
        httpPair.statusCode = statusCode;
        httpPair.body = getMethod.getResponseBodyAsString();
        
//        httpPair.cookies = httpclient.getState().getCookies();//Get all the cookies//cookies[i].toExternalForm()
//        //getMethod.getResponseHeader("Set-Cookie");
//        httpPair.cookiesMap = new HashMap<String, String>();
//        if(httpPair.cookies != null && httpPair.cookies.length > 0) {
//            for (int i = 0; i < httpPair.cookies.length; i++) {
//            	//httpPair.cookies[i].toExternalForm()
//            	httpPair.cookiesMap.put(httpPair.cookies[i].getName(), httpPair.cookies[i].getValue());
//            }
//        }
//        
//        httpclient.getState().clearCookies();
        
        // Release current connection to the connection pool 
        // once you are done
        getMethod.releaseConnection();
        
        return httpPair;
	}
	
	/** 仅仅返回HTTP响应头的响应码*/
	public static int doGetNotify(String url) {
		HttpResult hr = null;
		try {
			hr = _instance.get(url);
			if(hr != null) {
				return hr.getStateCode(); 
			}
		} catch (Exception e) {
			log.warn("doGetNotify Exception: "+e.getMessage()+", URL:"+url);
		}
		return 500;
	}
}
