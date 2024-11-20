package com.example.wewear_backend.Service;

import com.example.wewear_backend.Model.ClothingItem;
import com.example.wewear_backend.Repository.ClothingItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClothingItemService {
    private final ClothingItemRepository clothingItemRepository;

    public ClothingItemService(ClothingItemRepository clothingItemRepository) {
        this.clothingItemRepository = clothingItemRepository;
    }

    public List<ClothingItem> getAllClothingItems() {
        return clothingItemRepository.findAll();
    }

    public ClothingItem getClothingItemById(Integer id) {
        return clothingItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clothing Item not found"));
    }

    public ClothingItem createClothingItem(ClothingItem clothingItem) {
        return clothingItemRepository.save(clothingItem);
    }

    public ClothingItem updateClothingItem(Integer id, ClothingItem clothingItemDetails) {
        ClothingItem clothingItem = getClothingItemById(id);
        clothingItem.setName(clothingItemDetails.getName());
        clothingItem.setCategory(clothingItemDetails.getCategory());
        clothingItem.setColor(clothingItemDetails.getColor());
        clothingItem.setSize(clothingItemDetails.getSize());
        clothingItem.setMaterial(clothingItemDetails.getMaterial());
        clothingItem.setPattern(clothingItemDetails.getPattern());
        clothingItem.setSeason(clothingItemDetails.getSeason());
        clothingItem.setImageUrl(clothingItemDetails.getImageUrl());
        return clothingItemRepository.save(clothingItem);
    }

    public void deleteClothingItem(Integer id) {
        clothingItemRepository.deleteById(id);
    }
}
