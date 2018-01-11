package io.memcloud.cas;

import java.util.HashMap;
import java.util.Map;

public class SessionAccount implements IAccount {

	protected Long userId;
	
	protected String screenName;
	
	protected String headFace;
	
	protected Map<String,Object> attri = new HashMap<String, Object>();
	
	@Override
	public void setAttribute(String attribute, Object value) {
		this.attri.put(attribute, value);
	}

	@Override
	public Object getAttribute(String attribute) {
		return this.attri.get(attribute);
	}
	
	@Override
	public Long getUserId() {
		return this.userId;
	}

	@Override
	public String getScreenName() {
		return this.screenName;
	}

	@Override
	public String getHeadFace() {
		return this.headFace;
	}

	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public void setHeadFace(String headFace) {
		this.headFace = headFace;
	}
	
	
}
