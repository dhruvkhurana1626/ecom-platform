package com.example.demo.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class ProductResponse {
    private String name;
    private int price;
    private SellerResponse sellerResponse;
}
