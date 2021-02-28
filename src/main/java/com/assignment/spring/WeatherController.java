package com.assignment.spring;

import com.assignment.spring.domain.GetAndSaveResponseUseCase;
import com.assignment.spring.repository.WeatherEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final GetAndSaveResponseUseCase getAndSaveResponseUseCase;

    @Transactional
    @GetMapping("/weather")
    public WeatherEntity weather(@RequestParam String city) {
        return getAndSaveResponseUseCase.execute(GetAndSaveResponseUseCase.Command.builder()
                .city(city)
                .build());
    }
}
