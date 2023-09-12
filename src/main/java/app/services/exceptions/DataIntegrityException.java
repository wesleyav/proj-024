package app.services.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class DataIntegrityException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataIntegrityException(String string, DataIntegrityViolationException e) {
		super(string, e);
	}

}
