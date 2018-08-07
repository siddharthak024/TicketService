package com.walmart.ticketservice.service.api.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.ticketservice.db.entity.SeatBookingPerTier;
import com.walmart.ticketservice.db.entity.SeatHold;
import com.walmart.ticketservice.db.entity.Venue;
import com.walmart.ticketservice.db.repository.SeatFindAndHoldRepo;
import com.walmart.ticketservice.db.repository.VenueBookings;

@Service
@Transactional
public class PurgeSeatHold {

	@Autowired
	private SeatFindAndHoldRepo seatFindAndHoldRepo;

	@Autowired
	private VenueBookings venueBookings;

	@Scheduled(cron = "${purge.cron.expression}")
	public void purgeExperied() {
		Instant instant = Instant.now().minusSeconds(60);
		Date date = Date.from(instant);
		Timestamp time = new Timestamp(date.getTime());

		if (seatFindAndHoldRepo.deleteAllExperiedSeatHolds(time) >= 1) {
			List<SeatHold> tempHoldSeatsUpdate = seatFindAndHoldRepo.getTemporaryHoldSeats();
			List<Venue> venueTierSeatsCountRefresh = getVenueBookings();
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
			sumOfHoldSeatsByTier.forEach((k, v) -> {
				for (Venue venue : venueTierSeatsCountRefresh) {
					if (venue.getTier() == k) {
						venueBookings.setVenueSeatsByTier(v + venue.getSeatsPerTierCount(), k);
					}
				}

			});
			seatFindAndHoldRepo.updateExpiredStatus();
		}
	}

	private List<Venue> getVenueBookings() {
		return venueBookings.getVenueTierSeatCount();
	}
}
