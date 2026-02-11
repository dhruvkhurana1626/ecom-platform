package com.example.demo.Utility;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.request.OrderItemRequest;
import com.example.demo.dto.request.SellerRequest;
import com.example.demo.exception.*;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.model.Seller;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Validation {

    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;

    // -------------------- EXISTENCE VALIDATIONS --------------------

    public Customer checkIfCustomerExist(int customerId) {

        // Ensure customer exists before proceeding
        return customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFound("Customer not found with id: " + customerId));
    }

    public Seller checkIfSellerExist(int sellerId) {

        // Ensure seller exists before proceeding
        return sellerRepository.findById(sellerId)
                .orElseThrow(() ->
                        new SellerNotFound("Seller not found with id: " + sellerId));
    }

    public Product checkIfProductExist(int productId) {

        // Ensure product exists before proceeding
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFound("Product not found with id: " + productId));
    }

    public boolean checkIfEmailExist(String email){

        //Ensure email dont exist before proceeding
        return customerRepository.existsByEmail(email);
    }

    public boolean checkIfPhoneNumberExist(String phonenumber){

        //Ensure phonenumber dont exist before proceeding
        return customerRepository.existsByPhonenumber(phonenumber);
    }

    // -------------------- CREATE VALIDATIONS --------------------

    public void validateNewCustomer(CustomerRequest request) {

        // Prevent duplicate customer email
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyUsed("Email already in use");
        }

        // Prevent duplicate customer phone number
        if (customerRepository.existsByPhonenumber(request.getPhonenumber())) {
            throw new PhoneAlreadyUsed("Phone number already in use");
        }
    }

    public void validateNewSeller(SellerRequest sellerRequest) {

        // Prevent duplicate seller email
        if (sellerRepository.existsByEmail(sellerRequest.getEmail())) {
            throw new EmailAlreadyUsed("Email already in use");
        }

        // Prevent duplicate PAN number
        if (sellerRepository.existsByPan(sellerRequest.getPan())) {
            throw new PanAlreadyUsed("PAN already in use");
        }
    }

    // -------------------- ORDER VALIDATIONS --------------------

    public void validateOrderItemsList(List<OrderItemRequest> orderItemRequestList) {

        // Order must contain at least one product
        if (orderItemRequestList == null || orderItemRequestList.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
    }

}
