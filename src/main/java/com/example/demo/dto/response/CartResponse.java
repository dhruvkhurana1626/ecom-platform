package com.example.demo.dto.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class CartResponse {
    private List<CartItemResponse> cartItemResponseList;
    private Integer totalAmt;
}
