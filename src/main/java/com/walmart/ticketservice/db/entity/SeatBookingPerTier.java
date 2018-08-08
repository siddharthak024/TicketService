package com.walmart.ticketservice.db.entity;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "seat_booking_per_tier")
public class SeatBookingPerTier implements Serializable {

	private static final long serialVersionUID = -4658062107718732205L;

	@Id
	@JsonIgnore
	@Column(name = "seat_booking_per_tier_id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seat_booking_per_tier_seq_gen")
	@SequenceGenerator(name = "seat_booking_per_tier_seq_gen", sequenceName = "seat_booking_seq")
	private Integer seatBookingPerTierId;

	@Column(name = "tier")
	private Integer tier;

	@Column(name = "num_of_seats")
	private Integer numOfSeats;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seat_hold_id")
	private SeatHold seat_hold;

	public Integer getSeatBookingPerTierId() {
		return seatBookingPerTierId;
	}

	public void setSeatBookingPerTierId(Integer seatBookingPerTierId) {
		this.seatBookingPerTierId = seatBookingPerTierId;
	}

	public Integer getTier() {
		return tier;
	}

	public void setTier(Integer tier) {
		this.tier = tier;
	}

	public Integer getNumOfSeats() {
		return numOfSeats;
	}

	public void setNumOfSeats(Integer numOfSeats) {
		this.numOfSeats = numOfSeats;
	}

	public SeatHold getSeatHold() {
		return seat_hold;
	}

	public void setSeatHold(SeatHold seat_hold) {
		this.seat_hold = seat_hold;
	}

	@Override
	public boolean equals(Object o) {
		return reflectionEquals(this, o);
	}

	@Override
	public int hashCode() {
		return reflectionHashCode(this);
	}
}
