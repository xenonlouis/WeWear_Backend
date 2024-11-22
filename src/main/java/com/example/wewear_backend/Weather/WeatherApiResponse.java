package com.example.wewear_backend.Weather;

import lombok.Data;
import java.util.List;

@Data
public class WeatherApiResponse {
    private MainData main;
    private List<WeatherData> weather;
    private String name;

    @Data
    public static class MainData {
        private double temp;
        private int humidity;
    }

    @Data
    public static class WeatherData {
        private String description;
    }
} 