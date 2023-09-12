package app.services.exceptions;

public class CountryAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CountryAlreadyExistsException(String msg) {
		super(msg);
	}

	public CountryAlreadyExistsException() {
		super();
	}

}
