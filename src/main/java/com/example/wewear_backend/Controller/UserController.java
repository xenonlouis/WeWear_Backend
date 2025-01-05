package com.example.wewear_backend.Controller;

import com.example.wewear_backend.Model.ClothingItem;
import com.example.wewear_backend.Model.User;
import com.example.wewear_backend.Repository.UserRepository;
import com.example.wewear_backend.Service.wardrobeService;
import com.example.wewear_backend.dto.UserDto;
import com.example.wewear_backend.security.JwtUtils;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    private final wardrobeService wardrobeService;

    public UserController(UserRepository userRepository, JwtUtils jwtUtils ,  wardrobeService wardrobeService ) {
        this.wardrobeService = wardrobeService;

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



        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("profileImage", user.getProfileImage());
        response.put("bio", user.getBio());
        response.put("createdAt", user.getCreatedAt());
        response.put("updatedAt", user.getUpdatedAt());
        response.put("followers", user.getFollowersWithoutRecursion());
        response.put("followings", user.getFollowingsWithoutRecursion());
        response.put("followersCount", user.getFollowersCount());
        response.put("followingsCount", user.getFollowingsCount());

        return ResponseEntity.ok(response);
    }
    @GetMapping("/me/clothing-items")
    public ResponseEntity<List<ClothingItem>> getCurrentUserClothingItems(Authentication authentication) {
        // Get username from Spring Security User
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        System.out.println("Username: " + username);

        // Find your custom User by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("User ID: " + user.getId());

        List<ClothingItem> clothingItems = wardrobeService.getClothingItemsByUserId(user.getId());
        System.out.println("Found clothing items: " + clothingItems.size());
        System.out.println("Clothing items: " + clothingItems);
        return ResponseEntity.ok(clothingItems);
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
    @GetMapping("/search/{query}")
    public ResponseEntity<List<User>> searchUsers(@PathVariable String query) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(query);
        // Remove sensitive data
        users.forEach(user -> user.setPassword(null));
        return ResponseEntity.ok(users);
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
    //follow
    @PostMapping("/{id}/follow")
    public ResponseEntity<?> followUser(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        User userToFollow = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User to follow not found"));

        if (currentUser.getId() == id) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Cannot follow yourself"));
        }

        // Initialize collections if null
        if (currentUser.getFollowings() == null) {
            currentUser.setFollowings(new ArrayList<>());
        }
        if (userToFollow.getFollowers() == null) {
            userToFollow.setFollowers(new ArrayList<>());
        }

        // Add to both collections if not already following
        if (!currentUser.getFollowings().contains(userToFollow)) {
            currentUser.getFollowings().add(userToFollow);
            userToFollow.getFollowers().add(currentUser);

            // Save both users
            userRepository.save(currentUser);
            userRepository.save(userToFollow);
        }

        return ResponseEntity.ok(Map.of(
                "message", "Followed user successfully",
                "followersCount", userToFollow.getFollowers().size(),
                "followingCount", currentUser.getFollowings().size()
        ));
    }
    @DeleteMapping("/{id}/unfollow")
    public ResponseEntity<?> unfollowUser(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        User userToUnfollow = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User to unfollow not found"));

        if (!currentUser.getFollowings().contains(userToUnfollow)) {
            return ResponseEntity.badRequest().body(new ErrorResponse("You are not following this user"));
        }

        currentUser.getFollowings().remove(userToUnfollow);
        userToUnfollow.getFollowers().remove(currentUser);

        userRepository.save(currentUser);
        userRepository.save(userToUnfollow);

        return ResponseEntity.ok("Unfollowed user successfully");
    }
    @GetMapping("/{id}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user.getFollowers());
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<?> getFollowing(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user.getFollowings());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("profileImage", user.getProfileImage());
        response.put("bio", user.getBio());
        response.put("createdAt", user.getCreatedAt());
        response.put("updatedAt", user.getUpdatedAt());
        response.put("followers", user.getFollowersWithoutRecursion());
        response.put("followings", user.getFollowingsWithoutRecursion());
        response.put("followersCount", user.getFollowersCount());
        response.put("followingsCount", user.getFollowingsCount());

        return ResponseEntity.ok(response);
    }




}


