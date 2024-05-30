package com.weatherapi.demo.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Coordinates {
    private Double latitude;
    private Double longitude;
}
