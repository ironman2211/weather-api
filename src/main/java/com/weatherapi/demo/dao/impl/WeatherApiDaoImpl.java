package com.weatherapi.demo.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapi.demo.dao.WeatherApiDao;
import com.weatherapi.demo.dto.ResponseDto;
import com.weatherapi.demo.models.Weather;
import com.weatherapi.demo.repositories.WeatherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WeatherApiDaoImpl implements WeatherApiDao {

    @Autowired
    private WeatherRepository weatherRepository;

    @Override
    public Weather getWeatherByPinCodeAndDate(String pinCode, String date) {
        try {
            return weatherRepository.findByPincodeAndDate(pinCode, date);
        } catch (Exception exception) {
            log.error("exception in getting Data");
        }
        return null;
    }

    @Override
    public Weather saveWeather(Weather weather) {
        try {
            return weatherRepository.save(weather);
        }catch (Exception exception){
            log.error("exception in saving data");
        }
        return null;
    }


}
