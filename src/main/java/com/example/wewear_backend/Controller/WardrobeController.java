package com.example.wewear_backend.Controller;

import com.example.wewear_backend.Model.ClothingItem;
import com.example.wewear_backend.Model.Outfit;
import com.example.wewear_backend.Model.Wardrobe;
import com.example.wewear_backend.Repository.UserRepository;
import com.example.wewear_backend.Service.OutfitService;
import com.example.wewear_backend.Service.wardrobeService;
import com.example.wewear_backend.Service.OutfitRecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.example.wewear_backend.Model.User;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/wardrobes")
public class WardrobeController {

    private final wardrobeService wardrobeService;
    private final OutfitService outfitService;
    private final UserRepository userRepository;
    private final OutfitRecommendationService recommendationService;

    public WardrobeController(wardrobeService wardrobeService,
                              OutfitService outfitService,
                              UserRepository userRepository,
                              OutfitRecommendationService recommendationService) {
        this.wardrobeService = wardrobeService;
        this.outfitService = outfitService;
        this.userRepository = userRepository;
        this.recommendationService = recommendationService;
    }

    // Get current user's wardrobes
    @GetMapping
    public ResponseEntity<List<Wardrobe>> getAllWardrobes(Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Wardrobe> wardrobes = wardrobeService.getWardrobesByUserId(user.getId());
        return ResponseEntity.ok(wardrobes);
    }

    // Create wardrobe for current user
    @PostMapping("/me")
    public ResponseEntity<Wardrobe> createWardrobeForCurrentUser(
            @RequestBody Wardrobe wardrobe,
            Authentication authentication) {

        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        wardrobe.setUser(user);
        LocalDateTime now = LocalDateTime.now();
        wardrobe.setCreatedAt(now);
        wardrobe.setUpdatedAt(now);

        Wardrobe createdWardrobe = wardrobeService.createWardrobe(wardrobe);
        return ResponseEntity.ok(createdWardrobe);
    }

    // Get wardrobe by ID (verify ownership)
    @GetMapping("/{id}")
    public ResponseEntity<Wardrobe> getWardrobeById(
            @PathVariable Integer id,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wardrobe wardrobe = wardrobeService.getWardrobeById(id);
        if (!(wardrobe.getUser().getId() == (user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(wardrobe);
    }

    // Update wardrobe (verify ownership)
    @PutMapping("/{id}")
    public ResponseEntity<Wardrobe> updateWardrobe(
            @PathVariable Integer id,
            @RequestBody Wardrobe wardrobeDetails,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wardrobe existingWardrobe = wardrobeService.getWardrobeById(id);
        if (!(existingWardrobe.getUser().getId() == (user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        wardrobeDetails.setUpdatedAt(LocalDateTime.now());
        Wardrobe updatedWardrobe = wardrobeService.updateWardrobe(id, wardrobeDetails);
        return ResponseEntity.ok(updatedWardrobe);
    }

    // Delete wardrobe (verify ownership)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWardrobe(
            @PathVariable Integer id,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wardrobe wardrobe = wardrobeService.getWardrobeById(id);
        if (!(wardrobe.getUser().getId() == (user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        wardrobeService.deleteWardrobe(id);
        return ResponseEntity.noContent().build();
    }

    // Get clothing items for a wardrobe (verify ownership)
    @GetMapping("/{id}/clothing-items")
    public ResponseEntity<List<ClothingItem>> getClothingItemsByWardrobe(
            @PathVariable Integer id,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wardrobe wardrobe = wardrobeService.getWardrobeById(id);
        if (!(wardrobe.getUser().getId() == (user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<ClothingItem> clothingItems = wardrobeService.getClothingItemsByWardrobeId(id);
        return ResponseEntity.ok(clothingItems);
    }

    // Get specific clothing item (verify ownership)
    @GetMapping("/{wardrobeId}/clothing-items/{itemId}")
    public ResponseEntity<ClothingItem> getClothingItemById(
            @PathVariable Integer wardrobeId,
            @PathVariable Integer itemId,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wardrobe wardrobe = wardrobeService.getWardrobeById(wardrobeId);

        if (!(wardrobe.getUser().getId() == (user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ClothingItem clothingItem = wardrobeService.getClothingItemByWardrobeIdAndClothingItemId(wardrobeId, itemId);
        System.out.println(clothingItem);
        return ResponseEntity.ok(clothingItem);
    }

    @PostMapping("/{wardrobeId}/clothing-items")
    public ResponseEntity<ClothingItem> createClothingItem(
            @PathVariable Integer wardrobeId,
            @RequestBody ClothingItem clothingItem,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify wardrobe ownership
        Wardrobe wardrobe = wardrobeService.getWardrobeById(wardrobeId);
        if (!(wardrobe.getUser().getId() == (user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Set wardrobe and timestamps
        clothingItem.setWardrobe(wardrobe);
        LocalDateTime now = LocalDateTime.now();
        clothingItem.setCreatedAt(now);
        clothingItem.setUpdatedAt(now);

        ClothingItem createdItem = wardrobeService.createClothingItem(clothingItem);
        return ResponseEntity.ok(createdItem);
    }

    // Get all clothing items for current user
    @GetMapping("/me/clothing-items")
    public ResponseEntity<List<ClothingItem>> getCurrentUserClothingItems(Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ClothingItem> clothingItems = wardrobeService.getClothingItemsByUserId(user.getId());
        return ResponseEntity.ok(clothingItems);
    }

    // Get outfits
    // Get all outfits for current user
    @GetMapping("/outfits")
    public ResponseEntity<List<Outfit>> getAllOutfits(Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Outfit> outfits = outfitService.getOutfitsByUserId(user.getId());
        return ResponseEntity.ok(outfits);
    }

    // Get outfits for a specific wardrobe
    @GetMapping("/{wardrobeId}/outfits")
    public ResponseEntity<List<Outfit>> getWardrobeOutfits(
            @PathVariable Integer wardrobeId,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify wardrobe ownership
        Wardrobe wardrobe = wardrobeService.getWardrobeById(wardrobeId);
        if (!(wardrobe.getUser().getId()==(user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Outfit> outfits = outfitService.getOutfitsByWardrobeId(wardrobeId);
        return ResponseEntity.ok(outfits);
    }

    // Create outfit
    @PostMapping("/{wardrobeId}/outfits")
    public ResponseEntity<Outfit> createOutfit(
            @PathVariable Integer wardrobeId,
            @RequestBody Outfit outfit,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify wardrobe ownership
        Wardrobe wardrobe = wardrobeService.getWardrobeById(wardrobeId);
        if (!(wardrobe.getUser().getId()==(user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Set wardrobe and timestamps
        outfit.setWardrobe(wardrobe);
        LocalDateTime now = LocalDateTime.now();
        outfit.setCreatedAt(now);
        outfit.setUpdatedAt(now);

        Outfit createdOutfit = outfitService.createOutfit(outfit);
        return ResponseEntity.ok(createdOutfit);
    }

    // Update outfit
    @PutMapping("/{wardrobeId}/outfits/{outfitId}")
    public ResponseEntity<Outfit> updateOutfit(
            @PathVariable Integer wardrobeId,
            @PathVariable Integer outfitId,
            @RequestBody Outfit outfit,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify wardrobe ownership
        Wardrobe wardrobe = wardrobeService.getWardrobeById(wardrobeId);
        if (!(wardrobe.getUser().getId()==(user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Verify outfit exists and belongs to the wardrobe
        Outfit existingOutfit = outfitService.getOutfitById(outfitId);
        if (!(existingOutfit.getWardrobe().getId()==(wardrobeId))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Update outfit
        outfit.setId(outfitId);
        outfit.setWardrobe(wardrobe);
        outfit.setUpdatedAt(LocalDateTime.now());
        outfit.setCreatedAt(existingOutfit.getCreatedAt()); // Preserve creation date

        Outfit updatedOutfit = outfitService.updateOutfit(outfit);
        return ResponseEntity.ok(updatedOutfit);
    }

    // Delete outfit
    @DeleteMapping("/{wardrobeId}/outfits/{outfitId}")
    public ResponseEntity<Void> deleteOutfit(
            @PathVariable Integer wardrobeId,
            @PathVariable Integer outfitId,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify wardrobe ownership
        Wardrobe wardrobe = wardrobeService.getWardrobeById(wardrobeId);
        if (!(wardrobe.getUser().getId() == (user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Verify outfit exists and belongs to the wardrobe
        Outfit existingOutfit = outfitService.getOutfitById(outfitId);
        if (!(existingOutfit.getWardrobe().getId() == (wardrobeId))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return null;
    }

    // Get recommended outfits for a wardrobe
    @GetMapping("/{wardrobeId}/outfits/recommendations")
    public ResponseEntity<List<Outfit>> getRecommendedOutfits(
            @PathVariable Integer wardrobeId,
            @RequestParam(required = false) String season,
            @RequestParam(required = false) String occasion,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify wardrobe ownership
        Wardrobe wardrobe = wardrobeService.getWardrobeById(wardrobeId);
        if (!(wardrobe.getUser().getId() == user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Outfit> recommendations = recommendationService.getRecommendedOutfits(wardrobe, season, occasion);
        return ResponseEntity.ok(recommendations);
    }

    // Get popular outfits for a wardrobe
    @GetMapping("/{wardrobeId}/outfits/popular")
    public ResponseEntity<List<Outfit>> getPopularOutfits(
            @PathVariable Integer wardrobeId,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify wardrobe ownership
        Wardrobe wardrobe = wardrobeService.getWardrobeById(wardrobeId);
        if (!(wardrobe.getUser().getId() == user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Outfit> popularOutfits = recommendationService.getPopularOutfits(wardrobe);
        return ResponseEntity.ok(popularOutfits);
    }

    // Get similar outfits for a specific outfit in a wardrobe
    @GetMapping("/{wardrobeId}/outfits/{outfitId}/similar")
    public ResponseEntity<List<Outfit>> getSimilarOutfits(
            @PathVariable Integer wardrobeId,
            @PathVariable Integer outfitId,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify wardrobe ownership
        Wardrobe wardrobe = wardrobeService.getWardrobeById(wardrobeId);
        if (!(wardrobe.getUser().getId() == user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Outfit outfit = outfitService.getOutfitById(outfitId);
        if (!Integer.valueOf(outfit.getWardrobe().getId()).equals(wardrobeId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Outfit> similarOutfits = recommendationService.getSimilarOutfits(outfit);
        return ResponseEntity.ok(similarOutfits);
    }

    // Rate an outfit in a wardrobe
    @PostMapping("/{wardrobeId}/outfits/{outfitId}/rate")
    public ResponseEntity<Outfit> rateOutfit(
            @PathVariable Integer wardrobeId,
            @PathVariable Integer outfitId,
            @RequestParam Double rating,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify wardrobe ownership
        Wardrobe wardrobe = wardrobeService.getWardrobeById(wardrobeId);
        if (!(wardrobe.getUser().getId() == user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Outfit outfit = outfitService.getOutfitById(outfitId);
        if (!Integer.valueOf(outfit.getWardrobe().getId()).equals(wardrobeId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        outfit.setRating(rating);
        outfit.setUpdatedAt(LocalDateTime.now());
        Outfit updatedOutfit = outfitService.updateOutfit(outfit);
        return ResponseEntity.ok(updatedOutfit);
    }

    // Increment times worn for an outfit in a wardrobe
    @PostMapping("/{wardrobeId}/outfits/{outfitId}/wear")
    public ResponseEntity<Outfit> incrementOutfitWear(
            @PathVariable Integer wardrobeId,
            @PathVariable Integer outfitId,
            Authentication authentication) {
        String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify wardrobe ownership
        Wardrobe wardrobe = wardrobeService.getWardrobeById(wardrobeId);
        if (!(wardrobe.getUser().getId() == user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Outfit outfit = outfitService.getOutfitById(outfitId);
        if (!Integer.valueOf(outfit.getWardrobe().getId()).equals(wardrobeId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        outfit.setTimesWorn(outfit.getTimesWorn() + 1);
        outfit.setUpdatedAt(LocalDateTime.now());
        Outfit updatedOutfit = outfitService.updateOutfit(outfit);
        return ResponseEntity.ok(updatedOutfit);
    }
}

