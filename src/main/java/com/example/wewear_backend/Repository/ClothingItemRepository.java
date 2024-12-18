package com.example.wewear_backend.Repository;

import com.example.wewear_backend.Model.ClothingItem;
import com.example.wewear_backend.Model.Wardrobe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClothingItemRepository extends JpaRepository<ClothingItem, Integer> {
    List<ClothingItem> findByWardrobeId(Integer wardrobeId);
    Optional<ClothingItem> findByWardrobeIdAndId(Integer wardrobeId, Integer id);
}