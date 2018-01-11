package io.memcloud.memdns.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;

import com.github.downgoon.jresty.commons.security.Base64Codec;
import com.github.downgoon.jresty.rest.model.UnifiedResponse;
import com.github.downgoon.jresty.rest.view.DefaultHttpHeaders;

/** HTTP_BASE_AUTH 限制 */
public class BaseAuthInterceptor extends BaseInterceptor implements API4InternalAccess {

	private static final long serialVersionUID = -5385973827472053390L;

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		// String scopelist = DynamicProperties.parseRefProperty(baseauthlist);
		String scopelist = baseauthlist;
		if("*".equals(scopelist)) {//额外增加一个统配
			return invocation.invoke();
		}
		
		UnifiedResponse up = new UnifiedResponse();
		String authHeader = request.getHeader("Authorization");
		if(StringUtils.isEmpty(authHeader)) {//缺少BaseAuth认证信息
			up.setStatus(RC_DENY_BASEAUTH);
			up.setMessage("http basic auth required");
			up.setDebug(getRefNum4Log(), "enter user and pwd for basic auth");
			return REST(new DefaultHttpHeaders("DENY").withStatus(401)
												.addHeader("WWW-Authenticate", "Basic realm=\"Secure Area\""), 
									up);
			
		}
		
		String[] authArray = authHeader.split(" ");
		if(authArray==null || authArray.length !=2) {
			up.setStatus(RC_DENY_BASEAUTH);
			up.setMessage("syntax error for basic auth");
			up.setDebug(getRefNum4Log(), "your input: "+authHeader);
			return REST(new DefaultHttpHeaders("DENY").withStatus(401)
											.addHeader("WWW-Authenticate", "Basic realm=\"Secure Area\""), 
										up);
			
		}
		String authContentBase64 = authArray[1];
		// String authUserAndPwd = new String(Base64.decode(authContentBase64));
		String authUserAndPwd = new String(Base64Codec.decode(authContentBase64));
		
		if(scopelist==null) {
			scopelist = "";
		}
		scopelist = ","+scopelist+",";
		if(scopelist.indexOf(authUserAndPwd+",") != -1) {//表示当前账号密码在允许的范围内
			return invocation.invoke();
		}
		else {
			up.setStatus(RC_DENY_BASEAUTH);
			up.setMessage("http basic auth fail");
			up.setDebug(getRefNum4Log(), "your input: "+authHeader);
			return REST(new DefaultHttpHeaders("DENY").withStatus(401)
											.addHeader("WWW-Authenticate", "Basic realm=\"Secure Area\""), 
										up);
		}
	}
	
	private String baseauthlist;
	
	public String getBaseauthlist() {
		return baseauthlist;
	}

	public void setBaseauthlist(String baseauthlist) {
		this.baseauthlist = baseauthlist;
	}
	
}
