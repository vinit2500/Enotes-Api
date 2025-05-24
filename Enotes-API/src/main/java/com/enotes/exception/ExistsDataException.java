package com.enotes.exception;

public class ExistsDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ExistsDataException(String message) {
		super(message);
	}

}
