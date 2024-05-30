package com.weatherapi.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class Coordinates implements Serializable {
    private Double latitude;
    private Double longitude;
}
