package app.services.exceptions;

public class AddressAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AddressAlreadyExistsException(String msg) {
		super(msg);
	}

	public AddressAlreadyExistsException() {
		super();
	}
}
