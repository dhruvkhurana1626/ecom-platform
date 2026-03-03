package com.example.demo.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CartItemResponse {
    private Integer productId;
    private String productName;
    private Integer price;
    private Integer quantity;
    private Integer subTotal;
}
