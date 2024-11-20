package com.example.wewear_backend.Controller;


import com.example.wewear_backend.Model.ClothingItem;
import com.example.wewear_backend.Service.ClothingItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clothing-items")
public class ClothingItemController {

    private final ClothingItemService clothingItemService;

    public ClothingItemController(ClothingItemService clothingItemService) {
        this.clothingItemService = clothingItemService;
    }

    // Récupérer tous les vêtements
    @GetMapping
    public ResponseEntity<List<ClothingItem>> getAllClothingItems() {
        List<ClothingItem> clothingItems = clothingItemService.getAllClothingItems();
        return ResponseEntity.ok(clothingItems);
    }

    // Récupérer un vêtement par ID
    @GetMapping("/{id}")
    public ResponseEntity<ClothingItem> getClothingItemById(@PathVariable Integer id) {
        ClothingItem clothingItem = clothingItemService.getClothingItemById(id);
        return ResponseEntity.ok(clothingItem);
    }

    // Créer un nouveau vêtement
    @PostMapping
    public ResponseEntity<ClothingItem> createClothingItem(@RequestBody ClothingItem clothingItem) {
        ClothingItem createdItem = clothingItemService.createClothingItem(clothingItem);
        return ResponseEntity.ok(createdItem);
    }

    // Mettre à jour un vêtement
    @PutMapping("/{id}")
    public ResponseEntity<ClothingItem> updateClothingItem(@PathVariable Integer id, @RequestBody ClothingItem clothingItemDetails) {
        ClothingItem updatedItem = clothingItemService.updateClothingItem(id, clothingItemDetails);
        return ResponseEntity.ok(updatedItem);
    }

    // Supprimer un vêtement
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClothingItem(@PathVariable Integer id) {
        clothingItemService.deleteClothingItem(id);
        return ResponseEntity.noContent().build();
    }
}
