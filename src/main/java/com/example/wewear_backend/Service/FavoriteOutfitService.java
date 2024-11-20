package com.example.wewear_backend.Service;

import com.example.wewear_backend.Model.FavoriteOutfit;
import com.example.wewear_backend.Repository.FavoriteOutfitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteOutfitService {
    private final FavoriteOutfitRepository favoriteOutfitRepository;

    public FavoriteOutfitService(FavoriteOutfitRepository favoriteOutfitRepository) {
        this.favoriteOutfitRepository = favoriteOutfitRepository;
    }

    public List<FavoriteOutfit> getAllFavoriteOutfits() {
        return favoriteOutfitRepository.findAll();
    }

    public FavoriteOutfit getFavoriteOutfitById(Integer id) {
        return favoriteOutfitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Favorite Outfit not found"));
    }

    public FavoriteOutfit createFavoriteOutfit(FavoriteOutfit favoriteOutfit) {
        return favoriteOutfitRepository.save(favoriteOutfit);
    }

    public void deleteFavoriteOutfit(Integer id) {
        favoriteOutfitRepository.deleteById(id);
    }
}
