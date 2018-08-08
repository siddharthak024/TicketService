package com.walmart.ticketservice.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.walmart.ticketservice.rest.request.ReserveAndCommitRequest;
import com.walmart.ticketservice.rest.request.SeatHoldRequest;
import com.walmart.ticketservice.rest.response.SeatHoldResponse;
import com.walmart.ticketservice.service.api.TicketService;

@SuppressWarnings("deprecation")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class TicketServiceControllerTest {

	@InjectMocks
	private TicketServiceController ticketServiceController;

	@Mock
	private TicketService ticketService;

	@Test
	public void getTotalAvailableSeatsTest() {
		when(ticketService.numSeatsAvailable()).thenReturn(10);
		assertEquals(10, ticketServiceController.getTotalAvailableSeats());
	}

	@Test
	public void findAndHoldTest() {
		SeatHoldResponse seatHoldResponse = new SeatHoldResponse();
		when(ticketService.findAndHoldSeats(10, "sid@k.com")).thenReturn(seatHoldResponse);

		ResponseEntity<SeatHoldResponse> expected = new ResponseEntity<SeatHoldResponse>(seatHoldResponse, HttpStatus.CREATED);
		SeatHoldRequest holdRequest = new SeatHoldRequest();
		holdRequest.setCustEmail("sid@k.com");
		holdRequest.setNumofSeatsHold(10);
		assertEquals(expected, ticketServiceController.findAndHold(holdRequest));
	}

	@Test
	public void reserveTest() {
		when(ticketService.reserveSeats(1, "sid@k.com")).thenReturn("123-456-abc");
		ResponseEntity<String> expected = new ResponseEntity<String>("123-456-abc", HttpStatus.CREATED);
		ReserveAndCommitRequest reserveAndCommitRequest = new ReserveAndCommitRequest();
		reserveAndCommitRequest.setCustomerEmail("sid@k.com");
		reserveAndCommitRequest.setSeatHoldId(1);
		assertEquals(expected, ticketServiceController.reserve(reserveAndCommitRequest));

	}

}
