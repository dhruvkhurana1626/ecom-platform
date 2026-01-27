package com.example.demo.model;

import com.example.demo.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Builder
public class OrderEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private int id;

    @Column
    private int value;

    @Column
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "orderEntity")
    List<OrderItems> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="customer_id")
    Customer customer;
}
