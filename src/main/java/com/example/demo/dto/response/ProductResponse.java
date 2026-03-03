package com.example.demo.dto.response;

import com.example.demo.enums.Category;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Category category;

    private String sellerName;   // lighter than full object
}
