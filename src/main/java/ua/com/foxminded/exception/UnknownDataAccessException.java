package ua.com.foxminded.exception;

public class UnknownDataAccessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownDataAccessException(Exception exception) {
		super(exception);
	}

	public UnknownDataAccessException(String exception) {
		super(exception);
	}
}
