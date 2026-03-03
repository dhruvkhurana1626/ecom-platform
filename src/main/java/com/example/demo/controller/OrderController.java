package com.example.demo.controller;

import com.example.demo.dto.request.OrderEntityRequest;
import com.example.demo.dto.response.OrderEntityResponse;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Place order from cart
    @PostMapping
    public ResponseEntity<OrderEntityResponse> placeOrder(
            @RequestBody @Valid OrderEntityRequest request) {

        return new ResponseEntity<>(
                orderService.placeOrder(request),
                HttpStatus.CREATED
        );
    }

    // Cancel order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Integer orderId) {

        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}