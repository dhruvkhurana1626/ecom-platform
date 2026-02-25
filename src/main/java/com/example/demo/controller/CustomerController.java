package com.example.demo.controller;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.enums.Gender;
import com.example.demo.service.CustomerService;
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

    @GetMapping
    public ResponseEntity<CustomerResponse> getCustomerById(
            @RequestParam("id") int id) {

        CustomerResponse customerResponse =
                customerService.getCustomerById(id);

        return ResponseEntity.ok(customerResponse);
    }

    @GetMapping("/gender")
    public ResponseEntity<List<CustomerResponse>> getCustomersByGender(
            @RequestParam("gender") Gender gender) {

        List<CustomerResponse> customerResponseList =
                customerService.getCustomersByGender(gender);

        return ResponseEntity.ok(customerResponseList);
    }

    @GetMapping("/by-age")
    public ResponseEntity<List<CustomerResponse>> getCustomersByAge(
            @RequestParam("age") int age) {

        List<CustomerResponse> customerResponseList =
                customerService.getCustomersByAge(age);

        return ResponseEntity.ok(customerResponseList);
    }

    @PutMapping
    public ResponseEntity<CustomerResponse> updateCustomer(
            @RequestParam int customerId,
            @RequestBody CustomerRequest customerRequest) {

        CustomerResponse customerResponse =
                customerService.updateCustomer(customerId, customerRequest);

        return ResponseEntity.ok(customerResponse);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCustomer(
            @RequestParam int customerId) {

        String msg = customerService.deleteCustomer(customerId);

        return ResponseEntity.ok(msg);
    }
}