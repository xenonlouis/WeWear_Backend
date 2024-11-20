package com.example.wewear_backend.Repository;

import com.example.wewear_backend.Model.ClothingItem;
import com.example.wewear_backend.Model.Wardrobe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothingItemRepository extends JpaRepository<ClothingItem, Integer> {
}