package io.memcloud.memdns.dao;

public class ConcurrencyProblemException extends Exception {

	private static final long serialVersionUID = 5622104884512507524L;

	public ConcurrencyProblemException() {
		super();
	}

	public ConcurrencyProblemException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConcurrencyProblemException(String message) {
		super(message);
	}

	public ConcurrencyProblemException(Throwable cause) {
		super(cause);
	}
	
}
