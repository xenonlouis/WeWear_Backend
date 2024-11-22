package com.example.wewear_backend.Controller;

import com.example.wewear_backend.Service.WeatherService;
import com.example.wewear_backend.Weather.WeatherResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    public Mono<WeatherResponse> getWeatherByCity(@PathVariable String city) {
        return weatherService.getWeatherByCity(city);
    }
} 