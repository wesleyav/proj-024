package app.services.exceptions;

public class InvalidPaginationParametersException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPaginationParametersException(String message) {
		super(message);
	}

	public InvalidPaginationParametersException() {
	}
}
