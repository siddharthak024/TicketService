package com.walmart.ticketservice.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.walmart.ticketservice.db.entity.Customer;

public interface HoldCustomerDetailsRepo extends JpaRepository<Customer, String> {

}
