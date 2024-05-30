package com.weatherapi.demo.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseDto {
    private String pinCode;
    private String date;
    private Coordinates coordinates;
    private Object weatherInfo;
}
