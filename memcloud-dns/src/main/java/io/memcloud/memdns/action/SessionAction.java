package io.memcloud.memdns.action;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import io.memcloud.memdns.dao.IUserDao;
import io.memcloud.memdns.dao.entry.User;
import io.memcloud.cas.IAccount;
import io.memcloud.cas.IHttpTracker;
import io.memcloud.cas.IllegalSessionException;
import io.memcloud.cas.SessionAccount;
import io.memcloud.cas.session.PasswordCodec;

public class SessionAction extends BaseAction {

	private static final long serialVersionUID = 1138674623448257319L;

	@Resource(name = "userDao")
	private IUserDao userDao;
	
	@Resource(name = "httpTracker")
	private IHttpTracker httpTracker;
	
	
	/**
	 * @param	u	账号名
	 * @param	p	密码
	 * @return	登录凭证COOKIE
	 * */
	@Override
	public String create() throws Exception {
		String u = getParam("u");
		String p = getParam("p");
		if (StringUtils.isEmpty(u) || StringUtils.isEmpty(p)) {
			responseModel.setStatus(RC_ERR_ARG);
			responseModel.setMessage("u(user) and p(password) required");
			return REST("session-index");
		}
		u = StringUtils.deleteWhitespace(u);
		User user = userDao.getByName(u);
		if (user == null) {
			responseModel.setStatus(401);
			responseModel.setMessage("user name not found");
			return REST("session-index");
		}
		// final String pMD5 = MD5.MD5Encode(p);
		final String pMD5 = PasswordCodec.encode(p);
		if (! StringUtils.equalsIgnoreCase(pMD5, user.getPwd())) {
			responseModel.setStatus(402);
			responseModel.setMessage("password mismatch");
			return REST("session-index");
		}
		
		SessionAccount acc = new SessionAccount();
		acc.setScreenName(user.getName());
		acc.setUserId(user.getId());
		httpTracker.save(acc,0,getHttpRequest(), getHttpResponse());
		responseModel.setAttachment(acc);
		return REST("session-index");
	}


	@Override
	public String remove() throws Exception {
		httpTracker.remove(getHttpRequest(), getHttpResponse());
		return REST("session-index");
	}

	/** 查看自己有没有登录 */
	@Override
	public String index() {
		IAccount acc = null;
		try {
			acc = httpTracker.fetch(getHttpRequest());
		} catch (IllegalSessionException e) {
			responseModel.setStatus(404);//伪造Session
			responseModel.setMessage("invalid session");
			responseModel.setDebug(e.getDebug());
			return REST("login.jsp");
		}
		if (acc == null) {//尚未登录
			responseModel.setStatus(RC_NOT_LOGIN);
			responseModel.setMessage("unlogined");
			return REST("login.jsp");
		}
		responseModel.setStatus(200);
		responseModel.setMessage("logined");
		responseModel.setAttachment(acc);
		return REST("console.jsp");
	}
	
	
}
