package io.memcloud.cas;

public interface IAccount {

	public Long getUserId();
	
	public String getScreenName();
	
	public String getHeadFace();

	public void setAttribute(String attribute, Object value);
	
	public Object getAttribute(String attribute);
}
