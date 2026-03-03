package com.example.demo.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

    private Integer cartId;
    private List<CartItemResponse> items;
    private BigDecimal totalAmount;
}
