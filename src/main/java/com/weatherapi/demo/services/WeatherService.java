package com.weatherapi.demo.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapi.demo.client.WeatherApiClient;
import com.weatherapi.demo.dao.WeatherApiDao;
import com.weatherapi.demo.dto.Coordinates;
import com.weatherapi.demo.dto.ResponseDto;
import com.weatherapi.demo.models.Weather;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Slf4j
@Service
@CacheConfig(cacheNames="weather")
public class WeatherService {

    @Autowired
    private GeocodingService geocodingService;

    @Autowired
    private WeatherApiDao weatherApiDao;

    @Autowired
    private WeatherApiClient weatherApiClient;

    @Cacheable(key = "{#pinCode, #date}")
    public ResponseDto getWeatherByPINCodeAndDate(String pinCode, String date) throws Exception {
        Weather weather = null;
        try {
            weather = weatherApiDao.getWeatherByPinCodeAndDate(pinCode, date);
            if (weather == null) weather = getWeatherFromCoordinated(pinCode, date);
            if (weather != null) {
                Coordinates coordinates = Coordinates.builder()
                        .latitude(weather.getLatitude())
                        .longitude(weather.getLongitude())
                        .build();
                ObjectMapper mapper = new ObjectMapper();
                Object weatherInfo = mapper.readValue(weather.getWeatherInfo(), Object.class);
                return ResponseDto.builder()
                        .coordinates(coordinates)
                        .pinCode(pinCode)
                        .date(date)
                        .weatherInfo(weatherInfo)
                        .build();
            } else throw new Exception("Exception in getting weather data");
        } catch (Exception exception) {
            log.info("Exception in getting weather info");
            throw exception;
        }
    }

    public Weather getWeatherFromCoordinated(String pinCode, String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            long unixTime = instant.getEpochSecond();
            Coordinates latLongFromPINCode = geocodingService.getLatLongFromPINCode(pinCode);
            Double latitude = latLongFromPINCode.getLatitude();
            Double longitude = latLongFromPINCode.getLongitude();
            if (latitude != null && longitude != null) {
                String response = weatherApiClient.getWeatherData(latitude, longitude, unixTime);
                Weather newWeather = new Weather();
                newWeather.setPincode(pinCode);
                newWeather.setLatitude(latitude);
                newWeather.setLongitude(longitude);
                newWeather.setDate(date);
                newWeather.setWeatherInfo(response);
                return weatherApiDao.saveWeather(newWeather);
            } else throw new Exception("latitude longitude not found for current RefId...");
        } catch (Exception exception) {
            log.error("Exception during getting weather information...");
        }
        return null;
    }
}
