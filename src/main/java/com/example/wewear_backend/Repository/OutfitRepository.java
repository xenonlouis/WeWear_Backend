package com.example.wewear_backend.Repository;

import com.example.wewear_backend.Model.Outfit;
import com.example.wewear_backend.Model.Wardrobe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutfitRepository extends JpaRepository<Outfit, Integer> {

    List<Outfit> findByWardrobeId(Integer userId);

}
