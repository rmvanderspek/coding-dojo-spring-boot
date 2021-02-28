package com.assignment.spring.config;


import com.assignment.spring.api.model.failure.BadRequestException;
import com.assignment.spring.api.model.failure.CouldNotFindWeatherException;
import com.assignment.spring.api.model.failure.KnownServerErrorException;
import com.assignment.spring.api.model.failure.UnknownServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(CouldNotFindWeatherException.class)
    public ResponseEntity<String> couldNotFindWeatherException(CouldNotFindWeatherException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UnknownServerErrorException.class)
    public ResponseEntity<String> unknownServerErrorException(UnknownServerErrorException ex) {
        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(ex.getMessage());
    }

    @ExceptionHandler(KnownServerErrorException.class)
    public ResponseEntity<String> knownServerErrorException(KnownServerErrorException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> badRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
