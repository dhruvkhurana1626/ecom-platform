package com.example.demo.dto.request;

import com.example.demo.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ProductRequest {
    private String name;
    private int price;
    private int quantity;
    private Category category;
}
