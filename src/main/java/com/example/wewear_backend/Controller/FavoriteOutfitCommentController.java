package com.example.wewear_backend.Controller;

import com.example.wewear_backend.Model.FavoriteOutfitComment;
import com.example.wewear_backend.Service.FavoriteOutfitComentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorite-outfit-comments")
public class FavoriteOutfitCommentController {

    private final FavoriteOutfitComentService favoriteOutfitComentService;

    public FavoriteOutfitCommentController(FavoriteOutfitComentService favoriteOutfitComentService) {
        this.favoriteOutfitComentService = favoriteOutfitComentService;
    }

    // Récupérer tous les commentaires
    @GetMapping
    public ResponseEntity<List<FavoriteOutfitComment>> getAllComments() {
        List<FavoriteOutfitComment> comments = favoriteOutfitComentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    // Récupérer un commentaire par ID
    @GetMapping("/{id}")
    public ResponseEntity<FavoriteOutfitComment> getCommentById(@PathVariable Integer id) {
        FavoriteOutfitComment comment = favoriteOutfitComentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    // Créer un nouveau commentaire
    @PostMapping
    public ResponseEntity<FavoriteOutfitComment> createComment(@RequestBody FavoriteOutfitComment comment) {
        FavoriteOutfitComment createdComment = favoriteOutfitComentService.createComment(comment);
        return ResponseEntity.ok(createdComment);
    }

    // Mettre à jour un commentaire
    @PutMapping("/{id}")
    public ResponseEntity<FavoriteOutfitComment> updateComment(@PathVariable Integer id, @RequestBody FavoriteOutfitComment commentDetails) {
        FavoriteOutfitComment updatedComment = favoriteOutfitComentService.updateComment(id, commentDetails);
        return ResponseEntity.ok(updatedComment);
    }

    // Supprimer un commentaire
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        favoriteOutfitComentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
