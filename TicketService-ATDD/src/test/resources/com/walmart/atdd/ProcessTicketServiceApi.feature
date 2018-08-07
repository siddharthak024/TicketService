Feature: To validate TicketService API functionality

	@restservice
	Scenario Outline: To check whether TicketService GET service returns successful response
	Given the API with the header information as
		|Accept|application/json|
		|Content-Type|application/json|
	When the Client sends request to web service "<ServiceURL>" using GET
	Then the response status code should be "<statusCode>"
	
		Examples:
			|ServiceURL|statusCode|
			|/venue/totalseatsavailable|200|			

	@restservice
	Scenario Outline: To check whether TicketService findandhold POST service returns successful response
	Given the API with the header information as
		|Accept|application/json|
		|Content-Type|application/json|
	When the Client sends request "<request>" to web service "<ServiceURL>" using POST
	Then the response status code should be "<statusCode>"
	
		Examples:
			|request|ServiceURL|statusCode|
			|{"numofSeatsHold" : 8,"custEmail" : "sid@k.com"}|/venue/findandhold|200|
			|{"numofSeatsHold" : 8,"custEmail" : "BADEMAIL.com"}|/venue/findandhold|400|
			|{"numofSeatsHold" : 100,"custEmail" : "sid@k.com"}|/venue/findandhold|400|
			|{"numofSeatsHold" : 0,"custEmail" : "sid@k.com"}|/venue/findandhold|400|
			|{"numofSeatsHold" : 10,"custEmail" : "sidhu@k.com"}|/venue/findandhold|200|
			
	@restservice
	Scenario Outline: To check whether TicketService reservetickets POST service returns successful response
	Given the API with the header information as
		|Accept|application/json|
		|Content-Type|application/json|
	When the Client sends request "<request>" to web service "<ServiceURL>" using POST
	Then the response status code should be "<statusCode>"
	
		Examples:
			|request|ServiceURL|statusCode|
			|{"seatHoldId":1,"customerEmail":"sid@k.com"}|/venue/reservetickets|200|
			|{"seatHoldId":1,"customerEmail":"sid@k.com"}|/venue/reservetickets|400|
			|{"seatHoldId":2,"customerEmail":"sid@k.com"}|/venue/reservetickets|400|
			|{"seatHoldId":2,"customerEmail":"WRONGEMAIL@k.com"}|/venue/reservetickets|400|
			|{"seatHoldId":2,"customerEmail":"sidhu@k.com"}|/venue/reservetickets|200|
