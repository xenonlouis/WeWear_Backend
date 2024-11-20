package com.example.wewear_backend.Controller;


import com.example.wewear_backend.Model.FavoriteOutfit;
import com.example.wewear_backend.Service.FavoriteOutfitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorite-outfits")
public class FavoriteOutfitController {

    private final FavoriteOutfitService favoriteOutfitService;

    public FavoriteOutfitController(FavoriteOutfitService favoriteOutfitService) {
        this.favoriteOutfitService = favoriteOutfitService;
    }

    // Récupérer toutes les tenues préférées
    @GetMapping
    public ResponseEntity<List<FavoriteOutfit>> getAllFavoriteOutfits() {
        List<FavoriteOutfit> outfits = favoriteOutfitService.getAllFavoriteOutfits();
        return ResponseEntity.ok(outfits);
    }

    // Récupérer une tenue préférée par ID
    @GetMapping("/{id}")
    public ResponseEntity<FavoriteOutfit> getFavoriteOutfitById(@PathVariable Integer id) {
        FavoriteOutfit outfit = favoriteOutfitService.getFavoriteOutfitById(id);
        return ResponseEntity.ok(outfit);
    }

    // Créer une nouvelle tenue préférée
    @PostMapping
    public ResponseEntity<FavoriteOutfit> createFavoriteOutfit(@RequestBody FavoriteOutfit favoriteOutfit) {
        FavoriteOutfit createdOutfit = favoriteOutfitService.createFavoriteOutfit(favoriteOutfit);
        return ResponseEntity.ok(createdOutfit);
    }

    // Supprimer une tenue préférée
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavoriteOutfit(@PathVariable Integer id) {
        favoriteOutfitService.deleteFavoriteOutfit(id);
        return ResponseEntity.noContent().build();
    }
}
