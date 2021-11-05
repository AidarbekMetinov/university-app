package ua.com.foxminded.exception;

public class NotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFoundException(Exception exception) {
		super(exception);
	}

	public NotFoundException(String exception) {
		super(exception);
	}
}
