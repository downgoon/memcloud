package io.memcloud.memdns.interceptor;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

import io.memcloud.cas.IAccount;
import io.memcloud.cas.IHttpTracker;
import io.memcloud.cas.IllegalSessionException;
import com.github.downgoon.jresty.rest.model.UnifiedResponse;

/**
 * 登录认证拦截器
 * */
public class LoginedAuthInterceptor extends BaseInterceptor  {

	private static final long serialVersionUID = 3310120188663527131L;
	
	@Resource(name = "httpTracker")
	protected IHttpTracker httpTracker;
	
	/** 默认情况下，没登陆的时候，拦截；当然也可以设置成登录的时候，拦截*/
	protected boolean interceptOnUnlogined = true;

	@Override
	protected String doIntercept(ActionInvocation invocation)  throws Exception {
		IAccount acc = null;
		try {
			acc = httpTracker.fetch(ServletActionContext.getRequest());
		} catch (IllegalSessionException e) {
			log.warn(e.getDebug(), e);
		}	
		if (acc != null) {
			ActionContext.getContext().put(FLAG_LOGIN, acc);
		}
		
		boolean unlogined = (acc == null);

		UnifiedResponse responseModel = new UnifiedResponse();
		//true, true   拦截
		//true, false  放行
		//false, ture  放行
		//false, false  拦截
		if (interceptOnUnlogined == unlogined) {//同或，则拦截
			if (unlogined) {
				responseModel.setStatus(RC_NOT_LOGIN);
				responseModel.setMessage("unlogined");
			} else {
				responseModel.setStatus(RC_HAS_LOGIN);
				responseModel.setMessage("logined");
				responseModel.setAttachment(acc);
			}
			return REST("nologin", responseModel);
			
		} else {//异或，则放行
			return invocation.invoke();
		}
		
	}
	
	public boolean isInterceptOnUnlogined() {
		return interceptOnUnlogined;
	}

	public void setInterceptOnUnlogined(boolean interceptOnUnlogined) {
		this.interceptOnUnlogined = interceptOnUnlogined;
	}

	public IHttpTracker getHttpTracker() {
		return httpTracker;
	}

	public void setHttpTracker(IHttpTracker httpTracker) {
		this.httpTracker = httpTracker;
	}

}
