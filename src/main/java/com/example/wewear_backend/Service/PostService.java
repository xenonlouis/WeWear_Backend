package com.example.wewear_backend.Service;

import com.example.wewear_backend.Model.Post;
import com.example.wewear_backend.Repository.PostRepository;
import org.springframework.stereotype.Service;

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

    public Post createPost(Post post) {
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
