package com.example.demo.service;

import com.example.demo.Utility.Email;
import com.example.demo.Utility.Validation;
import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.enums.Gender;
import com.example.demo.exception.ConflictException;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.transformers.CustomerTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final Validation validation;
    private final PasswordEncoder passwordEncoder;

    // 1️⃣ Get Logged-in Profile
    public CustomerResponse getProfile() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);

        return CustomerTransformer.customerToCustomerResponse(customer);
    }

    // 2️⃣ Update Logged-in Profile
    @Transactional
    public CustomerResponse updateProfile(CustomerRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);

        if(request.getName()!=null)customer.setName(request.getName());
        if(request.getAge()>=18)customer.setAge(request.getAge());
        if(request.getGender()!=null)customer.setGender(request.getGender());
        if(request.getPhonenumber()!=null)customer.setPhonenumber(request.getPhonenumber());

        // Update password only if provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            customer.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return CustomerTransformer.customerToCustomerResponse(customer);
    }

    // 3️⃣ Delete Logged-in Account
    @Transactional
    public void deleteAccount() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);

        customerRepository.delete(customer);
    }
}