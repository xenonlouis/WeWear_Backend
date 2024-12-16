package com.example.wewear_backend.Service;

import com.example.wewear_backend.Model.Post;
import com.example.wewear_backend.Model.User;
import com.example.wewear_backend.Repository.PostRepository;
import com.example.wewear_backend.dto.CreatePostRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Post createPost(CreatePostRequest postRequest, User user) {
        Post post = new Post();
        post.setContent(postRequest.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setImageUrl(postRequest.getImageUrl());
        post.setUser(user); // Associez l'utilisateur à la publication
        return postRepository.save(post);
    }

    public Post updatePost(Integer id, Post postDetails) {
        Post post = getPostById(id);
        post.setContent(postDetails.getContent());
        post.setImageUrl(postDetails.getImageUrl());
        return postRepository.save(post);
    }

    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }
}
