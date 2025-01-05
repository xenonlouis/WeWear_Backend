package com.example.wewear_backend.security;

import com.example.wewear_backend.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Add debug logging
        System.out.println("Attempting to load user with username: " + username);

        // Try to find user by both username and email
        com.example.wewear_backend.Model.User appUser = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(username)
                        .orElseThrow(() -> {
                            System.out.println("User not found with username/email: " + username);
                            return new UsernameNotFoundException("User not found with username/email: " + username);
                        }));

        System.out.println("Found user: " + appUser.getUsername());

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .authorities("USER")
                .build();
    }
    }