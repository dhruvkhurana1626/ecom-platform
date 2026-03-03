package com.example.demo.controller;

import com.example.demo.dto.request.AddToCartRequest;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 1️⃣ Add or update item
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addToCart(
            @RequestBody @Valid AddToCartRequest request) {

        return ResponseEntity.ok(cartService.addToCart(request));
    }

    // 2️⃣ View cart
    @GetMapping
    public ResponseEntity<CartResponse> getCart() {

        return ResponseEntity.ok(cartService.getCart());
    }

    // 3️⃣ Remove specific item
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeItem(
            @PathVariable Integer productId) {

        return ResponseEntity.ok(cartService.removeItem(productId));
    }

    // 4️⃣ Clear cart
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {

        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}
