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
        items.put("whiteTee", findOrCreateClothingItem("T-Shirt Basique en Coton Blanc", "Tops", wardrobe,
            item -> {
                item.setOccasion("Casual");
                item.setSize("M");
                item.setMaterial("Cotton");
                item.setSeason("Summer");
                item.setImageUrl("https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?ixlib=rb-4.0.3");
                item.setBrand("Basic Co");
                item.setColors(Arrays.asList("Blanc"));
                item.setPatterns(Collections.emptyList());
            }));
        
        items.put("navyShirt", findOrCreateClothingItem("Chemise Business Bleu Marine", "Tops", wardrobe,
            item -> {
                item.setOccasion("Business");
                item.setSize("M");
                item.setMaterial("Cotton");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?ixlib=rb-4.0.3");
                item.setBrand("Business Basics");
                item.setColors(Arrays.asList("Marine"));
                item.setPatterns(Collections.emptyList());
            }));

        // Bottoms
        items.put("jeans", findOrCreateClothingItem("Jean Classique Bleu Marine", "Pantalons", wardrobe,
            item -> {
                item.setOccasion("Casual");
                item.setSize("32");
                item.setMaterial("Denim");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1542272604-787c3835535d?ixlib=rb-4.0.3");
                item.setBrand("Denim Co");
                item.setColors(Arrays.asList("Bleu Marine"));
                item.setPatterns(Collections.emptyList());
            }));
        
        items.put("dressSlacks", findOrCreateClothingItem("Pantalon Business Noir Classique", "Pantalons", wardrobe,
            item -> {
                item.setOccasion("Business");
                item.setSize("32");
                item.setMaterial("Wool Blend");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1594938298603-c8148c4dae35?ixlib=rb-4.0.3");
                item.setBrand("Business Basics");
                item.setColors(Arrays.asList("Noir"));
                item.setPatterns(Collections.emptyList());
            }));

        // Skirt
        items.put("pleatedSkirt", findOrCreateClothingItem("Jupe Plissée Business Marine", "Jupes", wardrobe,
            item -> {
                item.setOccasion("Business");
                item.setSize("M");
                item.setMaterial("Polyester");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?ixlib=rb-4.0.3");
                item.setBrand("Business Basics");
                item.setColors(Arrays.asList("Marine"));
                item.setPatterns(Collections.emptyList());
            }));

        // Dress
        items.put("summerDress", findOrCreateClothingItem("Robe d'Été Fleurie Bleu et Blanc", "Robes", wardrobe,
            item -> {
                item.setOccasion("Casual");
                item.setSize("M");
                item.setMaterial("Cotton");
                item.setSeason("Summer");
                item.setImageUrl("https://images.unsplash.com/photo-1595777457583-95e059d581b8?ixlib=rb-4.0.3");
                item.setBrand("Summer Style");
                item.setColors(Arrays.asList("Bleu", "Blanc"));
                item.setPatterns(Arrays.asList("Floral"));
            }));

        // Outerwear
        items.put("blazer", findOrCreateClothingItem("Blazer Business Noir Classique", "Vêtements d'extérieur", wardrobe,
            item -> {
                item.setOccasion("Business");
                item.setSize("M");
                item.setMaterial("Polyester Blend");
                item.setSeason("Spring");
                item.setImageUrl("https://images.unsplash.com/photo-1591047139829-d91aecb6caea?ixlib=rb-4.0.3");
                item.setBrand("Business Basics");
                item.setColors(Arrays.asList("Noir"));
                item.setPatterns(Collections.emptyList());
            }));

        // Shoes - Create unique shoes for each outfit
        items.put("casualSneakers1", findOrCreateClothingItem("Baskets Casual Blanches Sport 1", "Chaussures", wardrobe,
            item -> {
                item.setOccasion("Casual");
                item.setSize("42");
                item.setMaterial("Canvas");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1597248881519-db089d3744a5?ixlib=rb-4.0.3");
                item.setBrand("Comfort Walk");
                item.setColors(Arrays.asList("Blanc"));
                item.setPatterns(Collections.emptyList());
            }));
        
        items.put("oxfords", findOrCreateClothingItem("Chaussures Business Oxford Noires", "Chaussures", wardrobe,
            item -> {
                item.setOccasion("Business");
                item.setSize("42");
                item.setMaterial("Leather");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1614252235316-8c857d38b5f4?ixlib=rb-4.0.3");
                item.setBrand("Business Basics");
                item.setColors(Arrays.asList("Noir"));
                item.setPatterns(Collections.emptyList());
            }));

        items.put("casualSneakers2", findOrCreateClothingItem("Baskets Casual Blanches Sport 2", "Chaussures", wardrobe,
            item -> {
                item.setOccasion("Casual");
                item.setSize("42");
                item.setMaterial("Canvas");
                item.setSeason("Summer");
                item.setImageUrl("https://images.unsplash.com/photo-1600269452121-4f2416e55c28?ixlib=rb-4.0.3");
                item.setBrand("Comfort Walk");
                item.setColors(Arrays.asList("Blanc"));
                item.setPatterns(Collections.emptyList());
            }));

        // Bags
        items.put("tote", findOrCreateClothingItem("Grand Sac Fourre-tout Casual Beige", "Sacs", wardrobe,
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
        items.put("sunHat", findOrCreateClothingItem("Chapeau de Soleil d'Été Beige", "Chapeaux", wardrobe,
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
        items.put("belt", findOrCreateClothingItem("Ceinture Business Cuir Noir", "Bijoux", wardrobe,
            item -> {
                item.setOccasion("All");
                item.setSize("32");
                item.setMaterial("Leather");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1624222247344-550fb60583dc?ixlib=rb-4.0.3");
                item.setBrand("Accessories Co");
                item.setColors(Arrays.asList("Noir"));
                item.setPatterns(Collections.emptyList());
            }));
        
        items.put("watch", findOrCreateClothingItem("Montre Classique Argent", "Bijoux", wardrobe,
            item -> {
                item.setOccasion("All");
                item.setSize("One Size");
                item.setMaterial("Stainless Steel");
                item.setSeason("All");
                item.setImageUrl("https://images.unsplash.com/photo-1587836374828-4dbafa94cf0e?ixlib=rb-4.0.3");
                item.setBrand("Time Style");
                item.setColors(Arrays.asList("Argent"));
                item.setPatterns(Collections.emptyList());
            }));

        // Create outfits with unique names and unique shoes
        // Casual Summer Outfit
        findOrCreateOutfit("Tenue Casual d'Été avec Jeans", wardrobe,
            outfit -> {
                outfit.setDescription("Parfait pour une journée ensoleillée décontractée");
                outfit.setSeason("Summer");
                outfit.setOccasion("Casual");
                outfit.setTop(items.get("whiteTee"));
                outfit.setBottom(items.get("jeans"));
                outfit.setShoes(items.get("casualSneakers1")); // Using first pair of sneakers
                outfit.setAccessories(Arrays.asList(items.get("belt"), items.get("sunHat")));
            });

        // Business Outfit
        findOrCreateOutfit("Tenue Business Classique", wardrobe,
            outfit -> {
                outfit.setDescription("Pour les réunions professionnelles importantes");
                outfit.setSeason("All");
                outfit.setOccasion("Business");
                outfit.setTop(items.get("navyShirt"));
                outfit.setBottom(items.get("dressSlacks"));
                outfit.setOuterwear(items.get("blazer"));
                outfit.setShoes(items.get("oxfords")); // Using oxford shoes
                outfit.setAccessories(Arrays.asList(items.get("belt"), items.get("watch")));
            });

        // Summer Dress Outfit
        findOrCreateOutfit("Tenue Robe d'Été Élégante", wardrobe,
            outfit -> {
                outfit.setDescription("Tenue d'été légère et sophistiquée");
                outfit.setSeason("Summer");
                outfit.setOccasion("Casual");
                outfit.setDress(items.get("summerDress"));
                outfit.setShoes(items.get("casualSneakers2")); // Using second pair of sneakers
                outfit.setAccessories(Arrays.asList(items.get("watch"), items.get("sunHat"), items.get("tote")));
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