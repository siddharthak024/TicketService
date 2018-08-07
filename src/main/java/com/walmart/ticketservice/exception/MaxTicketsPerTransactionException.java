package com.walmart.ticketservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MaxTicketsPerTransactionException extends RuntimeException {

	private static final long serialVersionUID = 297427151048992110L;

	public MaxTicketsPerTransactionException(int numOfSeats) {
		super("numOfSeats can be purchased per request is 10");
	}
}
