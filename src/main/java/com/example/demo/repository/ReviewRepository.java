package com.example.demo.repository;

import com.example.demo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
    List<Review> findByCommentContainingIgnoreCase(String word);

    boolean existsByCustomerIdAndProductId(Integer id, Integer id1);

    List<Review> findByCustomerId(Integer id);

    Object findByProductId(Integer productId);
}
