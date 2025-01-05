package com.example.wewear_backend.dto;

import com.example.wewear_backend.Model.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
@Data
public class UserDto {
    private Integer id;
    private String username;
    private String bio;
    private String profileImage;
    private List<UserDto> followers;
    private List<UserDto> followings;

    // Constructor
    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.profileImage = user.getProfileImage();
        this.followers = user.getFollowers().stream()
                .map(follower -> new UserDto(follower.getId(), follower.getUsername()))
                .collect(Collectors.toList());
        this.followings = user.getFollowings().stream()
                .map(following -> new UserDto(following.getId(), following.getUsername()))
                .collect(Collectors.toList());
    }
    // Simple constructor for follower/following lists
    private UserDto(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserDto() {

    }
}
