package com.example.demo.model;

import com.example.demo.enums.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column
    private String name;

    @Column
    private int price;

    @Enumerated(value = EnumType.STRING)
    Category category;

    @ManyToOne
    @JoinColumn(name="seller_id")
    Seller seller;

    @OneToMany(mappedBy = "product")
    List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    List<OrderItems> orderItems = new ArrayList<>();

}
