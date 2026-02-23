package com.example.demo.controller;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.enums.Category;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    /**
     * ProductService handles business validation,
     * seller verification, and persistence logic.
     * Controller remains a thin request delegator.
     */
    private final ProductService productService;

    /**
     * Creates a new product for a given seller ID.
     *
     * - Validates seller existence
     * - Persists product entity
     *
     * Any domain exception (e.g., SellerNotFound)
     * is handled globally via @RestControllerAdvice.
     */
    @PostMapping
    public ResponseEntity addProduct(@RequestBody ProductRequest productRequest,
                                     @RequestParam("Seller_id") int id) {

        ProductResponse productResponse =
                productService.addProduct(productRequest, id);

        return new ResponseEntity(productResponse, HttpStatus.CREATED);
    }

    /**
     * Retrieves products filtered by category.
     * Business filtering logic resides in service layer.
     */
    @GetMapping
    public ResponseEntity getProductByCategory(@RequestParam Category category) {

        List<ProductResponse> productResponseList =
                productService.getProductByCategory(category);

        return new ResponseEntity(productResponseList, HttpStatus.OK);
    }
}