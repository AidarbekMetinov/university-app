package ua.com.foxminded.exception;

public class ConnectionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConnectionException(Exception exception) {
		super(exception);
	}

	public ConnectionException(String exception) {
		super(exception);
	}
}
