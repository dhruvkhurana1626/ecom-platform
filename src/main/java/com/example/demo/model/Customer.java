package com.example.demo.model;
import com.example.demo.enums.Gender;
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
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private int age;

    @Column
    private String email;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(length = 10)
    private String mob;

    @OneToOne(mappedBy = "customer")
    Address address;

    @CreationTimestamp
    Date date;

    @OneToMany(mappedBy = "customer")
    List<Review> reviewList = new ArrayList<>();

}
