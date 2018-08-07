package com.walmart.ticketservice.rest.request;

import java.io.Serializable;

public class ReserveAndCommitRequest implements Serializable {

	private static final long serialVersionUID = -6734884033990009576L;

	private int seatHoldId;
	private String customerEmail;

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

}
