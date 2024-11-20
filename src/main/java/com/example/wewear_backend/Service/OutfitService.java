package com.example.wewear_backend.Service;

import com.example.wewear_backend.Model.Outfit;
import com.example.wewear_backend.Repository.OutfitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutfitService {
    private final OutfitRepository outfitRepository;

    public OutfitService(OutfitRepository outfitRepository) {
        this.outfitRepository = outfitRepository;
    }

    public List<Outfit> getAllOutfits() {
        return outfitRepository.findAll();
    }

    public Outfit getOutfitById(Integer id) {
        return outfitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Outfit not found"));
    }

    public Outfit createOutfit(Outfit outfit) {
        return outfitRepository.save(outfit);
    }

    public Outfit updateOutfit(Integer id, Outfit outfitDetails) {
        Outfit outfit = getOutfitById(id);
        outfit.setName(outfitDetails.getName());
        outfit.setDescription(outfitDetails.getDescription());
        return outfitRepository.save(outfit);
    }

    public void deleteOutfit(Integer id) {
        outfitRepository.deleteById(id);
    }
}
