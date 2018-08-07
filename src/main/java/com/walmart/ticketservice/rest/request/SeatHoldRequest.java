package com.walmart.ticketservice.rest.request;

import java.io.Serializable;

public class SeatHoldRequest implements Serializable {

	private static final long serialVersionUID = 5838822737291058789L;
	
	private int numofSeatsHold;
	private String custEmail;

	public int getNumofSeatsHold() {
		return numofSeatsHold;
	}

	public void setNumofSeatsHold(int numofSeatsHold) {
		this.numofSeatsHold = numofSeatsHold;
	}

	public String getCustEmail() {
		return custEmail;
	}

	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}

}
