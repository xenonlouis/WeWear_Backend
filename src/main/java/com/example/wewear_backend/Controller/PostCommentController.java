package com.example.wewear_backend.Controller;

import com.example.wewear_backend.Model.PostComment;
import com.example.wewear_backend.Service.PostCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post-comments")
public class PostCommentController {

    private final PostCommentService postCommentService;

    public PostCommentController(PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    // Récupérer tous les commentaires
    @GetMapping
    public ResponseEntity<List<PostComment>> getAllComments() {
        List<PostComment> comments = postCommentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    // Récupérer un commentaire par ID
    @GetMapping("/{id}")
    public ResponseEntity<PostComment> getCommentById(@PathVariable Integer id) {
        PostComment comment = postCommentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    // Créer un nouveau commentaire
    @PostMapping
    public ResponseEntity<PostComment> createComment(@RequestBody PostComment comment) {
        PostComment createdComment = postCommentService.createComment(comment);
        return ResponseEntity.ok(createdComment);
    }

    // Mettre à jour un commentaire
    @PutMapping("/{id}")
    public ResponseEntity<PostComment> updateComment(@PathVariable Integer id, @RequestBody PostComment commentDetails) {
        PostComment updatedComment = postCommentService.updateComment(id, commentDetails);
        return ResponseEntity.ok(updatedComment);
    }

    // Supprimer un commentaire
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        postCommentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}

