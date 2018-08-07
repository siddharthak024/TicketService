package com.walmart.ticketservice.rest.response;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class SeatHoldResponse implements Serializable {

	private static final long serialVersionUID = -3887896630833862014L;
	
	private int seatHoldId;
	private String customerEmail;
	private Timestamp createdTimestamp;
	private List<SeatBookingPerTierResponse> seatBookingPerTierResponse;

	public int getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(int seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Timestamp getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public List<SeatBookingPerTierResponse> getSeatBookingPerTierResponse() {
		return seatBookingPerTierResponse;
	}

	public void setSeatBookingPerTierResponse(List<SeatBookingPerTierResponse> seatBookingPerTierResponse) {
		this.seatBookingPerTierResponse = seatBookingPerTierResponse;
	}

}