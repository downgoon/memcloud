package io.memcloud.client;

import org.apache.commons.httpclient.HttpStatus;

public class HttpResult {
	
	private int stateCode;

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStateCode() {
		return stateCode;
	}

	public boolean getOK() {
		if (this.stateCode == HttpStatus.SC_OK || this.stateCode == HttpStatus.SC_MOVED_PERMANENTLY || this.stateCode == HttpStatus.SC_MOVED_TEMPORARILY) {
			return true;
		} else {
			return false;
		}
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	@Override
	public String toString() {
		return stateCode+":"+content;
	}
	
}
