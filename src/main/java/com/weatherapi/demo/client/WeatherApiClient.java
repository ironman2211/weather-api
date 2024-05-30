package com.weatherapi.demo.client;

public interface WeatherApiClient {
    String getWeatherData(double latitude, double longitude, long unixTime);
}
