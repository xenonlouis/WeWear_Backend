package com.example.wewear_backend.Model;

import com.example.wewear_backend.Model.Wardrobe;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClothingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "wardrobe_id")
    @JsonBackReference
    private Wardrobe wardrobe;

    private String name;
    private String category;
    private String occasion;
    private String size;
    private String material;
    private String season;
    private String imageUrl;
    private String brand;
    private Double rating;
    private Double price;
    private String purchaseDate;
    private String purchaseLink;

    @ElementCollection
    @CollectionTable(name = "clothing_colors",
            joinColumns = @JoinColumn(name = "clothing_id"))

    @Column(name = "color")
    private List<String> colors;

    @ElementCollection
    @CollectionTable(name = "clothing_patterns",
            joinColumns = @JoinColumn(name = "clothing_id"))

    @Column(name = "pattern")
    private List<String> patterns;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
