package com.example.demo.controller;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.enums.Category;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

    @PostMapping
    public ResponseEntity addProduct(@RequestBody ProductRequest productRequest,
                                     @RequestParam("Seller_id") int id) {

        ProductResponse productResponse =
                productService.addProduct(productRequest, id);

        return new ResponseEntity(productResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getProductByCategory(@RequestParam Category category) {

        List<ProductResponse> productResponseList =
                productService.getProductByCategory(category);

        return new ResponseEntity(productResponseList, HttpStatus.OK);
    }
}