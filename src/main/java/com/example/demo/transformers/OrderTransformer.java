package com.example.demo.transformers;

import com.example.demo.dto.response.OrderEntityResponse;
import com.example.demo.dto.response.OrderItemsResponse;
import com.example.demo.model.OrderEntity;
import com.example.demo.model.OrderItems;

import java.util.ArrayList;
import java.util.List;

public class OrderTransformer {

    public static OrderEntityResponse orderEntityToOrderEntityResponse(OrderEntity orderEntity){
        OrderEntityResponse orderEntityResponse = OrderEntityResponse.builder()
                .customerResponse(CustomerTransformer.customerToCustomerResponse(orderEntity.getCustomer()))
                .id(orderEntity.getId())
                .totalCost(orderEntity.getTotalCost())
                .orderStatus(orderEntity.getOrderStatus())
                .createdAt(orderEntity.getCreatedAt())
                .orderItemsResponse(getOrderItemsResponseList(orderEntity.getOrderItems()))
                .build();

        return orderEntityResponse;
    }

    private static List<OrderItemsResponse> getOrderItemsResponseList(List<OrderItems> orderItemsList){
        List<OrderItemsResponse> orderItemsResponses = new ArrayList<>();
        for(OrderItems orderItems:orderItemsList){
            OrderItemsResponse orderItemsResponse = OrderItemsResponse.builder()
                    .productResponse(ProductTransformer.productToProductResponse(orderItems.getProduct()))
                    .quantiy(orderItems.getQuantity())
                    .build();

            orderItemsResponses.add(orderItemsResponse);
        }
        return orderItemsResponses;
    }
}
