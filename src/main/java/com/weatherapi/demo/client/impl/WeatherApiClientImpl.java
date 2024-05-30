package com.weatherapi.demo.client.impl;

import com.weatherapi.demo.client.WeatherApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherApiClientImpl implements WeatherApiClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openweather.api.key}")
    private String openWeatherApiKey;

    @Override
    public String getWeatherData(double latitude, double longitude, long unixTime) {
        String url = String.format("http://api.openweathermap.org/data/2.5/onecall/timemachine?lat=%s&lon=%s&dt=%d&appid=%s",
                latitude, longitude, unixTime, openWeatherApiKey);
        return restTemplate.getForObject(url, String.class);
    }
}
