package com.walmart.ticketservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomerEmailDoesNotMatchWithRegesteredException extends RuntimeException {

	private static final long serialVersionUID = -2006105881928507567L;

	public CustomerEmailDoesNotMatchWithRegesteredException() {
		super("Customer Email Does Not Match With Regestered Email Exception");
	}
}
