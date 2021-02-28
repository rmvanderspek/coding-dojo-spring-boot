package com.assignment.spring.api;

import com.assignment.spring.api.model.WeatherResponse;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

public interface GetWeatherPort {
    WeatherResponse execute(Query query);

    @Value
    @Builder
    class Query {
        @NotNull String city;
    }
}
