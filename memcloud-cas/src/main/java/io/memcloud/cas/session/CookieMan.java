package io.memcloud.cas.session;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieMan {

	private HttpServletRequest httpRequest;
	
	private HttpServletResponse httpResponse;
	
	public CookieMan(HttpServletRequest request, HttpServletResponse response) {
		super();
		this.httpRequest = request;
		this.httpResponse = response;
	}
	
	public CookieMan(HttpServletRequest request) {
		this(request, null);
	}

	public CookieMan(HttpServletResponse response) { 
		this(null, response);
	}

	/**
	 * 获取用户cookie取值
	 * @return 如果Cookie名不存在，则返回NULL；否则，返回COOKIE值。
	 */
	public String getCookie(String name) {
		Cookie cookie = getCookieObject(name);
		if (cookie == null) {
			return null; // Not Found
		}
		return urlDecode(cookie.getValue());
	}

	
	/**
	 * 获取用户cookie信息
	 */
	public Cookie getCookieObject(String name) {
		Cookie[] cookies = httpRequest.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(name)) {
					return cookies[i];
				}
			}
		}
		return null;
	}

	/**
	 * 设置cookie信息
	 */
	public void setCookie(String name, String value) {
		setCookie(name, value, null, null, 0);
	}
	
	/**
	 * 设置cookie信息
	 */
	public void setCookie(String name, String value, int expire) {
		setCookie(name, value, null, null, expire);
	}
	
	/**
	 * 设置cookie信息
	 */
	public void setCookie(String name, String value, String domain, String path,
			int expire) {
		Cookie cookie = new Cookie(name, value);
		
		if (domain != null && !domain.equals("")) {
			cookie.setDomain(domain);
		}
		
		if (path != null && !path.equals("")) {
			cookie.setPath(path);
		}
		
		if (expire > 0) {
			cookie.setMaxAge(expire);
		}
		httpResponse.addCookie(cookie);
	}

	/**
	 * 删除COOKIE
	 * */
	public void removeCookie(String name, String value) {
		removeCookie(name, value, null, null);
	}
	
	/**
	 * 删除COOKIE
	 * */
	public void removeCookie(String name, String value, String domain,
			String path) {
		
		if (value == null) {
			value = "";
		}
		Cookie cookie = new Cookie(name, value);
		
		if (domain != null && !domain.equals("")) {
			cookie.setDomain(domain);
		}
		if (path != null && !path.equals("")) {
			cookie.setPath(path);
		}
		cookie.setMaxAge(0); // 0表示删除COOKIE
		httpResponse.addCookie(cookie);
	}

	/**
	 * 删除cookie信息
	 */
	public void removeCookie(Cookie cookie, String domain, String path) {
		if (cookie != null) {
			cookie.setMaxAge(0);
			if (domain != null && !domain.equals("")) {
				cookie.setDomain(domain);
			}
			cookie.setDomain(domain);
			path = (path == null ? "/" : path);
			cookie.setPath(path);
			httpResponse.addCookie(cookie);
		}
	}
	
	private static String urlDecode(String value) {
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}

	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	public HttpServletResponse getHttpResponse() {
		return httpResponse;
	}
	
}
