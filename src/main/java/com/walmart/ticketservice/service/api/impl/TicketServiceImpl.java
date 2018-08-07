package com.walmart.ticketservice.service.api.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walmart.ticketservice.db.entity.Customer;
import com.walmart.ticketservice.db.entity.SeatBookingPerTier;
import com.walmart.ticketservice.db.entity.SeatHold;
import com.walmart.ticketservice.db.entity.TicketConformation;
import com.walmart.ticketservice.db.entity.Venue;
import com.walmart.ticketservice.db.repository.HoldCustomerDetailsRepo;
import com.walmart.ticketservice.db.repository.ReserveAndCommitRepo;
import com.walmart.ticketservice.db.repository.SeatFindAndHoldRepo;
import com.walmart.ticketservice.db.repository.VenueBookings;
import com.walmart.ticketservice.exception.AtThisTimeAllTicketsAreBookedOrHoldException;
import com.walmart.ticketservice.exception.CustomerEmailDoesNotMatchWithRegesteredException;
import com.walmart.ticketservice.exception.CustomerEmailNotValidException;
import com.walmart.ticketservice.exception.LessNumberOfSeatsAvailableThanExpectedException;
import com.walmart.ticketservice.exception.MaxTicketsPerTransactionException;
import com.walmart.ticketservice.exception.SeatHoldIdNotFoundException;
import com.walmart.ticketservice.exception.SeatsCannotBeZeroException;
import com.walmart.ticketservice.rest.response.SeatBookingPerTierResponse;
import com.walmart.ticketservice.rest.response.SeatHoldResponse;
import com.walmart.ticketservice.service.api.TicketService;

@Service
public class TicketServiceImpl implements TicketService {

	final int MAX_SEATS = 10;
	final boolean VALID_FLAG = true;
	final boolean IS_EXPIRED = false;
	@Autowired
	private SeatFindAndHoldRepo seatFindAndHoldRepo;

	@Autowired
	private HoldCustomerDetailsRepo holdCustomerDetailsRepo;

	@Autowired
	private VenueBookings venueBookings;

	@Autowired
	private ReserveAndCommitRepo reserveAndCommitRepo;

	@Override
	public int numSeatsAvailable() {
		return venueBookings.getTotalAvailableSeats();
	}

	@Override
	public SeatHoldResponse findAndHoldSeats(int numSeats, String customerEmail) {
		validateSeatHoldRequest(numSeats, customerEmail);
		insertOrLookupCustomer(customerEmail);

		SeatHold seatHold = new SeatHold();
		setCustomerDetails(customerEmail, seatHold);

		int bookingStatus = 0;
		List<SeatBookingPerTier> seatBookingPerTiers = new ArrayList<>();
		for (Venue venue : venueBookings.getVenueTierSeatCount()) {
			if (venue.getSeatsPerTierCount() >= numSeats) {
				bookingStatus = venueBookings.setVenueSeatsByTier(venue.getSeatsPerTierCount() - numSeats,
						venue.getTier());
				SeatBookingPerTier seatBooking = new SeatBookingPerTier();
				seatBooking.setTier(venue.getTier());
				seatBooking.setNumOfSeats(numSeats);
				seatHold.addSeatBookingPerTier(seatBooking);
				break;
			} else {
				SeatBookingPerTier bookingPerTier = new SeatBookingPerTier();
				bookingPerTier.setTier(venue.getTier());
				bookingPerTier.setNumOfSeats(venue.getSeatsPerTierCount());
				seatBookingPerTiers.add(bookingPerTier);
			}
		}

		bookSeatsInMultipleTiers(numSeats, seatHold, bookingStatus, seatBookingPerTiers);

		return setSeatHoldResponse(seatFindAndHoldRepo.save(seatHold));
	}

	private void setCustomerDetails(String customerEmail, SeatHold seatHold) {
		seatHold.setCustomerEmail(customerEmail);
		seatHold.setValidFlag(VALID_FLAG);
		seatHold.setExpired(IS_EXPIRED);
		seatHold.setCreatedTimestamp(new Timestamp(Instant.now().toEpochMilli()));
	}

	private void bookSeatsInMultipleTiers(int numSeats, SeatHold seatHold, int bookingStatus,
			List<SeatBookingPerTier> seatBookingPerTiers) {
		if (bookingStatus == 0) {
			int availableSeats = venueBookings.getTotalAvailableSeats();
			if (availableSeats < numSeats) {
				throw new LessNumberOfSeatsAvailableThanExpectedException(availableSeats);
			}

			for (SeatBookingPerTier bookingPerTier : seatBookingPerTiers) {
				SeatBookingPerTier seatHoldInMultipleTier = new SeatBookingPerTier();
				if (numSeats != 0 && numSeats > 0) {
					if (numSeats > (bookingPerTier.getNumOfSeats())) {
						venueBookings.setVenueSeatsByTier(0, bookingPerTier.getTier());
						numSeats = numSeats - (bookingPerTier.getNumOfSeats());
						seatHoldInMultipleTier.setTier(bookingPerTier.getTier());
						seatHoldInMultipleTier.setNumOfSeats(bookingPerTier.getNumOfSeats());
						seatHold.addSeatBookingPerTier(seatHoldInMultipleTier);
					} else {
						venueBookings.setVenueSeatsByTier(Math.abs(bookingPerTier.getNumOfSeats() - numSeats),
								bookingPerTier.getTier());
						seatHoldInMultipleTier.setTier(bookingPerTier.getTier());
						seatHoldInMultipleTier.setNumOfSeats(numSeats);
						seatHold.addSeatBookingPerTier(seatHoldInMultipleTier);
						numSeats = numSeats - (bookingPerTier.getNumOfSeats());
					}
				}
			}
		}
	}

	private SeatHoldResponse setSeatHoldResponse(SeatHold seatHoldCreated) {
		SeatHoldResponse seatHoldResponse = new SeatHoldResponse();

		seatHoldResponse.setSeatHoldId(seatHoldCreated.getSeatHoldId());
		seatHoldResponse.setCustomerEmail(seatHoldCreated.getCustomerEmail());
		seatHoldResponse.setCreatedTimestamp(seatHoldCreated.getCreatedTimestamp());

		List<SeatBookingPerTierResponse> bookingPerTierResponses = new ArrayList<>();

		for (SeatBookingPerTier bookingPerTier : seatHoldCreated.getSeatBookingPerTier()) {
			SeatBookingPerTierResponse bookingPerTierResponse = new SeatBookingPerTierResponse();
			bookingPerTierResponse.setSeatsHold(bookingPerTier.getNumOfSeats());
			bookingPerTierResponse.setTier(bookingPerTier.getTier());
			bookingPerTierResponses.add(bookingPerTierResponse);
		}
		seatHoldResponse.setSeatBookingPerTierResponse(bookingPerTierResponses);
		return seatHoldResponse;
	}

	private void validateSeatHoldRequest(int numSeats, String customerEmail) {
		if (numSeats == 0) {
			throw new SeatsCannotBeZeroException(numSeats);
		}
		if (numSeats > MAX_SEATS) {
			throw new MaxTicketsPerTransactionException(numSeats);
		}

		if (venueBookings.getTotalAvailableSeats() == 0) {
			throw new AtThisTimeAllTicketsAreBookedOrHoldException();
		}

		if (!validateEmailFormat(customerEmail)) {
			throw new CustomerEmailNotValidException();
		}
	}

	private void insertOrLookupCustomer(String customerEmail) {
		Optional<Customer> customer = holdCustomerDetailsRepo.findById(customerEmail);
		if (!customer.isPresent()) {
			Customer cust = new Customer();
			cust.setCustomerEmail(customerEmail);
			cust.setCreatedTimestamp(new Timestamp(Instant.now().toEpochMilli()));
		}
	}

	public boolean validateEmailFormat(final String customerEmail) {

		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(customerEmail);
		return matcher.matches();

	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {

		validateWithSeatHold(seatHoldId, customerEmail);
		TicketConformation conformation = setTicketConformation(seatHoldId, customerEmail);
		TicketConformation conformedSeatId = reserveAndCommitRepo.save(conformation);

		seatFindAndHoldRepo.updateSeatHoldStatus(seatHoldId);

		return conformedSeatId.getConformationId();
	}

	private TicketConformation setTicketConformation(int seatHoldId, String customerEmail) {
		TicketConformation conformation = new TicketConformation();
		conformation.setCustomerEmail(customerEmail);
		conformation.setSeatHoldId(seatHoldId);
		return conformation;
	}

	private void validateWithSeatHold(int seatHoldId, String customerEmail) {
		Optional<SeatHold> seatHold = seatFindAndHoldRepo.getValidSeatHoldById(seatHoldId);
		if (!seatHold.isPresent()) {
			throw new SeatHoldIdNotFoundException(seatHoldId);
		}

		if (!customerEmail.equalsIgnoreCase(seatHold.get().getCustomerEmail())) {
			throw new CustomerEmailDoesNotMatchWithRegesteredException();
		}
	}

}
