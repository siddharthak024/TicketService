package com.walmart.ticketservice.service.api.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.ticketservice.db.entity.SeatBookingPerTier;
import com.walmart.ticketservice.db.entity.SeatHold;
import com.walmart.ticketservice.db.entity.Venue;
import com.walmart.ticketservice.db.repository.SeatFindAndHoldRepo;
import com.walmart.ticketservice.db.repository.VenueBookingsRepo;

@Service
@Transactional
public class PurgeSeatHold {

	static final int SEAT_HOLD_VALID_SECS = 60;
	@Autowired
	private SeatFindAndHoldRepo seatFindAndHoldRepo;

	@Autowired
	private VenueBookingsRepo venueBookings;

	Logger log = LogManager.getLogger(getClass());

	@Scheduled(cron = "${purge.cron.expression}")
	public void purgeExperied() {
		Date date = Date.from(Instant.now().minusSeconds(SEAT_HOLD_VALID_SECS));
		Timestamp time = new Timestamp(date.getTime());

		if (seatFindAndHoldRepo.deleteAllExperiedSeatHolds(time) >= 1) {
			log.info("Retreiving all temporary hold seat hold's");
			List<SeatHold> tempHoldSeatsUpdate = seatFindAndHoldRepo.getTemporaryHoldSeats();

			log.info("Retreiving all venue tier seats");
			List<Venue> venueTierSeatsCountRefresh = getVenueBookings();

			log.info("Calculating all pull back seats per tier from temp seat holds");

			HashMap<Integer, Integer> sumOfHoldSeatsByTier = new HashMap<>();
			for (SeatHold tempSeats : tempHoldSeatsUpdate) {
				List<SeatBookingPerTier> seatBookingsPerTiers = tempSeats.getSeatBookingPerTier();
				for (SeatBookingPerTier seatBookingPerTier : seatBookingsPerTiers) {
					if (sumOfHoldSeatsByTier.get(seatBookingPerTier.getTier()) != null) {
						int value = sumOfHoldSeatsByTier.get(seatBookingPerTier.getTier())
								+ seatBookingPerTier.getNumOfSeats();
						sumOfHoldSeatsByTier.put(seatBookingPerTier.getTier(), value);
					} else {
						sumOfHoldSeatsByTier.put(seatBookingPerTier.getTier(), seatBookingPerTier.getNumOfSeats());
					}
				}

			}
			sumOfHoldSeatsByTier.forEach((tier, pulledBackSeatsPerTier) -> {
				for (Venue venue : venueTierSeatsCountRefresh) {
					if (venue.getTier() == tier) {
						venueBookings.setVenueSeatsByTier(pulledBackSeatsPerTier + venue.getSeatsPerTierCount(), tier);
					}
				}
			});
			log.info("Updating the expired status to true for not valid records");
			seatFindAndHoldRepo.updateExpiredStatus();
		}
	}

	private List<Venue> getVenueBookings() {
		return venueBookings.getVenueTierSeatCount();
	}
}
