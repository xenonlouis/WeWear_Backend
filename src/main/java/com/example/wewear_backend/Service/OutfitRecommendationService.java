package com.example.wewear_backend.Service;

import com.example.wewear_backend.Model.Outfit;
import com.example.wewear_backend.Model.Wardrobe;
import com.example.wewear_backend.Repository.OutfitRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OutfitRecommendationService {
    private final OutfitRepository outfitRepository;

    public OutfitRecommendationService(OutfitRepository outfitRepository) {
        this.outfitRepository = outfitRepository;
    }

    public List<Outfit> getRecommendedOutfits(Wardrobe wardrobe, String season, String occasion) {
        List<Outfit> allOutfits = outfitRepository.findByWardrobeId(wardrobe.getId());
        
        // Filter outfits based on season and occasion if provided
        return allOutfits.stream()
            .filter(outfit -> season == null || outfit.getSeason().equals(season))
            .filter(outfit -> occasion == null || outfit.getOccasion().equals(occasion))
            .sorted(Comparator
                .comparingDouble(Outfit::getRating).reversed()
                .thenComparingInt(Outfit::getTimesWorn).reversed())
            .limit(10)
            .collect(Collectors.toList());
    }

    public List<Outfit> getPopularOutfits(Wardrobe wardrobe) {
        List<Outfit> allOutfits = outfitRepository.findByWardrobeId(wardrobe.getId());
        
        // Return top-rated and most worn outfits
        return allOutfits.stream()
            .sorted(Comparator
                .comparingDouble(Outfit::getRating).reversed()
                .thenComparingInt(Outfit::getTimesWorn).reversed())
            .limit(5)
            .collect(Collectors.toList());
    }

    public List<Outfit> getSimilarOutfits(Outfit outfit) {
        List<Outfit> allOutfits = outfitRepository.findByWardrobeId(outfit.getWardrobe().getId());
        
        return allOutfits.stream()
            .filter(o -> o.getId() != outfit.getId())
            .filter(o -> hasSimilarAttributes(o, outfit))
            .limit(5)
            .collect(Collectors.toList());
    }

    private boolean hasSimilarAttributes(Outfit o1, Outfit o2) {
        // Check for similar season or occasion
        boolean seasonMatch = o1.getSeason() != null && o1.getSeason().equals(o2.getSeason());
        boolean occasionMatch = o1.getOccasion() != null && o1.getOccasion().equals(o2.getOccasion());
        
        // Check for common tags
        Set<String> tags1 = new HashSet<>(o1.getTags() != null ? o1.getTags() : Collections.emptyList());
        Set<String> tags2 = new HashSet<>(o2.getTags() != null ? o2.getTags() : Collections.emptyList());
        tags1.retainAll(tags2);
        boolean hasCommonTags = !tags1.isEmpty();

        return seasonMatch || occasionMatch || hasCommonTags;
    }
} 