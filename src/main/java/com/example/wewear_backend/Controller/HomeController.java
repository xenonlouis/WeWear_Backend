package com.example.wewear_backend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public String welcome() {
        return "Welcome to WeWear API - Your Fashion E-commerce Solution";
    }
} 