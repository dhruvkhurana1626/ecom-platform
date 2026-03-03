package com.example.demo.model;

import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 1️⃣ Money (Stripe ready)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    // 2️⃣ Currency
    @Column(nullable = false)
    private String currency;

    // 3️⃣ Order Status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    // 4️⃣ Payment Status (ENUM, not String)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    // 5️⃣ Stripe reference
    @Column(unique = true)
    private String paymentIntentId;

    // 6️⃣ Address snapshot (recommended)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    // 7️⃣ Order items
    @OneToMany(mappedBy = "orderEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrderItems> orderItems = new ArrayList<>();

    // 8️⃣ Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @CreationTimestamp
    private LocalDateTime createdAt;
}