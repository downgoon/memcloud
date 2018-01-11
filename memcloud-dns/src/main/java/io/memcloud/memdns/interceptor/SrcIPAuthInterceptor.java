package io.memcloud.memdns.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

import com.github.downgoon.jresty.commons.utils.DynamicProperties;
import com.github.downgoon.jresty.rest.model.UnifiedResponse;
import com.github.downgoon.jresty.rest.util.IPUtil;

/** 源IP限制授权 */
public class SrcIPAuthInterceptor extends BaseInterceptor implements API4InternalAccess {

	private static final long serialVersionUID = 4192250347238509538L;

	/** 允许的源IP：  先简单实现，以后修改成 includes/excludes 组合方式 */
	private String srciplist;
	
	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest)ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		String srcip = IPUtil.getSrcIP(httpRequest);
		if(authPassed(srcip)) {
			return invocation.invoke();
		} else {
			UnifiedResponse up = new UnifiedResponse();
			up.setStatus(RC_DENY_SRCIP);
			up.setMessage("源IP受限");
			up.setDebug(getRefNum4Log(), "源IP ["+srcip+"] 不在允许范围内");
			log.info("srcip: "+srcip+" forbidden, refnum:"+getRefNum4Log()+", scope:"+DynamicProperties.parseRefProperty(srciplist));
			return REST("DENY", up);
		}
	}

	/** 源IP是否认证通过*/
	protected boolean authPassed(String srcip) {
		if(srcip==null) {
			return false;
		}
		String scopelist = srciplist;
		if("*".equals(scopelist)) {//只支持一个统配
			return true;
		}
		scopelist = DynamicProperties.parseRefProperty(srciplist);
		if(StringUtils.isEmpty(scopelist)) {//表示不允许任何源IP访问
			return false;
		}
		if("*".equals(scopelist)) {//只支持一个统配
			return true;
		}
		scopelist = (","+scopelist+",");
		if(scopelist.indexOf(srcip+",") != -1) {//表示当前源IP在允许的范围内
			return true;
		}
		return false;
	}

	public String getSrciplist() {
		return srciplist;
	}

	public void setSrciplist(final String srciplist) {
		this.srciplist = srciplist;
	}
	
}
