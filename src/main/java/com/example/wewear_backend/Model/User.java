package com.example.wewear_backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*; // Changed to jakarta.persistence.*

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String email;
    private String password;
    private String profileImage;
    private String bio;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "followings")
    private List<User> followers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_following",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private List<User> followings = new ArrayList<>();

    // Remove @JsonIgnore from getters
    public List<User> getFollowers() {
        return followers;
    }

    public List<User> getFollowings() {
        return followings;
    }

    // Add a method to get counts only
    @JsonProperty("followersCount")
    public int getFollowersCount() {
        return followers != null ? followers.size() : 0;
    }
    @JsonProperty("followingsCount")
    public int getFollowingsCount() {
        return followings != null ? followings.size() : 0;
    }
    // Add this to prevent infinite recursion
    @JsonIgnore
    public List<User> getFollowersWithoutRecursion() {
        return followers.stream()
                .map(user -> {
                    User u = new User();
                    u.setId(user.getId());
                    u.setUsername(user.getUsername());
                    u.setProfileImage(user.getProfileImage());
                    return u;
                })
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public List<User> getFollowingsWithoutRecursion() {
        return followings.stream()
                .map(user -> {
                    User u = new User();
                    u.setId(user.getId());
                    u.setUsername(user.getUsername());
                    u.setProfileImage(user.getProfileImage());
                    return u;
                })
                .collect(Collectors.toList());
    }

}
