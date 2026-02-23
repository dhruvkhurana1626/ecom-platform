package com.example.demo.controller;

import com.example.demo.dto.request.OrderItemRequest;
import com.example.demo.dto.response.OrderEntityResponse;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    /**
     * OrderService encapsulates order creation logic,
     * inventory validation, and customer verification.
     * Controller remains responsible only for request handling.
     */
    private final OrderService orderService;

    /**
     * Places an order for a given customer.
     *
     * - Validates customer existence
     * - Validates order items
     * - Persists order and related entities
     *
     * Any domain exception (e.g., CustomerNotFound,
     * IllegalArgumentException) is handled globally
     * via @RestControllerAdvice.
     */
    @PostMapping
    public ResponseEntity placeOrder(@RequestParam int customerId,
                                     @RequestBody List<OrderItemRequest> orderItemRequestList) {

        OrderEntityResponse orderEntityResponse =
                orderService.placeOrder(customerId, orderItemRequestList);

        return new ResponseEntity(orderEntityResponse, HttpStatus.CREATED);
    }
}