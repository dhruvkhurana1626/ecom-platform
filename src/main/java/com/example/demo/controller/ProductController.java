package com.example.demo.controller;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.enums.Category;
import com.example.demo.exception.SellerNotFound;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")

public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
    public ResponseEntity addProduct(@RequestBody ProductRequest productRequest,
                                     @RequestParam ("Seller_id") int id){
        try{
            ProductResponse productResponse = productService.addProduct(productRequest,id);
            return new ResponseEntity(productResponse, HttpStatus.OK);
        }
        catch (SellerNotFound e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity getProductByCategory(@RequestParam Category category){
        List<ProductResponse> productResponseList = productService.getProductByCategory(category);
        return new ResponseEntity(productResponseList,HttpStatus.OK);
    }
}
