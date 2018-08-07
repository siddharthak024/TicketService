package com.walmart.ticketservice.db.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.ticketservice.db.entity.SeatHold;

public interface SeatFindAndHoldRepo extends JpaRepository<SeatHold, Integer> {

	@Query(value = "select * from seat_hold where seat_hold_id = :seatHoldId and valid_flag = true", nativeQuery = true)
	Optional<SeatHold> getValidSeatHoldById(@Param("seatHoldId") int seatHoldId);

	@Transactional
	@Modifying
	@Query("update SeatHold set validFlag = false where createdTimestamp < :currentTimestampMinusSixtySec and validFlag = true and "
			+ "seatHoldId not in (select seatHoldId from TicketConformation)")
	int deleteAllExperiedSeatHolds(@Param("currentTimestampMinusSixtySec") Timestamp currentTimestampMinusSixtySec);

	@Transactional
	@Modifying
	@Query("update SeatHold set validFlag = false where seatHoldId = :seatHoldId")
	void updateSeatHoldStatus(@Param("seatHoldId") int seatHoldId);

	@Query(value = "select * from seat_hold where valid_flag = false and is_expired = false and seat_hold_id not in (select seat_hold_id from ticket_conf)", nativeQuery = true)
	List<SeatHold> getTemporaryHoldSeats();

	@Transactional
	@Modifying
	@Query("update SeatHold set isExpired = true where validFlag = false")
	int updateExpiredStatus();
}
