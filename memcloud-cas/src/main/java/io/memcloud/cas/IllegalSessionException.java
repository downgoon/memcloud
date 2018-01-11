package io.memcloud.cas;


/**
 * Session信息不完整，可能是伪造的
 * */
public class IllegalSessionException extends Exception {
	
	private static final long serialVersionUID = -2093361229094007009L;
	
	public static enum REASON {
		ArtificalToken, //SESSIONID或COOKIE是伪造的
		SessionTimeout, //SESSION 超时
		BlackList	//用户黑名单
	}
	
	protected REASON reason;
	
	protected String debug;
	
	protected String message4REASON(REASON reason) {
		switch(reason) {
		case SessionTimeout:
			return "登录已过期，请重新登录";
		case BlackList:
			return "账号被封";
		case ArtificalToken:
		default:
			return "尚未登录";
		}
	}
	
	public IllegalSessionException(REASON reason) {
		this.reason = reason;
		this.message = message4REASON(reason);
	}

	public IllegalSessionException(REASON reason, String debug) {
		this(reason);
		this.debug = debug;
	}

	public IllegalSessionException(REASON reason, String debug, Throwable cause) {
		super(cause);
		this.reason = reason;
		this.message = message4REASON(reason);
		this.debug = debug;
	}

	private String message;
	@Override
	public String getMessage() {
		return this.message;
	}

	public final REASON getReason() {
		return reason;
	}

	public final String getDebug() {
		return debug;
	}
	
}
