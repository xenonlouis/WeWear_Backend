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
    private  WardrobeRepository wardrobeRepository;
    private ClothingItemRepository clothingItemRepository;

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

    public List<ClothingItem> getClothingItemsByUserId(Integer userId) {
        List<Wardrobe> wardrobes = wardrobeRepository.findByUserId(userId);
        List<ClothingItem> clothingItems = new ArrayList<>();
        for (Wardrobe wardrobe : wardrobes) {
            clothingItems.addAll(wardrobe.getClothingItems());
        }
        return clothingItems;
    }

}
