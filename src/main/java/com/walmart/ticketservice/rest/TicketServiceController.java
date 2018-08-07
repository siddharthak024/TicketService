package com.walmart.ticketservice.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.ticketservice.rest.request.ReserveAndCommitRequest;
import com.walmart.ticketservice.rest.request.SeatHoldRequest;
import com.walmart.ticketservice.rest.response.SeatHoldResponse;
import com.walmart.ticketservice.service.api.TicketService;

@RestController
@RequestMapping(value = "/venue")
public class TicketServiceController {

	@Autowired
	private TicketService ticketService;

	@GetMapping(value = "/totalseatsavailable")
	private int getTotalAvailableSeats() {
		return ticketService.numSeatsAvailable();
	}

	@PostMapping(value = "/findandhold", consumes = "application/json")
	private SeatHoldResponse findAndHold(@RequestBody SeatHoldRequest seatHoldRequest) {
		return ticketService.findAndHoldSeats(seatHoldRequest.getNumofSeatsHold(), seatHoldRequest.getCustEmail());
	}

	@PostMapping(value = "/reservetickets", consumes = "application/json")
	private String reserve(@RequestBody ReserveAndCommitRequest reserveAndCommit) {
		return ticketService.reserveSeats(reserveAndCommit.getSeatHoldId(), reserveAndCommit.getCustomerEmail());
	}
}
