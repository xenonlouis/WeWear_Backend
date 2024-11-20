package com.example.wewear_backend.Repository;

import com.example.wewear_backend.Model.User;
import com.example.wewear_backend.Model.Wardrobe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WardrobeRepository extends JpaRepository<Wardrobe, Integer> {
}