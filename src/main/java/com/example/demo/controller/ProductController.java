package com.example.demo.controller;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.enums.Category;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> addProduct(
            @RequestBody @Valid ProductRequest request) {
        return new ResponseEntity<>(productService.addProduct(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @RequestParam Category category) {
        return ResponseEntity.ok(productService.getProductByCategory(category));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductByProductId(
            @PathVariable int productId){
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> updateProductByProductId(
            @RequestBody @Valid ProductRequest productRequest,
            @PathVariable int productId){
        return ResponseEntity.ok(productService.updateProductByProductId(productRequest,productId));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity deleteProductById(@PathVariable int productId){
        productService.deleteProductById(productId);
        return ResponseEntity.ok().build();
    }
}