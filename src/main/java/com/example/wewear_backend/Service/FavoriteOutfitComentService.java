package com.example.wewear_backend.Service;

import com.example.wewear_backend.Model.FavoriteOutfitComment;
import com.example.wewear_backend.Repository.FavoriteOutfitCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteOutfitComentService {
    private final FavoriteOutfitCommentRepository favoriteOutfitCommentRepository;

    public FavoriteOutfitComentService(FavoriteOutfitCommentRepository favoriteOutfitCommentRepository) {
        this.favoriteOutfitCommentRepository = favoriteOutfitCommentRepository;
    }

    public List<FavoriteOutfitComment> getAllComments() {
        return favoriteOutfitCommentRepository.findAll();
    }

    public FavoriteOutfitComment getCommentById(Integer id) {
        return favoriteOutfitCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public FavoriteOutfitComment createComment(FavoriteOutfitComment comment) {
        return favoriteOutfitCommentRepository.save(comment);
    }

    public FavoriteOutfitComment updateComment(Integer id, FavoriteOutfitComment commentDetails) {
        FavoriteOutfitComment comment = getCommentById(id);
        comment.setContent(commentDetails.getContent());
        return favoriteOutfitCommentRepository.save(comment);
    }

    public void deleteComment(Integer id) {
        favoriteOutfitCommentRepository.deleteById(id);
    }
}
