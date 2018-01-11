package io.memcloud.memdns.model;

public interface Constants {

	/** RP:  Result Page */
	public static final String RP_PROMPT = "prompt.jsp";
	public static final String RP_NOHTML = "nohtml.jsp";
	
	public static final int RC_SUCC = 200;
	
	public static final int RC_ERROR = 500;
	
	/** 错误的输入参数 */
	public static final int RC_ERR_ARG = 400;
	
	
	public static final int RC_NOT_LOGIN = 444;
	public static final int RC_HAS_LOGIN = 488;
	
	/** 登陆者标记 */
	public static final String FLAG_LOGIN = "FLAG_LOGIN";
}
