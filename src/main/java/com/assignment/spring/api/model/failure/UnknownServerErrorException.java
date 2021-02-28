package com.assignment.spring.api.model.failure;

import org.springframework.web.client.RestClientResponseException;

public class UnknownServerErrorException extends RuntimeException {

    public UnknownServerErrorException(String message, RestClientResponseException ex) {
        super(message, ex);
    }
}
