package com.example.wewear_backend.Controller;

import com.example.wewear_backend.Model.User;
import com.example.wewear_backend.Repository.UserRepository;
import com.example.wewear_backend.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public UserController(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    public static class UpdateUserRequest {
        public String email;
        public String username;
        // Add other fields you want to allow updating
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Remove sensitive information
        user.setPassword(null);
        
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(@RequestBody UpdateUserRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate unique constraints
        if (request.username != null && !request.username.equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.username)) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Username already exists"));
            }
            user.setUsername(request.username);
        }

        if (request.email != null && !request.email.equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.email)) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Email already exists"));
            }
            user.setEmail(request.email);
        }

        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Remove sensitive information
        user.setPassword(null);
        
        return ResponseEntity.ok(user);
    }

    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}