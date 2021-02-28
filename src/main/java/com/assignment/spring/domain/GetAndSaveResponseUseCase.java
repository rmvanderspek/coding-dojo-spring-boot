package com.assignment.spring.domain;

import com.assignment.spring.repository.WeatherEntity;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

public interface GetAndSaveResponseUseCase {

    WeatherEntity execute(Command command);

    @Value
    @Builder
    class Command {
        @NotNull String city;
    }
}
