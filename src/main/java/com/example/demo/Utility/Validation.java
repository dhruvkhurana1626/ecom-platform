package com.example.demo.Utility;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.request.OrderItemRequest;
import com.example.demo.dto.request.SellerRequest;
import com.example.demo.exception.*;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.model.Seller;
import com.example.demo.repository.AddressRepository;
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
    private final AddressRepository addressRepository;

    public Customer checkCustomerByEmail_ReturnCustomer(String email){
        return customerRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Customer Not Found"));
    }

    public Address checkAddressByAddressID_ReturnAddress(int addressId){
        return addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address Not Found"));
    }

    public void validateNewCustomer(CustomerRequest request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new InvalidRequestException("Email already registered");
        }

        if (customerRepository.existsByPhonenumber(request.getPhonenumber())) {
            throw new InvalidRequestException("Phone number already registered");
        }

        if (request.getPassword().length() < 6) {
            throw new InvalidRequestException("Password must be at least 6 characters");
        }

        if (request.getAge() < 18) {
            throw new InvalidRequestException("Customer must be at least 18 years old");
        }
    }

    public void validateNewSeller(SellerRequest request) {

        if (sellerRepository.existsByEmail(request.getEmail())) {
            throw new InvalidRequestException("Email already registered");
        }

        if (sellerRepository.existsByPan(request.getPan())) {
            throw new InvalidRequestException("PAN already registered");
        }

        if (request.getPassword().length() < 6) {
            throw new InvalidRequestException("Password must be at least 6 characters");
        }
    }

    public Seller checkSellerByEmail_ReturnSeller(String email) {
        return sellerRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Seller Not Found"));
    }

    public Product checkProductByProductId_ReturnProduct(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));
    }

    public void checkProductExistById(Integer productId) {
        productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product Not Found"));
    }
}
