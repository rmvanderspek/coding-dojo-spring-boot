package com.assignment;

import com.assignment.spring.Application;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(classes = Application.class)
@ActiveProfiles("tst")
public class IntegrationSteps {
    @Given("that the weather api is available")
    public void thatTheWeatherApiIsAvailable() {
    }

    @When("the client wants to get the weather for {string}")
    public void theClientWantsToGetTheWeatherForAmsterdam(String city) {
    }

    @Then("the client recieves a valid response")
    public void theClientRecievesAValidResponse() {
    }

    @And("the response has been persisted")
    public void theResponseHasBeenPersisted() {
    }
}
