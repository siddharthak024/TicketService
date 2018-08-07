package com.walmart.ticketservice.db.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer implements Serializable {

	private static final long serialVersionUID = 8112492518037261707L;

	@Id
	@Column(name = "cust_email")
	private String customerEmail;

	@Column(name = "credt_ts")
	private Timestamp createdTimestamp;

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Timestamp getCreatedTimestamp() {
		return createdTimestamp == null ? null : new Timestamp(createdTimestamp.getTime());
	}

	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = (createdTimestamp == null ? null : new Timestamp(createdTimestamp.getTime()));
	}
}
