package com.assignment.spring.api.model.failure;

import org.springframework.web.client.RestClientException;

public class UnknownServerErrorException extends RuntimeException {

    public UnknownServerErrorException(String message, RestClientException ex) {
        super(message, ex);
    }
}
