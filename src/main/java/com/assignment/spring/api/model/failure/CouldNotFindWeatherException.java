package com.assignment.spring.api.model.failure;

import org.springframework.web.client.HttpClientErrorException;

public class CouldNotFindWeatherException extends RuntimeException {

    public CouldNotFindWeatherException(String message, HttpClientErrorException ex) {
        super(message, ex);
    }

    public CouldNotFindWeatherException(String message) {
        super(message);
    }
}
