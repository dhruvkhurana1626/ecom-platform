package com.example.demo.service;

import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.dto.response.SellerResponse;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SellerRepository;
import com.example.demo.transformers.CustomerTransformer;
import com.example.demo.transformers.ProductTransformer;
import com.example.demo.transformers.SellerTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerTransformer::customerToCustomerResponse)
                .toList();
    }

    public List<SellerResponse> getAllSellers() {
        return sellerRepository.findAll()
                .stream()
                .map(SellerTransformer::sellerToSellerResponse)
                .toList();
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductTransformer::productToProductResponse)
                .toList();
    }

    public void deleteCustomer(Integer id) {
        customerRepository.deleteById(id);
    }

    public void deleteSeller(Integer id) {
        sellerRepository.deleteById(id);
    }
}
