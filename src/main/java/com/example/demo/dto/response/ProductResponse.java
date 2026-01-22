package com.example.demo.dto.response;

import com.example.demo.enums.Type;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class ProductResponse {
    private String name;
    private int price;
    private Type type;
    private SellerResponse sellerResponse;
}
