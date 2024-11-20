package com.example.wewear_backend.Controller;

import com.example.wewear_backend.Model.Wardrobe;
import com.example.wewear_backend.Service.wardrobeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wardrobes")
public class WardrobeController {

    private final wardrobeService wardrobeService;

    public WardrobeController(wardrobeService wardrobeService) {
        this.wardrobeService = wardrobeService;
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
}
