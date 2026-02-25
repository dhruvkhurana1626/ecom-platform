package com.example.demo.controller;

import com.example.demo.configuration.LoginRequest;
import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.request.SellerRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.dto.response.SellerResponse;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity registerCustomer(@RequestBody CustomerRequest customerRequest){
        CustomerResponse customerResponse = authService.registerCustomer(customerRequest);
        return ResponseEntity.ok(customerResponse);
    }

    @PostMapping("/register/seller")
    public ResponseEntity registerSeller(@RequestBody SellerRequest sellerRequest){
        SellerResponse sellerResponse = authService.registerSeller(sellerRequest);
        return ResponseEntity.ok(sellerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest){
        String response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
