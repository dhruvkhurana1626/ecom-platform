package com.example.demo.model;

import com.example.demo.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
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
    private Integer id;

    @Column
    private int totalCost;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "orderEntity",cascade = CascadeType.ALL)
    List<OrderItems> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="customer_id")
    Customer customer;
}
