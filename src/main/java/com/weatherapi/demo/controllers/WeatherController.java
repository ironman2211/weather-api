package com.weatherapi.demo.controllers;

import com.weatherapi.demo.dto.ResponseDto;
import com.weatherapi.demo.models.Weather;
import com.weatherapi.demo.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{pinCode}/{date}")
    public ResponseEntity<?> getWeatherByPINCodeAndDate(@PathVariable String pinCode, @PathVariable String date) throws Exception {
        ResponseDto weather = weatherService.getWeatherByPINCodeAndDate(pinCode, date);
        if (weather != null) {
            return ResponseEntity.ok(weather);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
