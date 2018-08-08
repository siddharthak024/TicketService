package com.walmart.ticketservice.service.api.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.walmart.ticketservice.db.repository.VenueBookingsRepo;
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

	static final int MAX_SEATS = 10;
	static final boolean VALID_FLAG = true;
	static final boolean IS_EXPIRED = false;
	static final int BOOKING_IN_MULTIPLE_TIERS = 0;
	static final int SETTING_TIER_COUNT_ZERO = 0;

	@Autowired
	private SeatFindAndHoldRepo seatFindAndHoldRepo;

	@Autowired
	private HoldCustomerDetailsRepo holdCustomerDetailsRepo;

	@Autowired
	private VenueBookingsRepo venueBookingsRepo;

	@Autowired
	private ReserveAndCommitRepo reserveAndCommitRepo;

	Logger log = LogManager.getLogger(getClass());

	@Override
	public int numSeatsAvailable() {
		log.info("GET call received for total number of seats available");
		return venueBookingsRepo.getTotalAvailableSeats();
	}

	@Override
	public SeatHoldResponse findAndHoldSeats(int numSeats, String customerEmail) {
		validateSeatHoldRequest(numSeats, customerEmail);
		insertOrLookupCustomer(customerEmail);

		SeatHold seatHold = new SeatHold();
		setCustomerDetails(customerEmail, seatHold);

		int bookingStatus = BOOKING_IN_MULTIPLE_TIERS;
		List<SeatBookingPerTier> seatBookingPerTiers = new ArrayList<>();
		for (Venue venue : venueBookingsRepo.getVenueTierSeatCount()) {
			if (venue.getSeatsPerTierCount() >= numSeats) {
				log.info(
						"Requested number of seats available in a tier, So updating the venue tier based on total seats per tier minus number of seats");
				bookingStatus = venueBookingsRepo.setVenueSeatsByTier(venue.getSeatsPerTierCount() - numSeats,
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
		log.info("Inserting seat hold request to SeatHold and SeatBookingPerTier");
		SeatHold hold = seatFindAndHoldRepo.save(seatHold);
		return setSeatHoldResponse(hold);
	}

	private void setCustomerDetails(String customerEmail, SeatHold seatHold) {
		seatHold.setCustomerEmail(customerEmail);
		seatHold.setValidFlag(VALID_FLAG);
		seatHold.setExpired(IS_EXPIRED);
		seatHold.setCreatedTimestamp(new Timestamp(Instant.now().toEpochMilli()));
	}

	private void bookSeatsInMultipleTiers(int numSeats, SeatHold seatHold, int bookingStatus,
			List<SeatBookingPerTier> seatBookingPerTiers) {
		if (bookingStatus == BOOKING_IN_MULTIPLE_TIERS) {
			int availableSeats = venueBookingsRepo.getTotalAvailableSeats();
			if (availableSeats < numSeats) {
				log.info("Less number of seats in venue than requested, available seats are : " + availableSeats);
				throw new LessNumberOfSeatsAvailableThanExpectedException(availableSeats);
			}

			for (SeatBookingPerTier bookingPerTier : seatBookingPerTiers) {
				SeatBookingPerTier seatHoldInMultipleTier = new SeatBookingPerTier();
				if (numSeats != 0 && numSeats > 0) {
					if (numSeats > (bookingPerTier.getNumOfSeats())) {
						log.info("Setting " + bookingPerTier.getTier() + " tier of venue to zero");
						venueBookingsRepo.setVenueSeatsByTier(SETTING_TIER_COUNT_ZERO, bookingPerTier.getTier());
						numSeats = numSeats - (bookingPerTier.getNumOfSeats());
						seatHoldInMultipleTier.setTier(bookingPerTier.getTier());
						seatHoldInMultipleTier.setNumOfSeats(bookingPerTier.getNumOfSeats());
						seatHold.addSeatBookingPerTier(seatHoldInMultipleTier);
					} else {
						log.info("Setting " + bookingPerTier.getTier()
								+ " tier of venue to (available seats in tier - request seats)");
						venueBookingsRepo.setVenueSeatsByTier((bookingPerTier.getNumOfSeats() - numSeats),
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
		log.info("Setting Seat Hold Response object");
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
			log.error("Seats cannot be zero in the request : " + numSeats);
			throw new SeatsCannotBeZeroException(numSeats);
		}
		if (numSeats > MAX_SEATS) {
			log.error("Number of seats request per transaction is greater than MAX_SEATS 10");
			throw new MaxTicketsPerTransactionException(numSeats);
		}

		if (venueBookingsRepo.getTotalAvailableSeats() == 0) {
			log.error("Currently all tickets are booked or hold!!! please try again in couple of minutes");
			throw new AtThisTimeAllTicketsAreBookedOrHoldException();
		}

		if (!validateEmailFormat(customerEmail)) {
			log.error("Customer Email is not valid format");
			throw new CustomerEmailNotValidException();
		}
	}

	private void insertOrLookupCustomer(String customerEmail) {
		Optional<Customer> customer = holdCustomerDetailsRepo.findById(customerEmail);
		if (!customer.isPresent()) {
			Customer cust = new Customer();
			cust.setCustomerEmail(customerEmail);
			cust.setCreatedTimestamp(new Timestamp(Instant.now().toEpochMilli()));
			holdCustomerDetailsRepo.save(cust);
			log.info("Registering new customer with email : " + customerEmail);
		}
	}

	public boolean validateEmailFormat(final String customerEmail) {
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		return Pattern.compile(EMAIL_PATTERN).matcher(customerEmail).matches();
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		validateWithSeatHold(seatHoldId, customerEmail);

		TicketConformation conformation = setTicketConformation(seatHoldId, customerEmail);
		log.info("Reserving the seat hold request for seatHoldId :" + seatHoldId);
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
			log.error("Request Seat Hold ID not found in SeatHold table");
			throw new SeatHoldIdNotFoundException(seatHoldId);
		}

		if (!customerEmail.equalsIgnoreCase(seatHold.get().getCustomerEmail())) {
			log.error("Request email id doesn't match with the registered email and seatholdId");
			throw new CustomerEmailDoesNotMatchWithRegesteredException();
		}
	}

}
