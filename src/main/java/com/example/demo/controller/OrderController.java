package com.example.demo.controller;

import com.example.demo.dto.request.OrderItemRequest;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
public class OrderController  {

    @Autowired
    OrderService orderService;

//    @PostMapping
//    public ResponseEntity createOrder(@RequestParam ("custId") int customerId,
//                                      @RequestBody List<OrderItemRequest> orderItemRequest){
//
//    }

}