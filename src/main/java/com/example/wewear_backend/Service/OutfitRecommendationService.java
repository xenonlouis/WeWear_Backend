package com.example.wewear_backend.Service;

import com.example.wewear_backend.Model.Outfit;
import com.example.wewear_backend.Model.Wardrobe;
import com.example.wewear_backend.Model.ClothingItem;
import com.example.wewear_backend.Repository.OutfitRepository;
import com.example.wewear_backend.Repository.ClothingItemRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OutfitRecommendationService {
    private final OutfitRepository outfitRepository;
    private final ClothingItemRepository clothingItemRepository;

    public OutfitRecommendationService(OutfitRepository outfitRepository, ClothingItemRepository clothingItemRepository) {
        this.outfitRepository = outfitRepository;
        this.clothingItemRepository = clothingItemRepository;
    }

    public List<Outfit> getRecommendedOutfits(Wardrobe wardrobe, String season, String occasion) {
        System.out.println("Starting outfit recommendations for wardrobe: " + wardrobe.getId() + 
            ", season: " + season + ", occasion: " + occasion);

        // Verify wardrobe exists and has items
        if (wardrobe == null) {
            System.out.println("Error: Invalid wardrobe (null)");
            return new ArrayList<>();
        }
        Integer wardrobeId = wardrobe.getId();
        if (wardrobeId == null || wardrobeId <= 0L) {
            System.out.println("Error: Invalid wardrobe ID");
            return new ArrayList<>();
        }

        List<ClothingItem> allItems = clothingItemRepository.findByWardrobeId(wardrobe.getId());
        System.out.println("Found " + allItems.size() + " total items in wardrobe");
        
        if (allItems.isEmpty()) {
            System.out.println("Error: No items found in wardrobe");
            return new ArrayList<>();
        }

        // Filter items by season and occasion if provided
        List<ClothingItem> filteredItems = allItems.stream()
            .filter(item -> season == null || item.getSeason().equals(season))
            .filter(item -> occasion == null || item.getOccasion().equals(occasion))
            .collect(Collectors.toList());
        System.out.println("After filtering by season and occasion: " + filteredItems.size() + " items");

        if (filteredItems.isEmpty()) {
            System.out.println("Error: No items match the season/occasion criteria");
            return new ArrayList<>();
        }

        // Group items by category and verify each item exists in the database
        Map<String, List<ClothingItem>> itemsByCategory = filteredItems.stream()
            .filter(item -> clothingItemRepository.existsById(item.getId()))
            .collect(Collectors.groupingBy(ClothingItem::getCategory));
        System.out.println("Categories found: " + itemsByCategory.keySet());

        // Get items by category
        List<ClothingItem> tops = itemsByCategory.getOrDefault("Top", new ArrayList<>());
        List<ClothingItem> bottoms = itemsByCategory.getOrDefault("Bottom", new ArrayList<>());
        List<ClothingItem> dresses = itemsByCategory.getOrDefault("Dress", new ArrayList<>());
        List<ClothingItem> shoes = itemsByCategory.getOrDefault("Shoes", new ArrayList<>());
        List<ClothingItem> accessories = itemsByCategory.getOrDefault("Accessories", new ArrayList<>());

        System.out.println("Items by category:");
        System.out.println("- Tops: " + tops.size() + " items");
        System.out.println("- Bottoms: " + bottoms.size() + " items");
        System.out.println("- Dresses: " + dresses.size() + " items");
        System.out.println("- Shoes: " + shoes.size() + " items");
        System.out.println("- Accessories: " + accessories.size() + " items");

        // Verify we have enough items to create outfits
        if (dresses.isEmpty() && (tops.isEmpty() || bottoms.isEmpty())) {
            System.out.println("Error: Not enough items to create outfits");
            return new ArrayList<>();
        }

        // Generate outfit combinations
        Random random = new Random();
        Set<String> usedCombinations = new HashSet<>();
        List<Outfit> recommendations = new ArrayList<>();

        System.out.println("Starting to generate outfit combinations...");

        // Try to generate up to 5 unique outfits
        int attempts = 0;
        while (recommendations.size() < 5 && attempts < 50) {
            attempts++;
            System.out.println("\nAttempt " + attempts + " to create outfit");
            
            try {
                Outfit outfit = new Outfit();
                outfit.setWardrobe(wardrobe);
                outfit.setSeason(season != null ? season : "All");
                outfit.setOccasion(occasion != null ? occasion : "Casual");
                outfit.setRating(0.0);
                outfit.setTimesWorn(0);

                // Either use a dress or top+bottom combination
                boolean useDress = !dresses.isEmpty() && (tops.isEmpty() || bottoms.isEmpty() || random.nextBoolean());
                System.out.println("Trying to create a " + (useDress ? "dress-based" : "top+bottom") + " outfit");

                if (useDress && !dresses.isEmpty()) {
                    // Dress-based outfit
                    ClothingItem dress = dresses.get(random.nextInt(dresses.size()));
                    if (!clothingItemRepository.existsById(dress.getId())) {
                        System.out.println("Error: Selected dress no longer exists");
                        continue;
                    }
                    outfit.setDress(dress);
                    outfit.setName("Outfit with " + dress.getName());
                    outfit.setDescription("A stylish outfit centered around " + dress.getName());
                    System.out.println("Selected dress: " + dress.getName());
                } else if (!tops.isEmpty() && !bottoms.isEmpty()) {
                    // Top + Bottom combination
                    ClothingItem top = tops.get(random.nextInt(tops.size()));
                    ClothingItem bottom = bottoms.get(random.nextInt(bottoms.size()));
                    
                    if (!clothingItemRepository.existsById(top.getId()) || !clothingItemRepository.existsById(bottom.getId())) {
                        System.out.println("Error: Selected top or bottom no longer exists");
                        continue;
                    }
                    
                    outfit.setTop(top);
                    outfit.setBottom(bottom);
                    outfit.setName("Outfit with " + top.getName() + " and " + bottom.getName());
                    outfit.setDescription("A coordinated look combining " + top.getName() + " with " + bottom.getName());
                    System.out.println("Selected top: " + top.getName() + ", bottom: " + bottom.getName());
                } else {
                    System.out.println("Skipping - not enough items for either dress or top+bottom combination");
                    continue;
                }

                // Add shoes if available
                if (!shoes.isEmpty()) {
                    ClothingItem selectedShoes = shoes.get(random.nextInt(shoes.size()));
                    if (clothingItemRepository.existsById(selectedShoes.getId())) {
                        outfit.setShoes(selectedShoes);
                        System.out.println("Added shoes: " + selectedShoes.getName());
                    }
                }

                // Add 1-2 accessories if available
                if (!accessories.isEmpty()) {
                    int numAccessories = random.nextInt(2) + 1;
                    List<ClothingItem> selectedAccessories = new ArrayList<>();
                    for (int i = 0; i < numAccessories && i < accessories.size(); i++) {
                        ClothingItem accessory = accessories.get(random.nextInt(accessories.size()));
                        if (clothingItemRepository.existsById(accessory.getId())) {
                            selectedAccessories.add(accessory);
                            System.out.println("Added accessory: " + accessory.getName());
                        }
                    }
                    if (!selectedAccessories.isEmpty()) {
                        outfit.setAccessories(selectedAccessories);
                    }
                }

                // Generate a unique key for this combination
                String combinationKey = generateOutfitKey(outfit);
                System.out.println("Generated combination key: " + combinationKey);
                
                if (!usedCombinations.contains(combinationKey)) {
                    usedCombinations.add(combinationKey);
                    recommendations.add(outfit);
                    System.out.println("Added new outfit suggestion to recommendations");
                } else {
                    System.out.println("Skipping duplicate outfit combination");
                }
            } catch (Exception e) {
                System.out.println("Error creating outfit: " + e.getMessage());
                continue;
            }
        }

        System.out.println("\nFinished generating recommendations:");
        System.out.println("- Generated " + recommendations.size() + " unique outfits");
        System.out.println("- Made " + attempts + " attempts");
        System.out.println("- Used " + usedCombinations.size() + " unique combinations");

        return recommendations;
    }

    private String generateOutfitKey(Outfit outfit) {
        StringBuilder key = new StringBuilder();
        if (outfit.getDress() != null) key.append("D").append(outfit.getDress().getId());
        if (outfit.getTop() != null) key.append("T").append(outfit.getTop().getId());
        if (outfit.getBottom() != null) key.append("B").append(outfit.getBottom().getId());
        if (outfit.getShoes() != null) key.append("S").append(outfit.getShoes().getId());
        if (outfit.getAccessories() != null) {
            outfit.getAccessories().forEach(a -> key.append("A").append(a.getId()));
        }
        return key.toString();
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
        
        // Check for common clothing items
        boolean hasCommonTop = o1.getTop() != null && o2.getTop() != null && 
            o1.getTop().getId() == o2.getTop().getId();
        boolean hasCommonBottom = o1.getBottom() != null && o2.getBottom() != null && 
            o1.getBottom().getId() == o2.getBottom().getId();
        boolean hasCommonDress = o1.getDress() != null && o2.getDress() != null && 
            o1.getDress().getId() == o2.getDress().getId();
        
        return seasonMatch || occasionMatch || hasCommonTop || hasCommonBottom || hasCommonDress;
    }
} 