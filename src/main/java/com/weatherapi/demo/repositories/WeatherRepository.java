package com.weatherapi.demo.repositories;

import com.weatherapi.demo.models.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
    Weather findByPincodeAndDate(String pincode, String date);
}
