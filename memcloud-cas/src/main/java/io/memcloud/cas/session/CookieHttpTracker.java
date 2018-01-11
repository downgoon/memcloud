package io.memcloud.cas.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.downgoon.jresty.commons.security.URLEncodec;
import io.memcloud.cas.IAccount;
import io.memcloud.cas.IHttpTracker;
import io.memcloud.cas.IllegalSessionException;
import io.memcloud.cas.SessionAccount;

public class CookieHttpTracker implements IHttpTracker {

	protected static final Logger log = LoggerFactory.getLogger(CookieHttpTracker.class);

	private static final String domainDefault = null;
	private static final String pathDefault = "/";
	private static final int expireDefault = -1;

	private static final String secureKey = "memdns_cookie_key"; // md5 salt

	static final String PPC_TOKEN = "PPC_TOKEN";
	static final String PPC_UID = "PPC_UID";
	static final String PPC_NAME = "PPC_NAME";

	@Override
	public IAccount fetch(HttpServletRequest httpRequest) throws IllegalSessionException {
		CookieMan cman = new CookieMan(httpRequest);
		String uid = cman.getCookie(PPC_UID);
		String uname = cman.getCookie(PPC_NAME);
		String tokenRemote = cman.getCookie(PPC_TOKEN);

		if (isEmpty(uid) || isEmpty(uname) || isEmpty(tokenRemote)) {// 尚未登录
			return null;
		}

		StringBuffer plain = new StringBuffer();
		plain.append(uid).append("|").append(uname).append("|").append(secureKey).append("|");

		String tokenLocal = CastokenCodec.encode(plain.toString());

		if (!tokenLocal.equalsIgnoreCase(tokenRemote)) { // 表示没有登录
			throw new IllegalSessionException(IllegalSessionException.REASON.ArtificalToken,
					"remote is " + tokenRemote + ",local is " + tokenLocal);
		}

		SessionAccount acc = new SessionAccount();
		acc.setUserId(Long.parseLong(uid));
		acc.setScreenName(uname);
		acc.setHeadFace("");
		return acc;
	}

	private boolean isEmpty(String text) {
		return text == null || text.length() == 0;
	}

	@Override
	public String save(IAccount account, int expireSec, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {

		String uid = account.getUserId() + "";

		String uname = URLEncodec.encodeUTF8(account.getScreenName());

		StringBuffer plain = new StringBuffer();
		plain.append(uid).append("|").append(uname).append("|").append(secureKey).append("|");

		// String token = MD5.MD5Encode(plain.toString()).substring(16, 24);
		String token = CastokenCodec.encode(plain.toString());

		if (expireSec == 0) {
			expireSec = expireDefault;
		}

		CookieMan cman = new CookieMan(httpRequest, httpResponse);
		cman.setCookie(PPC_TOKEN, token, domainDefault, pathDefault, expireSec);
		cman.setCookie(PPC_UID, uid, domainDefault, pathDefault, expireSec);
		cman.setCookie(PPC_NAME, uname, domainDefault, pathDefault, expireSec);

		return token;
	}

	@Override
	public void remove(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

		CookieMan cman = new CookieMan(httpRequest, httpResponse);
		cman.removeCookie(PPC_TOKEN, "", domainDefault, pathDefault);
		cman.removeCookie(PPC_UID, "", domainDefault, pathDefault);
		cman.removeCookie(PPC_NAME, "", domainDefault, pathDefault);

	}

}
