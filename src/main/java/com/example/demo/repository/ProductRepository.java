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

    @Modifying(clearAutomatically = true)
    @Query("""
    UPDATE Product p
    SET p.quantity = p.quantity - :qty
    WHERE p.id = :id AND p.quantity >= :qty
""")
    int reduceStock(@Param("id") Integer id,
                    @Param("qty") Integer qty);

    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE Product p
        SET p.quantity = p.quantity + :qty
        WHERE p.id = :id
    """)
    int increaseStock(@Param("id") Integer id,
                      @Param("qty") Integer qty);
}
