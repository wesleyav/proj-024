package app.services.exceptions;

public class CityAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CityAlreadyExistsException(String msg) {
		super(msg);
	}

	public CityAlreadyExistsException() {
		super();
	}
}
