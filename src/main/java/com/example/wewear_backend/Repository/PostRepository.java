package com.example.wewear_backend.Repository;

import com.example.wewear_backend.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Integer> {
}
