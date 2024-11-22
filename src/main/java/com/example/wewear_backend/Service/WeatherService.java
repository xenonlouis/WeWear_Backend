package com.example.wewear_backend.Service;

import com.example.wewear_backend.Weather.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.example.wewear_backend.Weather.WeatherApiResponse;
@Service
public class WeatherService {

    private final String apiKey;
    private final WebClient webClient;

    public WeatherService(@Value("${openweather.api.key}") String apiKey) {
        this.apiKey = apiKey;
        System.out.println("API Key initialized: " + apiKey);
        
        this.webClient = WebClient.builder()
                .baseUrl("http://api.openweathermap.org/data/2.5")
                .build();
    }

    public Mono<WeatherResponse> getWeatherByCity(String city) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(WeatherApiResponse.class)
                .map(this::mapToWeatherResponse);
    }

    private WeatherResponse mapToWeatherResponse(WeatherApiResponse apiResponse) {
        WeatherResponse response = new WeatherResponse();
        response.setCity(apiResponse.getName());
        response.setTemperature(apiResponse.getMain().getTemp());
        response.setHumidity(apiResponse.getMain().getHumidity());
        response.setDescription(apiResponse.getWeather().get(0).getDescription());
        
        // Add clothing recommendations based on temperature
        response.setRecommendation(getClothingRecommendation(apiResponse.getMain().getTemp()));
        
        return response;
    }

    private String getClothingRecommendation(double temperature) {
        if (temperature > 25) {
            return "Hot weather: Consider lightweight, breathable clothing like t-shirts and shorts.";
        } else if (temperature > 15) {
            return "Mild weather: A light jacket or long sleeves would be comfortable.";
        } else if (temperature > 5) {
            return "Cool weather: Wear a warm jacket and consider layering.";
        } else {
            return "Cold weather: Bundle up with a heavy coat, scarf, and gloves.";
        }
    }
} 