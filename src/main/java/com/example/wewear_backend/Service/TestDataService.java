package com.example.wewear_backend.Service;

import com.example.wewear_backend.Model.*;
import com.example.wewear_backend.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TestDataService {
    private final WardrobeRepository wardrobeRepository;
    private final ClothingItemRepository clothingItemRepository;
    private final OutfitRepository outfitRepository;
    private final UserRepository userRepository;

    public TestDataService(
            WardrobeRepository wardrobeRepository,
            ClothingItemRepository clothingItemRepository,
            OutfitRepository outfitRepository,
            UserRepository userRepository) {
        this.wardrobeRepository = wardrobeRepository;
        this.clothingItemRepository = clothingItemRepository;
        this.outfitRepository = outfitRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void injectTestData(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if test wardrobe already exists
        Optional<Wardrobe> existingWardrobeOpt = wardrobeRepository.findByUserIdAndName(userId, "Test Wardrobe");
        if (existingWardrobeOpt.isPresent()) {
            // If wardrobe exists, check if it already has all test items
            Wardrobe existingWardrobe = existingWardrobeOpt.get();
            List<ClothingItem> existingItems = clothingItemRepository.findByWardrobeId(existingWardrobe.getId());
            if (!existingItems.isEmpty()) {
                // If items exist, check if all test outfits are present
                List<Outfit> existingOutfits = outfitRepository.findByWardrobeId(existingWardrobe.getId());
                if (!existingOutfits.isEmpty()) {
                    return; // Test data already exists completely
                }
            }
            // Use existing wardrobe but continue with missing items/outfits
            createTestData(existingWardrobe);
        } else {
            // Create new wardrobe and all test data
            Wardrobe wardrobe = new Wardrobe();
            wardrobe.setName("Test Wardrobe");
            wardrobe.setUser(user);
            wardrobe.setCreatedAt(LocalDateTime.now());
            wardrobe.setUpdatedAt(LocalDateTime.now());
            wardrobe = wardrobeRepository.save(wardrobe);
            createTestData(wardrobe);
        }
    }

    private void createTestData(Wardrobe wardrobe) {
        Map<String, ClothingItem> items = new HashMap<>();

        // Tops
        items.put("whiteTee", findOrCreateClothingItem("White T-Shirt", "Top", wardrobe,
            item -> {
                item.setOccasion("Casual");
                item.setSize("M");
                item.setMaterial("Cotton");
                item.setSeason("Summer");
                item.setImageUrl("https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?ixlib=rb-4.0.3");
                item.setBrand("Basic Co");
                item.setColors(Arrays.asList("White"));
                item.setPatterns(Collections.emptyList());
            }));
        
        items.put("navyShirt", findOrCreateClothingItem("Navy Business Shirt", "Top", wardrobe,
            item -> {
                item.setOccasion("Business");
                item.setSize("M");
                item.setMaterial("Cotton");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?ixlib=rb-4.0.3");
                item.setBrand("Business Basics");
                item.setColors(Arrays.asList("Navy"));
                item.setPatterns(Collections.emptyList());
            }));

        // Bottoms
        items.put("jeans", findOrCreateClothingItem("Classic Navy Jeans", "Bottom", wardrobe,
            item -> {
                item.setOccasion("Casual");
                item.setSize("32");
                item.setMaterial("Denim");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1542272604-787c3835535d?ixlib=rb-4.0.3");
                item.setBrand("Denim Co");
                item.setColors(Arrays.asList("Navy"));
                item.setPatterns(Collections.emptyList());
            }));
        
        items.put("dressSlacks", findOrCreateClothingItem("Classic Black Business Pants", "Bottom", wardrobe,
            item -> {
                item.setOccasion("Business");
                item.setSize("32");
                item.setMaterial("Wool Blend");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1594938298603-c8148c4dae35?ixlib=rb-4.0.3");
                item.setBrand("Business Basics");
                item.setColors(Arrays.asList("Black"));
                item.setPatterns(Collections.emptyList());
            }));

        // Skirt
        items.put("pleatedSkirt", findOrCreateClothingItem("Navy Business Pleated Skirt", "Bottom", wardrobe,
            item -> {
                item.setOccasion("Business");
                item.setSize("M");
                item.setMaterial("Polyester");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?ixlib=rb-4.0.3");
                item.setBrand("Business Basics");
                item.setColors(Arrays.asList("Navy"));
                item.setPatterns(Collections.emptyList());
            }));

        // Dress
        items.put("summerDress", findOrCreateClothingItem("Blue and White Floral Summer Dress", "Dress", wardrobe,
            item -> {
                item.setOccasion("Casual");
                item.setSize("M");
                item.setMaterial("Cotton");
                item.setSeason("Summer");
                item.setImageUrl("https://images.unsplash.com/photo-1595777457583-95e059d581b8?ixlib=rb-4.0.3");
                item.setBrand("Summer Style");
                item.setColors(Arrays.asList("Blue", "White"));
                item.setPatterns(Arrays.asList("Floral"));
            }));

        // Outerwear
        items.put("blazer", findOrCreateClothingItem("Classic Black Business Blazer", "Top", wardrobe,
            item -> {
                item.setOccasion("Business");
                item.setSize("M");
                item.setMaterial("Polyester Blend");
                item.setSeason("Spring");
                item.setImageUrl("https://images.unsplash.com/photo-1591047139829-d91aecb6caea?ixlib=rb-4.0.3");
                item.setBrand("Business Basics");
                item.setColors(Arrays.asList("Black"));
                item.setPatterns(Collections.emptyList());
            }));

        // Shoes
        items.put("casualSneakers1", findOrCreateClothingItem("White Casual Sport Sneakers 1", "Shoes", wardrobe,
            item -> {
                item.setOccasion("Casual");
                item.setSize("42");
                item.setMaterial("Canvas");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1597248881519-db089d3744a5?ixlib=rb-4.0.3");
                item.setBrand("Comfort Walk");
                item.setColors(Arrays.asList("White"));
                item.setPatterns(Collections.emptyList());
            }));
        
        items.put("oxfords", findOrCreateClothingItem("Black Business Oxford Shoes", "Shoes", wardrobe,
            item -> {
                item.setOccasion("Business");
                item.setSize("42");
                item.setMaterial("Leather");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1614252235316-8c857d38b5f4?ixlib=rb-4.0.3");
                item.setBrand("Business Basics");
                item.setColors(Arrays.asList("Black"));
                item.setPatterns(Collections.emptyList());
            }));

        items.put("casualSneakers2", findOrCreateClothingItem("White Casual Sport Sneakers 2", "Shoes", wardrobe,
            item -> {
                item.setOccasion("Casual");
                item.setSize("42");
                item.setMaterial("Canvas");
                item.setSeason("Summer");
                item.setImageUrl("https://images.unsplash.com/photo-1600269452121-4f2416e55c28?ixlib=rb-4.0.3");
                item.setBrand("Comfort Walk");
                item.setColors(Arrays.asList("White"));
                item.setPatterns(Collections.emptyList());
            }));

        // Bags
        items.put("tote", findOrCreateClothingItem("Large Casual Beige Tote Bag", "Accessories", wardrobe,
            item -> {
                item.setOccasion("Casual");
                item.setSize("One Size");
                item.setMaterial("Canvas");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1590874103328-eac38a683ce7?ixlib=rb-4.0.3");
                item.setBrand("Bag Co");
                item.setColors(Arrays.asList("Beige"));
                item.setPatterns(Collections.emptyList());
            }));

        // Hats
        items.put("sunHat", findOrCreateClothingItem("Beige Summer Sun Hat", "Accessories", wardrobe,
            item -> {
                item.setOccasion("Casual");
                item.setSize("One Size");
                item.setMaterial("Straw");
                item.setSeason("Summer");
                item.setImageUrl("https://images.unsplash.com/photo-1572307480813-ceb0e59d8325?ixlib=rb-4.0.3");
                item.setBrand("Summer Style");
                item.setColors(Arrays.asList("Beige"));
                item.setPatterns(Collections.emptyList());
            }));

        // Accessories
        items.put("belt", findOrCreateClothingItem("Black Leather Business Belt", "Accessories", wardrobe,
            item -> {
                item.setOccasion("All");
                item.setSize("32");
                item.setMaterial("Leather");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1624222247344-550fb60583dc?ixlib=rb-4.0.3");
                item.setBrand("Accessories Co");
                item.setColors(Arrays.asList("Black"));
                item.setPatterns(Collections.emptyList());
            }));
        
        items.put("watch", findOrCreateClothingItem("Classic Silver Watch", "Accessories", wardrobe,
            item -> {
                item.setOccasion("All");
                item.setSize("One Size");
                item.setMaterial("Stainless Steel");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1587836374828-4dbafa94cf0e?ixlib=rb-4.0.3");
                item.setBrand("Time Style");
                item.setColors(Arrays.asList("Silver"));
                item.setPatterns(Collections.emptyList());
            }));

        // Create outfits with ratings and times worn
        // Casual Summer Outfit
        findOrCreateOutfit("Casual Summer Day", wardrobe,
            outfit -> {
                outfit.setDescription("Perfect for a sunny casual day");
                outfit.setSeason("Summer");
                outfit.setOccasion("Casual");
                outfit.setTop(items.get("whiteTee"));
                outfit.setBottom(items.get("jeans"));
                outfit.setShoes(items.get("casualSneakers1"));
                outfit.setAccessories(Arrays.asList(items.get("belt"), items.get("sunHat")));
                outfit.setRating(4.5);
                outfit.setTimesWorn(5);
            });

        // Business Outfit
        findOrCreateOutfit("Classic Business Look", wardrobe,
            outfit -> {
                outfit.setDescription("Professional attire for important meetings");
                outfit.setSeason("All");
                outfit.setOccasion("Business");
                outfit.setTop(items.get("navyShirt"));
                outfit.setBottom(items.get("dressSlacks"));
                outfit.setOuterwear(items.get("blazer"));
                outfit.setShoes(items.get("oxfords"));
                outfit.setAccessories(Arrays.asList(items.get("belt"), items.get("watch")));
                outfit.setRating(4.8);
                outfit.setTimesWorn(8);
            });

        // Summer Dress Outfit
        findOrCreateOutfit("Elegant Summer Dress", wardrobe,
            outfit -> {
                outfit.setDescription("Light and sophisticated summer ensemble");
                outfit.setSeason("Summer");
                outfit.setOccasion("Casual");
                outfit.setDress(items.get("summerDress"));
                outfit.setShoes(items.get("casualSneakers2"));
                outfit.setAccessories(Arrays.asList(items.get("watch"), items.get("sunHat"), items.get("tote")));
                outfit.setRating(4.2);
                outfit.setTimesWorn(3);
            });
    }

    private ClothingItem findOrCreateClothingItem(String name, String category, Wardrobe wardrobe, 
            java.util.function.Consumer<ClothingItem> itemInitializer) {
        return clothingItemRepository.findByWardrobeIdAndNameAndCategory(wardrobe.getId(), name, category)
            .orElseGet(() -> {
                ClothingItem item = new ClothingItem();
                item.setName(name);
                item.setCategory(category);
                item.setWardrobe(wardrobe);
                item.setCreatedAt(LocalDateTime.now());
                item.setUpdatedAt(LocalDateTime.now());
                itemInitializer.accept(item);
                return clothingItemRepository.save(item);
            });
    }

    private Outfit findOrCreateOutfit(String name, Wardrobe wardrobe,
            java.util.function.Consumer<Outfit> outfitInitializer) {
        return outfitRepository.findByWardrobeIdAndName(wardrobe.getId(), name)
            .orElseGet(() -> {
                Outfit outfit = new Outfit();
                outfit.setName(name);
                outfit.setWardrobe(wardrobe);
                outfit.setCreatedAt(LocalDateTime.now());
                outfit.setUpdatedAt(LocalDateTime.now());
                outfit.setRating(0.0);
                outfit.setTimesWorn(0);
                outfitInitializer.accept(outfit);
                return outfitRepository.save(outfit);
            });
    }
} 