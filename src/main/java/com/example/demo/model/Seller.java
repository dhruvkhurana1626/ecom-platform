package com.example.demo.model;

import com.example.demo.enums.Role;
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

public class Seller {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column
    private String name;

    @Column(unique = true,nullable = false)
    private String email;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private boolean enabled;

    @Column(unique = true,nullable = false)
    private String pan;

    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    List<Product> productList = new ArrayList<>();
}
