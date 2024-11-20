package com.example.wewear_backend.Repository;

import com.example.wewear_backend.Model.FavoriteOutfit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteOutfitRepository extends JpaRepository<FavoriteOutfit, Integer> {
}