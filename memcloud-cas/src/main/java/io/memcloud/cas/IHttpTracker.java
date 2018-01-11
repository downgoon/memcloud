package io.memcloud.cas;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IHttpTracker  {

	public IAccount fetch(HttpServletRequest httpRequest)
			throws IllegalSessionException;
	
	public String save(IAccount account,int expireSec,HttpServletRequest httpRequest,HttpServletResponse httpResponse);
	
	public void remove(HttpServletRequest httpRequest,HttpServletResponse httpResponse);
}
