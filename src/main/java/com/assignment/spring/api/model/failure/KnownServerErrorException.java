package com.assignment.spring.api.model.failure;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

@Value
public class KnownServerErrorException extends RuntimeException {
    HttpStatus httpStatus;

    public KnownServerErrorException(String message, HttpStatusCodeException ex) {
        super(message, ex);
        this.httpStatus = ex.getStatusCode();
    }
}
