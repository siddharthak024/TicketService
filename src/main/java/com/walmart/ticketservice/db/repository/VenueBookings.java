package com.walmart.ticketservice.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.ticketservice.db.entity.Venue;

public interface VenueBookings extends JpaRepository<Venue, Integer> {

	@Query(value = "select sum(seatsPerTierCount) as seatsPerTierCount from Venue")
	int getTotalAvailableSeats();
	
	@Query(value = "select * from Venue", nativeQuery = true)
	List<Venue> getVenueTierSeatCount();

	@Transactional
	@Modifying
	@Query("update Venue v set v.seatsPerTierCount =:seatsPerTierCount where tier =:tier")
	int setVenueSeatsByTier(@Param("seatsPerTierCount") Integer seatsPerTierCount, @Param("tier") Integer tier);
}
