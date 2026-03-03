package com.example.demo.controller;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.enums.Gender;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/me")
    public ResponseEntity<CustomerResponse> getProfile() {
        return ResponseEntity.ok(
                customerService.getProfile()
        );
    }

    @PutMapping("/me")
    public ResponseEntity<CustomerResponse> updateProfile(
            @RequestBody @Valid CustomerRequest request) {

        return ResponseEntity.ok(
                customerService.updateProfile(request)
        );
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount() {

        customerService.deleteAccount();
        return ResponseEntity.noContent().build();
    }
}