package com.example.wewear_backend.Service;

import com.example.wewear_backend.Model.Wardrobe;
import com.example.wewear_backend.Repository.WardrobeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class wardrobeService {
    private  WardrobeRepository wardrobeRepository;

    public wardrobeService(WardrobeRepository wardrobeRepository) {
        this.wardrobeRepository = wardrobeRepository;
    }

    public List<Wardrobe> getAllWardrobes() {
        return wardrobeRepository.findAll();
    }

    public Wardrobe getWardrobeById(Integer id) {
        return wardrobeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wardrobe not found"));
    }

    public Wardrobe createWardrobe(Wardrobe wardrobe) {
        return wardrobeRepository.save(wardrobe);
    }

    public Wardrobe updateWardrobe(Integer id, Wardrobe wardrobeDetails) {
        Wardrobe wardrobe = getWardrobeById(id);
        wardrobe.setName(wardrobeDetails.getName());
        return wardrobeRepository.save(wardrobe);
    }

    public void deleteWardrobe(Integer id) {
        wardrobeRepository.deleteById(id);
    }


}
