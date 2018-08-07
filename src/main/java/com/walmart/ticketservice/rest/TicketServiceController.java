package com.walmart.ticketservice.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.ticketservice.rest.request.ReserveAndCommitRequest;
import com.walmart.ticketservice.rest.request.SeatHoldRequest;
import com.walmart.ticketservice.rest.response.SeatHoldResponse;
import com.walmart.ticketservice.service.api.TicketService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/venue")
public class TicketServiceController {

	@Autowired
	private TicketService ticketService;

	@ApiOperation(value = "Get total seats available from DB")
	@ApiResponse(response = Integer.class, code = 200, message = "OK")
	@GetMapping(value = "/totalseatsavailable")
	public int getTotalAvailableSeats() {
		return ticketService.numSeatsAvailable();
	}

	@ApiOperation(value = "POST seat hold request")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "OK"),
			@ApiResponse(code = 400, message = "Seats Cannot Be Zero Exception"),
			@ApiResponse(code = 400, message = "numOfSeats can be purchased per request is 10"),
			@ApiResponse(code = 400, message = "At This Time All Tickets Are Booked Or Hold !!! Try again after couple of minutes"),
			@ApiResponse(code = 400, message = "Customer Email Not Valid Exception") })
	@PostMapping(value = "/findandhold", consumes = "application/json")
	public ResponseEntity<SeatHoldResponse> findAndHold(@RequestBody SeatHoldRequest seatHoldRequest) {
		SeatHoldResponse seatHoldResponse = ticketService.findAndHoldSeats(seatHoldRequest.getNumofSeatsHold(), seatHoldRequest.getCustEmail());
		return new ResponseEntity<SeatHoldResponse>(seatHoldResponse, HttpStatus.CREATED);
	}

	@ApiOperation(value = "POST reserve the seatHoldId Request")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "OK"),
			@ApiResponse(code = 400, message = "Seat Hold Id is Not Valid or Expired or already reserved"),
			@ApiResponse(code = 400, message = "Customer Email Does Not Match With Regestered Email Exception") })
	@PostMapping(value = "/reservetickets", consumes = "application/json")
	public ResponseEntity<String> reserve(@RequestBody ReserveAndCommitRequest reserveAndCommit) {
		return new ResponseEntity<String>(ticketService.reserveSeats(reserveAndCommit.getSeatHoldId(), reserveAndCommit.getCustomerEmail()), HttpStatus.CREATED);
	}
}
