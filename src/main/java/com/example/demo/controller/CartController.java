package com.example.demo.controller;

import com.example.demo.dto.request.AddToCartRequest;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public void addToCart(@RequestBody AddToCartRequest addToCartRequest){
        cartService.addToCart(addToCartRequest);
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(){
        CartResponse cartResponse = cartService.getCart();
        return ResponseEntity.ok(cartResponse);
    }
}
