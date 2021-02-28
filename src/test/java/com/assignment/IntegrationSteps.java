package com.assignment;

import com.assignment.spring.Application;
import com.assignment.spring.repository.WeatherEntity;
import com.assignment.spring.repository.WeatherRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.awaitility.Awaitility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

@CucumberContextConfiguration
@SpringBootTest(classes = Application.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("tst")
public class IntegrationSteps {

    private WireMockServer weatherMockServer;
    @Autowired
    private WeatherRepository weatherRepository;
    @Autowired
    private RestTemplate restTemplate;
    @LocalServerPort
    int port;

    private ResponseEntity<WeatherEntity> response;
    private HttpStatus faultStatus;


    @Given("that the weather api is available")
    public void thatTheWeatherApiIsAvailable() {
        this.weatherMockServer = new WireMockServer(9090);
        weatherMockServer.start();

        this.weatherMockServer.stubFor(
                get((urlPathEqualTo("/api/weather")))
                .withQueryParam("q", equalTo("Amsterdam"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("Amsterdam_response.json")));

        this.weatherMockServer.stubFor(
                get((urlPathEqualTo("/api/weather")))
                        .withQueryParam("q", equalTo("Amsterdamned"))
                        .willReturn(aResponse()
                                .withStatus(404)));

        String nullString = null;
        this.weatherMockServer.stubFor(
                get((urlPathEqualTo("/api/weather")))
                        .withQueryParam("q", equalTo("Berlikum"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody(nullString)));

        this.weatherMockServer.stubFor(
                get((urlPathEqualTo("/api/weather")))
                        .withQueryParam("q", equalTo("Almere"))
                        .willReturn(aResponse()
                                .withStatus(401)));
    }

    @When("the client requests the weather for {string}")
    public void theClientWantsToGetTheWeatherForAmsterdam(String city) {
        String path = String.format("http://localhost:%d/weather?city=%s", port, city);
        try {
            this.response = restTemplate.getForEntity(path, WeatherEntity.class);
        } catch(HttpStatusCodeException ex) {
            this.faultStatus = ex.getStatusCode();
        }
    }

    @Then("the client receives a valid {int} response")
    public void theClientReceivesAValidResponse(int responseStatusValue) {
        assertNull(response);
        assertEquals(responseStatusValue, faultStatus.value());
    }

    @Then("the client receives a valid response")
    public void theClientRecievesAValidResponse() {
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        WeatherEntity weatherResponse = response.getBody();
        assertThat(weatherResponse.getCity(), is("Amsterdam"));
        assertThat(weatherResponse.getCountry(), is("NL"));
        assertEquals(1, weatherResponse.getId().intValue());
        assertThat(weatherResponse.getTemperature(), is(282.12));
    }

    @Then("the response has been persisted")
    public void theResponseHasBeenPersisted() {
        List<WeatherEntity> weatherEntities = (List<WeatherEntity>) weatherRepository.findAll();
        assertEquals(1, weatherEntities.size());
        WeatherEntity entity = weatherEntities.get(0);
        assertThat(entity.getCity(), is("Amsterdam"));
        assertThat(entity.getCountry(), is("NL"));
        assertEquals(1, entity.getId().intValue());
        assertThat(entity.getTemperature(), is(282.12));
    }

    @Then("nothing will have been persisted")
    public void nothingWillHaveBeenPersisted() {
        List<WeatherEntity> weatherEntities = (List<WeatherEntity>) weatherRepository.findAll();
        assertEquals(0, weatherEntities.size());
    }

    @Then("the client will not have been bothered with this request")
    public void theClientWillNotHaveBeenBotheredWithThisRequest() {
        weatherMockServer.verify(0, getRequestedFor(anyUrl()));
    }

    @After
    public void tearDown() {
        if(weatherMockServer !=  null) {
            this.weatherMockServer.stop();
            Awaitility.await()
                    .atMost(Duration.ofSeconds(5L))
                    .until(() -> !weatherMockServer.isRunning());
        }

        weatherRepository.deleteAll();

        this.response = null;
        this.faultStatus = null;
    }
}
