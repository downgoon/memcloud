package io.memcloud.memdns.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;

import io.memcloud.cas.IAccount;
import io.memcloud.memdns.model.Constants;
import com.github.downgoon.jresty.rest.struts2.action.UnifiedRestAction;

public class BaseAction extends UnifiedRestAction implements Constants {

	private static final long serialVersionUID = -4173520615457404142L;

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	protected String sessionType = "cookie";
	protected String castoken;

	public String getSessionType() {
		return sessionType;
	}

	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}

	public String getCastoken() {
		return castoken;
	}

	public void setCastoken(String castoken) {
		this.castoken = castoken;
	}
	
	protected IAccount loginedUser() {
		return (IAccount)ActionContext.getContext().get(FLAG_LOGIN);
//		SessionAccount acc = new SessionAccount();
//		acc.setUserId(10001L);
//		acc.setScreenName("李伟");
//		return acc;
	}
	
}
