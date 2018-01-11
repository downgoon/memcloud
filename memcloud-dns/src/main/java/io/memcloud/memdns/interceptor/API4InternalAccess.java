package io.memcloud.memdns.interceptor;

/** 只对公司内部子系统开放的API */
public interface API4InternalAccess {

	public static final int RC_DENY_BASEAUTH = 401;
	public static final int RC_DENY_SRCIP = 402;
	public static final int RC_DENY_SIGN = 403;
}
