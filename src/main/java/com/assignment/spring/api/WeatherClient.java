package com.assignment.spring.api;

import com.assignment.spring.api.model.WeatherResponse;
import com.assignment.spring.api.model.failure.CouldNotFindWeatherException;
import com.assignment.spring.api.model.failure.KnownServerErrorException;
import com.assignment.spring.api.model.failure.UnknownServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

@Component
@RequiredArgsConstructor
public class WeatherClient implements GetWeatherPort {

    private final RestTemplate restTemplate;

    @Value("${com.assignment.spring.weather-client.url}")
    private String hostUrl;

    public static final String NOT_FOUND_MESSAGE = "Could not find weather for city %s.";
    public static final String SERVER_ERROR_MESSAGE = "We could not connect to our backend service. Please try again later.";

    @Override
    public WeatherResponse execute(Query query) {
        String url = hostUrl.replace("{city}", query.getCity());
        try {
            ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(url, WeatherResponse.class);

            if(response.getBody() == null) {
                throw new CouldNotFindWeatherException(String.format(NOT_FOUND_MESSAGE, query.getCity()));
            }
            return response.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new CouldNotFindWeatherException(String.format(NOT_FOUND_MESSAGE, query.getCity()), ex);
        } catch (HttpStatusCodeException ex) {
            throw new KnownServerErrorException(SERVER_ERROR_MESSAGE, ex);
        } catch (RestClientException ex) {
            throw new UnknownServerErrorException(SERVER_ERROR_MESSAGE, ex);
        }
    }
}
