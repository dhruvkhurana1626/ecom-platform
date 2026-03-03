package com.example.demo.transformers;

import com.example.demo.dto.response.OrderEntityResponse;
import com.example.demo.dto.response.OrderItemsResponse;
import com.example.demo.model.OrderEntity;
import com.example.demo.model.OrderItems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderTransformer {

    public static OrderEntityResponse orderEntityToOrderEntityResponse(OrderEntity orderEntity){
        OrderEntityResponse orderEntityResponse = OrderEntityResponse.builder()
                .orderId(orderEntity.getId())
                .totalAmount(orderEntity.getTotalAmount())
                .orderStatus(orderEntity.getOrderStatus())
                .items(getOrderItemsResponseList(orderEntity.getOrderItems()))
                .build();

        return orderEntityResponse;
    }

    private static List<OrderItemsResponse> getOrderItemsResponseList(List<OrderItems> orderItemsList){
        List<OrderItemsResponse> orderItemsResponses = new ArrayList<>();
        for(OrderItems orderItems:orderItemsList){
            OrderItemsResponse orderItemsResponse = OrderItemsResponse.builder()
                    .productId(orderItems.getProduct().getId())
                    .productName(orderItems.getProduct().getName())
                    .priceAtPurchase(orderItems.getPrice())
                    .quantity(orderItems.getQuantity())
                    .subTotal(orderItems.getPrice().multiply(BigDecimal.valueOf(orderItems.getQuantity())))
                    .build();

            orderItemsResponses.add(orderItemsResponse);
        }
        return orderItemsResponses;
    }
}
