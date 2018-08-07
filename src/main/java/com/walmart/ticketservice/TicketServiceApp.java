package com.walmart.ticketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = { "com.walmart.ticketservice" })
@EnableScheduling
public class TicketServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(TicketServiceApp.class, args);
	}
}