package com.walmart.atdd.steps;

import java.util.HashMap;
import java.util.Map;

import com.walmart.atdd.util.RestUtil;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ProcessMyRetailApi {

	protected HashMap<String, String> headerMap = null;

	RestUtil restUtil = new RestUtil();

	@Given("^the API with the header information as$")
	public void headerInfo(Map<String, String> headers) throws Exception {
		headerMap = new HashMap<String, String>(headers);
	}

	@When("^the Client sends request to web service \"(.*?)\" using GET$")
	public void req(String ServiceURL) {
		String response = restUtil.getRequest(headerMap, ServiceURL);
		System.out.println("GET Response : " + response);
	}

	@When("^the Client sends request \"(.*?)\" to web service \"(.*?)\" using POST$")
	public void postRequest(String jsonText, String ServiceURL) {
		String response = restUtil.postRequest(headerMap, ServiceURL, jsonText);
		System.out.println("POST Response : " + response);
	}

	@Then("^the response status code should be \"(.*?)\"$")
	public void the_response_status_code_should_be(int statusCode) throws Throwable {
		restUtil.validateResponseCode(statusCode);
	}

}
