package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Seller {

    @Id
    private int id;

    @Column
    private String name;

    @Column(unique = true,nullable = false)
    private String email;

    @Column
    private String pan;

    @OneToMany(mappedBy = "seller")
    List<Product> productList = new ArrayList<>();
}
