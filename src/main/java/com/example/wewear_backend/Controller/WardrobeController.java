package com.example.wewear_backend.Controller;

import com.example.wewear_backend.Model.ClothingItem;
import com.example.wewear_backend.Model.Outfit;
import com.example.wewear_backend.Model.Wardrobe;
import com.example.wewear_backend.Service.OutfitService;
import com.example.wewear_backend.Service.wardrobeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/wardrobes")
public class WardrobeController {

    private final wardrobeService wardrobeService;
    private final OutfitService outfitService;

    public WardrobeController(wardrobeService wardrobeService, OutfitService outfitService) {
        this.wardrobeService = wardrobeService;
        this.outfitService = outfitService;
    }

    // Récupérer toutes les garde-robes
    @GetMapping
    public ResponseEntity<List<Wardrobe>> getAllWardrobes() {
        List<Wardrobe> wardrobes = wardrobeService.getAllWardrobes();
        return ResponseEntity.ok(wardrobes);
    }


    // Récupérer une garde-robe par ID
    @GetMapping("/{id}")
    public ResponseEntity<Wardrobe> getWardrobeById(@PathVariable Integer id) {
        Wardrobe wardrobe = wardrobeService.getWardrobeById(id);
        return ResponseEntity.ok(wardrobe);
    }

    // Créer une nouvelle garde-robe
    @PostMapping
    public ResponseEntity<Wardrobe> createWardrobe(@RequestBody Wardrobe wardrobe) {
        Wardrobe createdWardrobe = wardrobeService.createWardrobe(wardrobe);
        return ResponseEntity.ok(createdWardrobe);
    }

    // Mettre à jour une garde-robe
    @PutMapping("/{id}")
    public ResponseEntity<Wardrobe> updateWardrobe(@PathVariable Integer id, @RequestBody Wardrobe wardrobeDetails) {
        Wardrobe updatedWardrobe = wardrobeService.updateWardrobe(id, wardrobeDetails);
        return ResponseEntity.ok(updatedWardrobe);
    }

    // Supprimer une garde-robe
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWardrobe(@PathVariable Integer id) {
        wardrobeService.deleteWardrobe(id);
        return ResponseEntity.noContent().build();
    }

    //!!Ajouter pour le Wardrobe : recuperer touts les vetements d'une garde-robe par ID
    @GetMapping("/{id}/clothing-items")
    public ResponseEntity<List<ClothingItem>> getClothingItemsByWardrobe(@PathVariable Integer id) {
        List<ClothingItem> clothingItems = wardrobeService.getClothingItemsByWardrobeId(id);
        return ResponseEntity.ok(clothingItems);
    }
    @GetMapping("/{wardrobeId}/clothing-items/{itemId}")
    public ResponseEntity<ClothingItem> getClothingItemById(
            @PathVariable Integer wardrobeId,
            @PathVariable Integer itemId
    ) {
        ClothingItem clothingItem = wardrobeService.getClothingItemByWardrobeIdAndClothingItemId(wardrobeId,itemId);
        return ResponseEntity.ok(clothingItem);
    }



    //Pour aprés si on veux passer vers wardrobe a partir des Users!!!!
    @GetMapping("/user/{userId}/clothing-items")
    public ResponseEntity<List<ClothingItem>> getClothingItemsByUserId(@PathVariable Integer userId) {
        List<ClothingItem> clothingItems = wardrobeService.getClothingItemsByUserId(userId);
        return ResponseEntity.ok(clothingItems);
    }


    //pour tester les endpoints de outfit qui ne marche pas dans le controlleur OUtfit Controller
    @GetMapping("/outfits")
    public ResponseEntity<List<Outfit>> getAllOutfits() {
        List<Outfit> outfits = outfitService.getAllOutfits();
        return ResponseEntity.ok(outfits);
    }

    @PostMapping("/outfits")
    public ResponseEntity<Outfit> createOutfit(@RequestBody Outfit outfit) {
        LocalDateTime now = LocalDateTime.now();
        outfit.setCreatedAt(now);
        outfit.setUpdatedAt(now);
        Outfit createdOutfit = outfitService.createOutfit(outfit);
        return ResponseEntity.ok(createdOutfit);
    }

}
