package com.weatherapi.demo.controllers;

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

    @GetMapping("/{pincode}/{date}")
    public ResponseEntity<?> getWeatherByPincodeAndDate(@PathVariable String pincode, @PathVariable String date) throws Exception {
        Weather weather = weatherService.getWeatherByPincodeAndDate(pincode, date);
        if (weather != null) {
            return ResponseEntity.ok(weather);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public String tres(){
        return "hii";
    }

    @PostMapping
    public ResponseEntity<?> saveWeather(@RequestBody Weather weather) {
        weatherService.saveWeather(weather);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
