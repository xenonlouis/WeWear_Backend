package com.example.wewear_backend.Repository;

import com.example.wewear_backend.Model.FavoriteOutfitComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteOutfitCommentRepository extends JpaRepository<FavoriteOutfitComment,Integer> {

}
