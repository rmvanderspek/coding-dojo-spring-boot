package com.assignment.spring.domain;

import com.assignment.spring.api.model.failure.BadRequestException;
import com.assignment.spring.repository.WeatherEntity;
import com.assignment.spring.api.GetWeatherPort;
import com.assignment.spring.api.model.WeatherResponse;
import com.assignment.spring.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class GetAndSaveResponseUseCaseImpl implements GetAndSaveResponseUseCase {

    private final GetWeatherPort getWeatherPort;
    private final WeatherRepository weatherRepository;

    @Override
    public WeatherEntity execute(Command command) {

        if(StringUtils.isEmpty(command.getCity())) {
            throw new BadRequestException("An empty city can never be found.");
        }

        WeatherResponse weatherResponse = getWeatherPort.execute(GetWeatherPort.Query.builder()
                .city(command.getCity())
                .build());

        WeatherEntity weatherEntity = map(weatherResponse);

        return weatherRepository.save(weatherEntity);
    }

    private static WeatherEntity map(WeatherResponse weatherResponse) {
        WeatherEntity entity = new WeatherEntity();
        entity.setCity(weatherResponse.getName());
        entity.setCountry(weatherResponse.getSys().getCountry());
        entity.setTemperature(weatherResponse.getMain().getTemp());
        return entity;
    }
}
