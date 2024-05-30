package com.weatherapi.demo.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapi.demo.client.WeatherApiClient;
import com.weatherapi.demo.dao.WeatherApiDao;
import com.weatherapi.demo.dto.Coordinates;
import com.weatherapi.demo.dto.ResponseDto;
import com.weatherapi.demo.models.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class WeatherServiceTest {

    @Mock
    private GeocodingService geocodingService;

    @Mock
    private WeatherApiDao weatherApiDao;

    @Mock
    private WeatherApiClient weatherApiClient;

    @InjectMocks
    private WeatherService weatherService;

    String stringWeather;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        stringWeather = "{\"lat\":21.8309,\"lon\":83.9239,\"timezone\":\"Asia/Kolkata\",\"timezone_offset\":19800,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04n\"}]}]}";
    }

    @Test
    void testGetWeatherByPINCodeAndDate_Success() throws Exception {
        String pinCode = "123456";
        String date = LocalDate.now().toString();
        Weather weather = new Weather();
        weather.setLatitude(12.3456);
        weather.setLongitude(-78.9012);

        weather.setWeatherInfo(stringWeather);
        when(weatherApiDao.getWeatherByPinCodeAndDate(anyString(), anyString())).thenReturn(null);
        Coordinates build = Coordinates.builder().latitude(12.3456).longitude(-78.9012).build();
        when(geocodingService.getLatLongFromPINCode(pinCode)).thenReturn(build);
        when(weatherApiClient.getWeatherData(12.3456, -78.9012, 0)).thenReturn("Weather information");
        when(weatherApiDao.saveWeather(any(Weather.class))).thenReturn(weather);

        ResponseDto responseDto = weatherService.getWeatherByPINCodeAndDate(pinCode, date);

        ObjectMapper mapper = new ObjectMapper();
        Object objWeather = mapper.readValue(stringWeather, Object.class);
        assertNotNull(responseDto);
        assertEquals(pinCode, responseDto.getPinCode());
        assertEquals(date, responseDto.getDate());
        assertEquals(12.3456, responseDto.getCoordinates().getLatitude());
        assertEquals(-78.9012, responseDto.getCoordinates().getLongitude());
        assertEquals(objWeather, responseDto.getWeatherInfo());
    }

    @Test
    void testGetWeatherByPINCodeAndDate_FromCache() throws Exception {

        String pinCode = "123456";
        String date = LocalDate.now().toString();
        Weather weather = new Weather();
        weather.setLatitude(12.3456);
        weather.setLongitude(-78.9012);
        weather.setWeatherInfo(stringWeather);
        when(weatherApiDao.getWeatherByPinCodeAndDate(anyString(), anyString())).thenReturn(weather);

        ResponseDto responseDto = weatherService.getWeatherByPINCodeAndDate(pinCode, date);
        ObjectMapper mapper = new ObjectMapper();
        Object objWeather = mapper.readValue(stringWeather, Object.class);

        assertNotNull(responseDto);
        assertEquals(pinCode, responseDto.getPinCode());
        assertEquals(date, responseDto.getDate());
        assertEquals(12.3456, responseDto.getCoordinates().getLatitude());
        assertEquals(-78.9012, responseDto.getCoordinates().getLongitude());
        assertEquals(objWeather, responseDto.getWeatherInfo());

        verify(geocodingService, never()).getLatLongFromPINCode(anyString());
        verify(weatherApiClient, never()).getWeatherData(anyDouble(), anyDouble(), anyLong());
    }

}
