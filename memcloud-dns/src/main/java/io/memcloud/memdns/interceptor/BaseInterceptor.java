package io.memcloud.memdns.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.memcloud.memdns.model.Constants;

import com.github.downgoon.jresty.rest.struts2.interceptor.RestInterceptorSupport;

public abstract class BaseInterceptor extends RestInterceptorSupport implements Constants {

	
	private static final long serialVersionUID = 2854167710010708057L;
	
	protected final Log log = LogFactory.getLog(this.getClass().getName());
	
	
	
//	/**
//	 * 提取Action描述信息
//	 * */
//	protected final String getActionNameDesciption(ActionInvocation invocation) {
//		return invocation.getProxy().getNamespace()+"/"+invocation.getProxy().getActionName()+"-"+invocation.getProxy().getMethod();
//	}

	

}
