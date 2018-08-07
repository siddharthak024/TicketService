package com.walmart.ticketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.walmart.ticketservice" })
public class TicketServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(TicketServiceApp.class, args);
	}
}