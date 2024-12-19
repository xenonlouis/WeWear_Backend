package com.example.wewear_backend.Controller;

import com.example.wewear_backend.Model.Outfit;
import com.example.wewear_backend.Service.OutfitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/outfits")
public class OutfitController {

    private final OutfitService outfitService;

    public OutfitController(OutfitService outfitService) {
        this.outfitService = outfitService;
    }

    // Récupérer toutes les tenues
    @GetMapping
    public ResponseEntity<List<Outfit>> getAllOutfits() {
        List<Outfit> outfits = outfitService.getAllOutfits();
        return ResponseEntity.ok(outfits);
    }

    // Récupérer une tenue par ID
    @GetMapping("/{id}")
    public ResponseEntity<Outfit> getOutfitById(@PathVariable Integer id) {
        Outfit outfit = outfitService.getOutfitById(id);
        return ResponseEntity.ok(outfit);
    }

    // Créer une nouvelle tenue
    @PostMapping
    public ResponseEntity<Outfit> createOutfit(@RequestBody Outfit outfit) {
        LocalDateTime now = LocalDateTime.now();
        outfit.setCreatedAt(now);
        outfit.setUpdatedAt(now);
        Outfit createdOutfit = outfitService.createOutfit(outfit);
        return ResponseEntity.ok(createdOutfit);
    }

    // Mettre à jour une tenue
    @PutMapping("/{id}")
    public ResponseEntity<Outfit> updateOutfit(@PathVariable Integer id, @RequestBody Outfit outfitDetails) {
        Outfit updatedOutfit = outfitService.updateOutfit(id, outfitDetails);
        return ResponseEntity.ok(updatedOutfit);
    }

    // Supprimer une tenue
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOutfit(@PathVariable Integer id) {
        outfitService.deleteOutfit(id);
        return ResponseEntity.noContent().build();
    }
}
