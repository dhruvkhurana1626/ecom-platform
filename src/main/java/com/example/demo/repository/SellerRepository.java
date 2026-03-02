package com.example.demo.repository;

import com.example.demo.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller,Integer> {
    boolean existsByEmail(String email);
    boolean existsByPan(String pan);
    Optional<Seller> findByEmail(String email);
}
