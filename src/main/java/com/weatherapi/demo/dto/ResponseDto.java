package com.weatherapi.demo.dto;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class ResponseDto implements Serializable {
    private String pinCode;
    private String date;
    private Coordinates coordinates;
    private Object weatherInfo;
}
