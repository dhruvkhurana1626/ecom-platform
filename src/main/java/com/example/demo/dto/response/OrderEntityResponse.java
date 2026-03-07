package com.example.demo.dto.response;

import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntityResponse {

    private Integer orderId;
    private List<OrderItemsResponse> items;
    private BigDecimal totalAmount;
    private String clientSecret;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
}
