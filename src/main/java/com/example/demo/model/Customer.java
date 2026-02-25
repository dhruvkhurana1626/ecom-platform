package com.example.demo.model;
import com.example.demo.enums.Gender;
import com.example.demo.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private int age;

    @Column(unique = true,nullable = false)
    private String email;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private boolean enabled;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(unique = true, length = 10,nullable = false)
    private String phonenumber;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="address_id")
    Address address;

    @CreationTimestamp
    Date date;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    List<OrderEntity> orderEntityList = new ArrayList<>();

}
