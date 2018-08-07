package com.walmart.ticketservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SeatHoldIdNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7035562680091194595L;

	public SeatHoldIdNotFoundException(int id) {
		super("Seat Hold Id is Not Valid or Expired or already reserved  =>" + id);
	}
}
