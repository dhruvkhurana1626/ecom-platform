package com.example.demo.controller;

import com.example.demo.dto.request.SellerRequest;
import com.example.demo.dto.response.SellerResponse;
import com.example.demo.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    /**
     * SellerService handles validation logic such as:
     * - Duplicate email detection
     * - Duplicate PAN validation
     * - Persistence of seller entity
     *
     * Controller strictly delegates request handling.
     */
    private final SellerService sellerService;

    /**
     * Registers a new seller.
     *
     * Business exceptions like EmailAlreadyUsed
     * or PanAlreadyUsed are handled centrally
     * by GlobalExceptionHandler.
     */
    @PostMapping
    public ResponseEntity addSeller(@RequestBody SellerRequest sellerRequest) {

        SellerResponse response =
                sellerService.addSeller(sellerRequest);

        return new ResponseEntity(response, HttpStatus.CREATED);
    }
}