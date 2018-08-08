package com.walmart.ticketservice.service.api.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import com.walmart.ticketservice.db.entity.Customer;
import com.walmart.ticketservice.db.entity.SeatBookingPerTier;
import com.walmart.ticketservice.db.entity.SeatHold;
import com.walmart.ticketservice.db.entity.TicketConformation;
import com.walmart.ticketservice.db.entity.Venue;
import com.walmart.ticketservice.db.repository.HoldCustomerDetailsRepo;
import com.walmart.ticketservice.db.repository.ReserveAndCommitRepo;
import com.walmart.ticketservice.db.repository.SeatFindAndHoldRepo;
import com.walmart.ticketservice.db.repository.VenueBookingsRepo;
import com.walmart.ticketservice.rest.response.SeatHoldResponse;
import com.walmart.ticketservice.service.api.TicketService;

@SuppressWarnings("deprecation")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class TicketServiceImplTest {

	@Mock
	private static SeatFindAndHoldRepo seatFindAndHoldRepo;

	@Mock
	private static HoldCustomerDetailsRepo holdCustomerDetailsRepo;

	@Mock
	private VenueBookingsRepo venueBookingsRepo;

	@Mock
	private ReserveAndCommitRepo reserveAndCommitRepo;

	private static TicketService ticketService = new TicketServiceImpl();

	static TicketConformation ticketConf;
	static SeatHold seatHoldCreated;

	static final String custEmail = "sid@k.com";
	static final int numOfSeats = 10;
	static final int seatHoldId = 1;

	@Test
	public void numSeatsAvailableTest() {
		Whitebox.setInternalState(ticketService, venueBookingsRepo);
		when(venueBookingsRepo.getTotalAvailableSeats()).thenReturn(numOfSeats);

		assertEquals(numOfSeats, ticketService.numSeatsAvailable());

	}

	@Test
	public void reserveSeatsTest() {
		SeatHold hold = new SeatHold();
		hold.setSeatHoldId(seatHoldId);
		hold.setCustomerEmail(custEmail);
		Optional<SeatHold> seatHold = Optional.of(hold);

		Whitebox.setInternalState(ticketService, seatFindAndHoldRepo);
		Whitebox.setInternalState(ticketService, reserveAndCommitRepo);

		int seatHoldId = 1;
		when(seatFindAndHoldRepo.getValidSeatHoldById(seatHoldId)).thenReturn(seatHold);

		ticketConf = createTicketConformation();

		when(reserveAndCommitRepo.save(any(TicketConformation.class))).thenReturn(ticketConf);
		assertEquals("123-456-abc", ticketService.reserveSeats(seatHoldId, custEmail));

	}

	@Test
	public void findAndHoldTest() {
		Whitebox.setInternalState(ticketService, venueBookingsRepo);
		when(venueBookingsRepo.getTotalAvailableSeats()).thenReturn(64);

		Whitebox.setInternalState(ticketService, holdCustomerDetailsRepo);
		Whitebox.setInternalState(ticketService, seatFindAndHoldRepo);

		Customer value = new Customer();
		Optional<Customer> customer = Optional.of(value);
		when(holdCustomerDetailsRepo.findById(custEmail)).thenReturn(customer);

		List<Venue> venue = new ArrayList<>();
		Venue v1 = new Venue();
		v1.setTier(1);
		v1.setSeatsPerTierCount(34);

		Venue v2 = new Venue();
		v2.setTier(1);
		v2.setSeatsPerTierCount(34);
		venue.add(v1);
		venue.add(v2);

		when(venueBookingsRepo.getVenueTierSeatCount()).thenReturn(venue);

		when(venueBookingsRepo.setVenueSeatsByTier(24, 1)).thenReturn(1);

		seatHoldCreated = new SeatHold();
		seatHoldCreated.setCustomerEmail(custEmail);
		seatHoldCreated.setSeatHoldId(seatHoldId);
		seatHoldCreated.setCreatedTimestamp(new Timestamp(10));
		List<SeatBookingPerTier> seatBookingPerTier = new ArrayList<>();
		SeatBookingPerTier sb1 = new SeatBookingPerTier();
		sb1.setNumOfSeats(numOfSeats);
		sb1.setTier(1);
		seatBookingPerTier.add(sb1);

		seatHoldCreated.setSeatBookingPerTier(seatBookingPerTier);

		seatHoldCreated.setSeatBookingPerTier(seatBookingPerTier);

		when(seatFindAndHoldRepo.save(any(SeatHold.class))).thenReturn(seatHoldCreated);
		SeatHoldResponse holdResponse = ticketService.findAndHoldSeats(numOfSeats, custEmail);
		assertEquals(custEmail, holdResponse.getCustomerEmail());
	}

	private TicketConformation createTicketConformation() {
		TicketConformation conformation = new TicketConformation();
		conformation.setCustomerEmail(custEmail);
		conformation.setSeatHoldId(seatHoldId);
		conformation.setConformationId("123-456-abc");
		return conformation;
	}
}
