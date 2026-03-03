package com.example.demo.repository;

import com.example.demo.enums.Category;
import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findByCategory(Category category);

    @Modifying
    @Query("""
       UPDATE Product p
       SET p.stock = p.stock - :qty
       WHERE p.id = :productId
       AND p.stock >= :qty
       """)
    int reduceStockIfAvailable(
            @Param("productId") Integer productId,
            @Param("qty") Integer qty);

    @Modifying
    @Query("""
       UPDATE Product p
       SET p.stock = p.stock + :qty
       WHERE p.id = :productId
       """)
    void incrementStock(
            @Param("productId") Integer productId,
            @Param("qty") Integer qty);
}
