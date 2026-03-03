package com.example.demo.controller;

import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.dto.response.SellerResponse;
import com.example.demo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(
                adminService.getAllCustomers()
        );
    }

    @GetMapping("/sellers")
    public ResponseEntity<List<SellerResponse>> getAllSellers() {
        return ResponseEntity.ok(
                adminService.getAllSellers()
        );
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(
                adminService.getAllProducts()
        );
    }

    @DeleteMapping("/customers/{customerId}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Integer customerId) {

        adminService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/sellers/{sellerId}")
    public ResponseEntity<Void> deleteSeller(
            @PathVariable Integer sellerId) {

        adminService.deleteSeller(sellerId);
        return ResponseEntity.noContent().build();
    }
}