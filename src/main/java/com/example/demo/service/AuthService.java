package com.example.demo.service;

import com.example.demo.Utility.Validation;
import com.example.demo.configuration.LoginRequest;
import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.request.SellerRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.dto.response.SellerResponse;
import com.example.demo.enums.Role;
import com.example.demo.model.Customer;
import com.example.demo.model.Seller;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.SellerRepository;
import com.example.demo.transformers.CustomerTransformer;
import com.example.demo.transformers.SellerTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final SellerRepository sellerRepository;
    private final CustomerRepository customerRepository;
    private final Validation validation;

    @Transactional
    public CustomerResponse registerCustomer(CustomerRequest customerRequest) {

        validation.validateNewCustomer(customerRequest);

        Customer customer = CustomerTransformer.customerRequestToCustomer(customerRequest);
        customer.setPassword(passwordEncoder.encode(customerRequest.getPassword()));
        customer.setRole(Role.CUSTOMER);
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerTransformer.customerToCustomerResponse(savedCustomer);

    }

    @Transactional
    public SellerResponse registerSeller(SellerRequest sellerRequest) {

        validation.validateNewSeller(sellerRequest);

        Seller seller = SellerTransformer.sellerRequestToSeller(sellerRequest);
        seller.setPassword(passwordEncoder.encode(sellerRequest.getPassword()));
        seller.setRole(Role.SELLER);

        Seller savedSeller = sellerRepository.save(seller);
        return SellerTransformer.sellerToSellerResponse(savedSeller);

    }

    private final AuthenticationManager authenticationManager;

    public String login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        return "Login successful";
    }
}
