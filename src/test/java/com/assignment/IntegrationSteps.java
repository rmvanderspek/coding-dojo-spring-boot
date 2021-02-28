package com.assignment;

import com.assignment.spring.Application;
import com.assignment.spring.repository.WeatherEntity;
import com.assignment.spring.repository.WeatherRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.awaitility.Awaitility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@CucumberContextConfiguration
@SpringBootTest(classes = Application.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("tst")
public class IntegrationSteps {

    private WireMockServer weatherMockServer;
    @Autowired
    private WeatherRepository weatherRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    int port;
    private ResponseEntity<WeatherEntity> response;

    @Before
    public void prepare() {
        this.weatherMockServer = new WireMockServer(9090);
        weatherMockServer.start();
    }

    @Given("that the weather api is available for {string}")
    public void thatTheWeatherApiIsAvailable(String city) {
        this.weatherMockServer.stubFor(
                get((urlPathEqualTo("/api/weather")))
                .withQueryParam("q", equalTo(city))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("Amsterdam_response.json")));
    }

    @When("the client requests the weather for {string}")
    public void theClientWantsToGetTheWeatherForAmsterdam(String city) {
        String path = String.format("http://localhost:%d/weather?city=%s", port, city);
        this.response = restTemplate.getForEntity(path, WeatherEntity.class);
    }

    @Then("the client recieves a valid response")
    public void theClientRecievesAValidResponse() {
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        WeatherEntity weatherResponse = response.getBody();
        assertThat(weatherResponse.getCity(), is("Amsterdam"));
        assertThat(weatherResponse.getCountry(), is("NL"));
        assertEquals(1, weatherResponse.getId().intValue());
        assertThat(weatherResponse.getTemperature(), is(282.12));
    }

    @And("the response has been persisted")
    public void theResponseHasBeenPersisted() {
        List<WeatherEntity> weatherEntities = (List<WeatherEntity>) weatherRepository.findAll();
        assertEquals(1, weatherEntities.size());
        WeatherEntity entity = weatherEntities.get(0);
        assertThat(entity.getCity(), is("Amsterdam"));
        assertThat(entity.getCountry(), is("NL"));
        assertEquals(1, entity.getId().intValue());
        assertThat(entity.getTemperature(), is(282.12));
    }

    @After
    public void tearDown() {
        this.weatherMockServer.stop();
        Awaitility.await()
                .atMost(Duration.ofSeconds(5L))
                .until(() -> !weatherMockServer.isRunning());

        this.response = null;
    }
}
