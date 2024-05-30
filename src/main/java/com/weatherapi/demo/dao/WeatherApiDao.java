package com.weatherapi.demo.dao;

import com.weatherapi.demo.models.Weather;

public interface WeatherApiDao {
    public Weather getWeatherByPinCodeAndDate(String pinCode,String date);
    public Weather saveWeather(Weather weather);

}
