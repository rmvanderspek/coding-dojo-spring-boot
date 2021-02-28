package com.assignment.spring.api;

import com.assignment.spring.api.model.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class WeatherClient implements GetWeatherPort {

    private final RestTemplate restTemplate;

    @Value("${com.assignment.spring.weather-client.url}")
    private String hostUrl;


    @Override
    public WeatherResponse execute(Query query) {
        String url = hostUrl.replace("{city}", query.getCity());
        ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(url, WeatherResponse.class);

        return response.getBody();
    }
}
