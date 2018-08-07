package com.walmart.ticketservice.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.walmart.ticketservice.db.entity.TicketConformation;

public interface ReserveAndCommitRepo extends JpaRepository<TicketConformation, String>{

}
