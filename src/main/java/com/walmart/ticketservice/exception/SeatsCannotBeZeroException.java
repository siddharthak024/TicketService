package com.walmart.ticketservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SeatsCannotBeZeroException extends RuntimeException {

	private static final long serialVersionUID = -6807503983831053064L;

	public SeatsCannotBeZeroException(int numOfSeats) {
		super("Seats Cannot Be Zero Exception ==> " + numOfSeats);
	}
}
