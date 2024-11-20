package com.example.wewear_backend.Repository;

import com.example.wewear_backend.Model.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutfitRepository extends JpaRepository<Outfit, Integer> {
}
