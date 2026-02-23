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
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class CustomerController {

    // Service layer handles business logic.
    // Controller should remain thin and delegate processing.
    private final CustomerService customerService;

    /**
     * Creates a new customer.
     * Delegates validation and business rules to service layer.
     */
    @PostMapping
    public ResponseEntity<CustomerResponse> addCustomer(
            @RequestBody CustomerRequest customerRequest) {

        CustomerResponse customerResponse =
                customerService.addCustomer(customerRequest);

        return ResponseEntity.ok(customerResponse);
    }

    /**
     * Fetch customer by unique ID.
     * Throws CustomerNotFound if not present (handled globally).
     */
    @GetMapping
    public ResponseEntity<CustomerResponse> getCustomerById(
            @RequestParam("id") int id) {

        CustomerResponse customerResponse =
                customerService.getCustomerById(id);

        return ResponseEntity.ok(customerResponse);
    }

    /**
     * Retrieve customers filtered by gender.
     * Business filtering logic resides in service layer.
     */
    @GetMapping("/gender")
    public ResponseEntity<List<CustomerResponse>> getCustomersByGender(
            @RequestParam("gender") Gender gender) {

        List<CustomerResponse> customerResponseList =
                customerService.getCustomersByGender(gender);

        return ResponseEntity.ok(customerResponseList);
    }

    /**
     * Retrieve customers by age.
     * Keeps controller logic minimal and response consistent.
     */
    @GetMapping("/by-age")
    public ResponseEntity<List<CustomerResponse>> getCustomersByAge(
            @RequestParam("age") int age) {

        List<CustomerResponse> customerResponseList =
                customerService.getCustomersByAge(age);

        return ResponseEntity.ok(customerResponseList);
    }

    /**
     * Updates existing customer details.
     * Service layer ensures validation and entity consistency.
     */
    @PutMapping
    public ResponseEntity<CustomerResponse> updateCustomer(
            @RequestParam int customerId,
            @RequestBody CustomerRequest customerRequest) {

        CustomerResponse customerResponse =
                customerService.updateCustomer(customerId, customerRequest);

        return ResponseEntity.ok(customerResponse);
    }

    /**
     * Deletes customer by ID.
     * Throws exception if ID is invalid (handled globally).
     */
    @DeleteMapping
    public ResponseEntity<String> deleteCustomer(
            @RequestParam int customerId) {

        String msg = customerService.deleteCustomer(customerId);

        return ResponseEntity.ok(msg);
    }
}