package com.walmart.ticketservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LessNumberOfSeatsAvailableThanExpectedException extends RuntimeException {

	private static final long serialVersionUID = 369849198258682209L;

	public LessNumberOfSeatsAvailableThanExpectedException(int availableSeats) {
		super("total number of seats available are" + availableSeats);
	}
}
