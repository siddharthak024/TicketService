package com.walmart.atdd;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
//@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/com/walmart/atdd/ProcessTicketServiceApi.feature"}, 
glue = {"com.walmart.atdd.steps"}, tags = {"@restservice"})
public class RESTRunnerTest extends AbstractTestNGCucumberTests{

}