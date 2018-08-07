package com.walmart.ticketservice.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "venue")
public class Venue implements Serializable {

	private static final long serialVersionUID = 4795819778524562561L;

	@Id
	@Column(name = "tier")
	private int tier;

	@Column(name = "seats_per_tier_count")
	private int seatsPerTierCount;

	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	public int getSeatsPerTierCount() {
		return seatsPerTierCount;
	}

	public void setSeatsPerTierCount(int seatsPerTierCount) {
		this.seatsPerTierCount = seatsPerTierCount;
	}
}
