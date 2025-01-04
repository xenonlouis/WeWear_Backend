package com.example.wewear_backend.Service;

import com.example.wewear_backend.Model.ClothingItem;
import com.example.wewear_backend.Model.Wardrobe;
import com.example.wewear_backend.Repository.ClothingItemRepository;
import com.example.wewear_backend.Repository.WardrobeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class wardrobeService {
    private final  WardrobeRepository wardrobeRepository;
    private final  ClothingItemRepository clothingItemRepository;

    public wardrobeService(WardrobeRepository wardrobeRepository,ClothingItemRepository clothingItemRepository) {
        this.wardrobeRepository = wardrobeRepository;
        this.clothingItemRepository = clothingItemRepository;
    }

    public List<Wardrobe> getAllWardrobes() {
        return wardrobeRepository.findAll();
    }

    public Wardrobe getWardrobeById(Integer id) {
        return wardrobeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wardrobe not found"));
    }

    public Wardrobe createWardrobe(Wardrobe wardrobe) {
        return wardrobeRepository.save(wardrobe);
    }

    public Wardrobe updateWardrobe(Integer id, Wardrobe wardrobeDetails) {
        Wardrobe wardrobe = getWardrobeById(id);
        wardrobe.setName(wardrobeDetails.getName());
        return wardrobeRepository.save(wardrobe);
    }

    public void deleteWardrobe(Integer id) {
        wardrobeRepository.deleteById(id);
    }


    //!! Ajouter pour le  Wardrobe

    public List<ClothingItem> getClothingItemsByWardrobeId(Integer wardrobeId) {
        return clothingItemRepository.findByWardrobeId(wardrobeId);
    }

    public ClothingItem getClothingItemByWardrobeIdAndClothingItemId(Integer wardrobeId, Integer clothingItemId) {
        return clothingItemRepository.findByWardrobeIdAndId(wardrobeId, clothingItemId)
                .orElseThrow(() -> new RuntimeException("Clothing item not found"));
    }


    public List<Wardrobe> getWardrobesByUserId(Integer userId) {
        return wardrobeRepository.findByUserId(userId);
    }

    public List<ClothingItem> getClothingItemsByUserId(Integer userId) {
        List<Wardrobe> userWardrobes = wardrobeRepository.findByUserId(userId);
        if (userWardrobes == null || userWardrobes.isEmpty()) {
            System.out.println("No wardrobes found for user: " + userId);
            return new ArrayList<>();
        }

        List<ClothingItem> allItems = new ArrayList<>();
        for (Wardrobe wardrobe : userWardrobes) {
            List<ClothingItem> wardrobeItems = clothingItemRepository.findByWardrobeId(wardrobe.getId());
            if (wardrobeItems != null) {
                allItems.addAll(wardrobeItems);
            }
        }

        return allItems;
    }

    public ClothingItem createClothingItem(ClothingItem clothingItem) {
        // Validate that the wardrobe exists and is set
        if (clothingItem.getWardrobe() == null ) {
            throw new RuntimeException("Wardrobe must be set for clothing item");
        }

        // Ensure the wardrobe exists
        Wardrobe wardrobe = wardrobeRepository.findById(clothingItem.getWardrobe().getId())
                .orElseThrow(() -> new RuntimeException("Wardrobe not found"));

        // Set the wardrobe reference
        clothingItem.setWardrobe(wardrobe);

        // Save the clothing item
        return clothingItemRepository.save(clothingItem);
    }
}
