package com.walmart.ticketservice.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ticket_conf")
public class TicketConformation implements Serializable {

	private static final long serialVersionUID = -4956303366264704443L;

	@Id
	@Column(name = "conf_id")
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String conformationId;

	@Column(name = "cust_email")
	private String customerEmail;

	@Column(name = "seat_hold_id")
	private int seatHoldId;

	public String getConformationId() {
		return conformationId;
	}

	public void setConformationId(String conformationId) {
		this.conformationId = conformationId;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public int getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(int seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

}
