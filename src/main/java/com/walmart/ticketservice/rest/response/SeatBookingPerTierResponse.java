package com.walmart.ticketservice.rest.response;

import java.io.Serializable;

public class SeatBookingPerTierResponse implements Serializable{

	private static final long serialVersionUID = 8414641213011852881L;
	
	private int tier;
	private int seatsHold;

	public int getSeatsHold() {
		return seatsHold;
	}

	public void setSeatsHold(int seatsHold) {
		this.seatsHold = seatsHold;
	}

	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}


}
