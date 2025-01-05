package com.example.wewear_backend.Controller;

import com.example.wewear_backend.Model.User;
import com.example.wewear_backend.Repository.UserRepository;
import com.example.wewear_backend.Service.TestDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-data")
public class TestDataController {
    private final TestDataService testDataService;
    private final UserRepository userRepository;

    public TestDataController(TestDataService testDataService, UserRepository userRepository) {
        this.testDataService = testDataService;
        this.userRepository = userRepository;
    }

    @PostMapping("/inject")
    public ResponseEntity<String> injectTestData(Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            testDataService.injectTestData(user.getId());
            return ResponseEntity.ok("Test data injected successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to inject test data: " + e.getMessage());
        }
    }
} 