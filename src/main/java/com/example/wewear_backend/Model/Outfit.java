package com.example.wewear_backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Outfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "wardrobe_id")
    private Wardrobe wardrobe;

    private String name;
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Structured outfit composition
    @ManyToOne
    @JoinColumn(name = "top_id")
    private ClothingItem top;
    
    @ManyToOne
    @JoinColumn(name = "bottom_id")
    private ClothingItem bottom;
    
    @ManyToOne
    @JoinColumn(name = "dress_id")
    private ClothingItem dress;
    
    @ManyToOne
    @JoinColumn(name = "outerwear_id")
    private ClothingItem outerwear;
    
    @ManyToOne
    @JoinColumn(name = "shoes_id")
    private ClothingItem shoes;
    
    @ManyToMany
    @JoinTable(
        name = "outfit_accessories",
        joinColumns = @JoinColumn(name = "outfit_id"),
        inverseJoinColumns = @JoinColumn(name = "clothing_item_id")
    )
    private List<ClothingItem> accessories;

    // Recommendation-related fields
    private String season;
    private String occasion;
    private Double rating;
    private Integer timesWorn;

    @ElementCollection
    @CollectionTable(
        name = "outfit_tags",
        joinColumns = @JoinColumn(name = "outfit_id")
    )
    @Column(name = "tag")
    private List<String> tags;

    // For outfit preview
    private String imageUrl;
}
