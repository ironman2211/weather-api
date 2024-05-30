package com.weatherapi.demo.services;

import com.weatherapi.demo.models.Weather;
import com.weatherapi.demo.repositories.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private GeocodingService geocodingService;

    @Value("${openweather.api.key}")
    private String openWeatherApiKey;

    @Autowired
    private  RestTemplate restTemplate;

    @Cacheable(value = "weather", key = "{#pincode, #date}")
    public Weather getWeatherByPincodeAndDate(String pincode, String date) throws Exception {
        Weather weather = weatherRepository.findByPincodeAndDate(pincode, date);
        if (weather != null) {
            return weather;
        } else {
            // Get latitude and longitude from pincode
            double[] latLong = geocodingService.getLatLongFromPincode(pincode);
            if (latLong != null) {
                double lat = latLong[0];
                double lon = latLong[1];

                // Convert date to Unix timestamp
                LocalDate localDate = LocalDate.parse(date);
                Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
                long unixTime = instant.getEpochSecond();

                // Fetch weather data from OpenWeather Time Machine API
                String url = "http://api.openweathermap.org/data/2.5/onecall/timemachine?lat=" + lat + "&lon=" + lon + "&dt=" + unixTime + "&appid=" + openWeatherApiKey;
                String response = restTemplate.getForObject(url, String.class);
//                JSONObject jsonObject = new JSONObject(response);
//                String weatherInfo = jsonObject.toString();
//
//                // Save to database
//                Weather newWeather = new Weather();
//                newWeather.setPincode(pincode);
//                newWeather.setLatitude(lat);
//                newWeather.setLongitude(lon);
//                newWeather.setDate(date);
//                newWeather.setWeatherInfo(weatherInfo);
//                weatherRepository.save(newWeather);

                return null;
            }
        }
        return null;
    }

    public void saveWeather(Weather weather) {
        weatherRepository.save(weather);
    }
}
