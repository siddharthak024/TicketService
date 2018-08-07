package com.walmart.atdd.util;

import java.util.HashMap;

import com.jayway.restassured.response.Response;

import junit.framework.Assert;

import static com.jayway.restassured.RestAssured.given;

public class RestUtil {

	HashMap<String, String> mapReqHeaders = new HashMap<String, String>();
	Response response = null;
	private final String baseUrl = "http://localhost:8080";

	public String getRequest(HashMap<String, String> header, String ServiceURL) {
		mapReqHeaders = header;
		response = given().log().all().headers(mapReqHeaders).
				urlEncodingEnabled(false).when().
				get(baseUrl + ServiceURL);
		return response.asString();
	}

	public String postRequest(HashMap<String, String> header, String ServiceURL, String jsonText) {
		mapReqHeaders = header;
		response = given().log().all().headers(mapReqHeaders).
				body(jsonText).urlEncodingEnabled(false).when().
			    post(baseUrl + ServiceURL);
		return response.asString();
	}

	public void validateResponseCode(int statusCode) {
		int expectedStatusCode = response.getStatusCode();
		Assert.assertEquals(expectedStatusCode, statusCode);
		System.out.println("Passed response status code matches expected" + expectedStatusCode);
	}
}
