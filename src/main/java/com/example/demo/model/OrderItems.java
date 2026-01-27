package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class OrderItems {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "orderentity_id")
    private OrderEntity orderEntity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int Quantity;
    private int priceAtOrder;

}
