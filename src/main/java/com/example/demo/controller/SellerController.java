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

    private final SellerService sellerService;

}