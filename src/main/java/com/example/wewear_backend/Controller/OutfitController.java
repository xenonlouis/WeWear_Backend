package com.example.wewear_backend.Controller;

import com.example.wewear_backend.Model.Outfit;
import com.example.wewear_backend.Model.User;
import com.example.wewear_backend.Model.Wardrobe;
import com.example.wewear_backend.Service.OutfitService;
import com.example.wewear_backend.Service.OutfitRecommendationService;
import com.example.wewear_backend.Service.wardrobeService;
import com.example.wewear_backend.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/outfits")
public class OutfitController {

    private final OutfitService outfitService;
    private final OutfitRecommendationService recommendationService;
    private final UserRepository userRepository;
    private final wardrobeService wardrobeService;

    public OutfitController(
            OutfitService outfitService,
            OutfitRecommendationService recommendationService,
            UserRepository userRepository,
            wardrobeService wardrobeService) {
        this.outfitService = outfitService;
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
        this.wardrobeService = wardrobeService;
    }

    // Get all outfits
    @GetMapping
    public ResponseEntity<List<Outfit>> getAllOutfits() {
        List<Outfit> outfits = outfitService.getAllOutfits();
        return ResponseEntity.ok(outfits);
    }

    // Get outfit by ID
    @GetMapping("/{id}")
    public ResponseEntity<Outfit> getOutfitById(@PathVariable Integer id) {
        Outfit outfit = outfitService.getOutfitById(id);
        return ResponseEntity.ok(outfit);
    }

    // Create new outfit
    @PostMapping
    public ResponseEntity<Outfit> createOutfit(@RequestBody Outfit outfit) {
        LocalDateTime now = LocalDateTime.now();
        outfit.setCreatedAt(now);
        outfit.setUpdatedAt(now);
        outfit.setRating(0.0);  // Initialize rating
        outfit.setTimesWorn(0); // Initialize times worn
        Outfit createdOutfit = outfitService.createOutfit(outfit);
        return ResponseEntity.ok(createdOutfit);
    }

    // Update outfit
    @PutMapping("/{id}")
    public ResponseEntity<Outfit> updateOutfit(@PathVariable Integer id, @RequestBody Outfit outfitDetails) {
        outfitDetails.setId(id);
        outfitDetails.setUpdatedAt(LocalDateTime.now());
        Outfit updatedOutfit = outfitService.updateOutfit(outfitDetails);
        return ResponseEntity.ok(updatedOutfit);
    }

    // Delete outfit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOutfit(@PathVariable Integer id) {
        outfitService.deleteOutfit(id);
        return ResponseEntity.noContent().build();
    }

    // Get recommended outfits
    @GetMapping("/recommendations")
    public ResponseEntity<List<Outfit>> getRecommendedOutfits(
            Authentication authentication,
            @RequestParam(required = false) String season,
            @RequestParam(required = false) String occasion) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Wardrobe> wardrobes = wardrobeService.getWardrobesByUserId(user.getId());
        if (wardrobes.isEmpty()) {
            throw new RuntimeException("User has no wardrobes");
        }
        
        // Use the first wardrobe for now
        Wardrobe wardrobe = wardrobes.get(0);
        List<Outfit> recommendations = recommendationService.getRecommendedOutfits(wardrobe, season, occasion);
        return ResponseEntity.ok(recommendations);
    }

    // Get popular outfits
    @GetMapping("/popular")
    public ResponseEntity<List<Outfit>> getPopularOutfits(Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Wardrobe> wardrobes = wardrobeService.getWardrobesByUserId(user.getId());
        if (wardrobes.isEmpty()) {
            throw new RuntimeException("User has no wardrobes");
        }
        
        // Use the first wardrobe for now
        Wardrobe wardrobe = wardrobes.get(0);
        List<Outfit> popularOutfits = recommendationService.getPopularOutfits(wardrobe);
        return ResponseEntity.ok(popularOutfits);
    }

    // Get similar outfits
    @GetMapping("/{id}/similar")
    public ResponseEntity<List<Outfit>> getSimilarOutfits(@PathVariable Integer id) {
        Outfit outfit = outfitService.getOutfitById(id);
        List<Outfit> similarOutfits = recommendationService.getSimilarOutfits(outfit);
        return ResponseEntity.ok(similarOutfits);
    }

    // Update outfit rating
    @PostMapping("/{id}/rate")
    public ResponseEntity<Outfit> rateOutfit(
            @PathVariable Integer id,
            @RequestParam Double rating) {
        Outfit outfit = outfitService.getOutfitById(id);
        outfit.setRating(rating);
        outfit.setUpdatedAt(LocalDateTime.now());
        Outfit updatedOutfit = outfitService.updateOutfit(outfit);
        return ResponseEntity.ok(updatedOutfit);
    }

    // Increment times worn
    @PostMapping("/{id}/wear")
    public ResponseEntity<Outfit> incrementTimesWorn(@PathVariable Integer id) {
        Outfit outfit = outfitService.getOutfitById(id);
        outfit.setTimesWorn(outfit.getTimesWorn() + 1);
        outfit.setUpdatedAt(LocalDateTime.now());
        Outfit updatedOutfit = outfitService.updateOutfit(outfit);
        return ResponseEntity.ok(updatedOutfit);
    }
}
