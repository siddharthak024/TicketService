package com.walmart.ticketservice.db.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "seat_hold")
public class SeatHold implements Serializable {

	private static final long serialVersionUID = -2738908195962247999L;

	@Id
	@Column(name = "seat_hold_id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seat_id_seq_gen")
	@SequenceGenerator(name = "seat_id_seq_gen", sequenceName = "seat_id_seq")
	private Integer seatHoldId;

	@Column(name = "cust_email")
	private String customerEmail;

	@Column(name = "valid_flag")
	private boolean validFlag;

	@Column(name = "is_expired")
	private boolean isExpired;

	@Column(name = "credt_ts")
	private Timestamp createdTimestamp;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "seat_hold", cascade = CascadeType.ALL)
	private List<SeatBookingPerTier> seatBookingPerTier = new ArrayList<>();

	public void addSeatBookingPerTier(SeatBookingPerTier bookingPerTier) {
		seatBookingPerTier.add(bookingPerTier);
		bookingPerTier.setSeatHold(this);
	}

	public Integer getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(Integer seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	// public int getHoldedSeats() {
	// return holdedSeats;
	// }
	//
	// public void setHoldedSeats(int holdedSeats) {
	// this.holdedSeats = holdedSeats;
	// }
	//
	// public int getTier() {
	// return tier;
	// }
	//
	// public void setTier(int tier) {
	// this.tier = tier;
	// }

	public boolean isValidFlag() {
		return validFlag;
	}

	public void setValidFlag(boolean validFlag) {
		this.validFlag = validFlag;
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}

	public Timestamp getCreatedTimestamp() {
		return createdTimestamp == null ? null : new Timestamp(createdTimestamp.getTime());
	}

	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = (createdTimestamp == null ? null : new Timestamp(createdTimestamp.getTime()));
	}

	public List<SeatBookingPerTier> getSeatBookingPerTier() {
		return (seatBookingPerTier != null ? new ArrayList<SeatBookingPerTier>(seatBookingPerTier) : null);
	}

	public void setSeatBookingPerTier(List<SeatBookingPerTier> seatBookingPerTier) {
		this.seatBookingPerTier = (seatBookingPerTier != null ? new ArrayList<SeatBookingPerTier>(seatBookingPerTier)
				: null);
	}

}
