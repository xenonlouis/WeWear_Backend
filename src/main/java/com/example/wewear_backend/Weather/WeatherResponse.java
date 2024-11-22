package com.example.wewear_backend.Weather;

import lombok.Data;

@Data
public class WeatherResponse {
    private String city;
    private double temperature;
    private int humidity;
    private String description;
    private String recommendation;
} 