# TicketService

Implement a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.

### Assumptions

1. There are only 34 seats per tier(1 to n) in venue, 1st being the best tier and 10 is least prefered tier. Seat numbers are not there in Venue.
2. SeatHolds are valid only for 60 seconds can be configurable. After 60 Secs these seats are again added back to available seats to their respective tiers. 
3. User can hold only MAX of 10 seats per transaction.
4. At the time of reserving tickets, user should use same registered email(which is used at the time of seatHold request otherwise app will throw exception).  

### Technology stack used:

Spring Boot 2.0.4, Database : HSQL in-memory DB.
END to End Testing: ATDD- Cucumber-java .
Functional Test: Spring-boot-test, Mockito, PowerMock. 
Build tool : Maven.

### Build and Running the application: 

1. Git clone the project locally and run "mvn clean install".
2. Run the command "java -jar target/ticketservice-api-0.1.0.jar" to start the API.
3. For testing open new terminal and cd to TicketService/TicketService-ATDD/ and run "mvn clean test", it will run different scenarios against the localy running server.

```
####Check for different test scenarios of the API in https://github.com/siddharthak024/TicketService/blob/master/TicketService-ATDD/src/test/resources/com/walmart/atdd/ProcessTicketServiceApi.feature
```

### Sample requests and Responses:

1. Check for availability of seats.

![TotalSeatAvailable](https://github.com/siddharthak024/TicketService/blob/master/images/TotalSeatAvailable.JPG)

2. Hold the seats to get seatHoldId.

![SeatHold](https://github.com/siddharthak024/TicketService/blob/master/images/SeatHold.JPG)

3. In the next 60 secs reserve the seats hold by ID and Registered Email, otherwise your hold will be expired.

![reserve](https://github.com/siddharthak024/TicketService/blob/master/images/reserve.JPG)

4. Check the availability of seats after reserving, available seats got decreased. 

![TotalSeatAvailableAfterBooking](https://github.com/siddharthak024/TicketService/blob/master/images/TotalSeatAvailableAfterBooking.JPG)

