package com.example.demo.model;
import com.example.demo.configuration.AppUser;
import com.example.demo.enums.Gender;
import com.example.demo.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Customer implements AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(unique = true, length = 10,nullable = false)
    private String phonenumber;

    @OneToMany(mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    List<Review> reviewList = new ArrayList<>();

    @OneToOne(mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Cart cart;


    @OneToMany(mappedBy = "customer",
            fetch = FetchType.LAZY)
    private List<OrderEntity> orderEntityList = new ArrayList<>();

    @PrePersist
    public void setDefaultRole() {
        if (role == null) {
            role = Role.CUSTOMER;
        }
    }

}
