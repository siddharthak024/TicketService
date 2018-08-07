package com.walmart.ticketservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AtThisTimeAllTicketsAreBookedOrHoldException extends RuntimeException {

	private static final long serialVersionUID = -5859060035149153609L;

	public AtThisTimeAllTicketsAreBookedOrHoldException() {
		super("At This Time All Tickets Are Booked Or Hold !!! Try again after couple of minutes");
	}
}
