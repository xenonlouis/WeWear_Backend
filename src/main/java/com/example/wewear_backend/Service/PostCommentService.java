package com.example.wewear_backend.Service;

import com.example.wewear_backend.Model.PostComment;
import com.example.wewear_backend.Repository.PostCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCommentService {
    private final PostCommentRepository postCommentRepository;

    public PostCommentService(PostCommentRepository postCommentRepository) {
        this.postCommentRepository = postCommentRepository;
    }

    public List<PostComment> getAllComments() {
        return postCommentRepository.findAll();
    }

    public PostComment getCommentById(Integer id) {
        return postCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public PostComment createComment(PostComment comment) {
        return postCommentRepository.save(comment);
    }

    public PostComment updateComment(Integer id, PostComment commentDetails) {
        PostComment comment = getCommentById(id);
        comment.setContent(commentDetails.getContent());
        return postCommentRepository.save(comment);
    }

    public void deleteComment(Integer id) {
        postCommentRepository.deleteById(id);
    }
}
