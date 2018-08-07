package com.walmart.ticketservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomerEmailNotValidException extends RuntimeException {

	private static final long serialVersionUID = -8204500946996164458L;

	public CustomerEmailNotValidException() {
		super("Customer Email Not Valid Exception");
	}
}
