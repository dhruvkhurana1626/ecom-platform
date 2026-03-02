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

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity placeOrder(@RequestParam int customerId,
                                     @RequestBody List<OrderItemRequest> orderItemRequestList) {

        OrderEntityResponse orderEntityResponse =
                orderService.placeOrder(customerId, orderItemRequestList);

        return new ResponseEntity(orderEntityResponse, HttpStatus.CREATED);
    }

    @PostMapping("/confirmpayment")
    public ResponseEntity updateOrderAfterPayment(@RequestParam ("orderId") int orderId,
                                                  @RequestParam ("success") boolean success){

        orderService.updateOrderAfterPayment(orderId,success);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/cancel")
    public ResponseEntity cancelOrder(@RequestParam ("orderId") int orderId) {

        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order cancelled");
    }
}