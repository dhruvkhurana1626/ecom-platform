package com.example.demo.security.controller;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.request.SellerRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.dto.response.SellerResponse;
import com.example.demo.security.service.AuthService;
import com.example.demo.security.dto.LoginRequest;
import com.example.demo.security.dto.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/customer")
    public ResponseEntity<CustomerResponse> registerCustomer(
            @RequestBody @Valid CustomerRequest customerRequest) {

        return new ResponseEntity<>(
                authService.registerCustomer(customerRequest),
                HttpStatus.CREATED);
    }

    @PostMapping("/register/seller")
    public ResponseEntity<SellerResponse> registerSeller(
            @RequestBody @Valid SellerRequest sellerRequest) {

        return new ResponseEntity<>(
                authService.registerSeller(sellerRequest),
                HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest loginRequest) {

        return ResponseEntity.ok(
                authService.login(loginRequest));
    }
}