package com.example.demo.security;

import com.example.demo.Utility.Email;
import com.example.demo.Utility.Validation;
import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.request.SellerRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.dto.response.SellerResponse;
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
    private final Email email;
    private final JwtUtil jwtUtil;
    private LoginRequest request;

    @Transactional
    public CustomerResponse registerCustomer(CustomerRequest request) {

        validation.validateNewCustomer(request);

        Customer customer = CustomerTransformer.customerRequestToCustomer(request);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        Customer savedCustomer = customerRepository.save(customer);
        email.sendEmailAtCustomerRegistration(savedCustomer);
        return CustomerTransformer.customerToCustomerResponse(savedCustomer);
    }

    @Transactional
    public SellerResponse registerSeller(SellerRequest request) {

        validation.validateNewSeller(request);

        Seller seller = SellerTransformer.sellerRequestToSeller(request);
        seller.setPassword(passwordEncoder.encode(request.getPassword()));

        Seller savedSeller = sellerRepository.save(seller);
        email.sendEmailAtSellerRegistration(savedSeller);
        return SellerTransformer.sellerToSellerResponse(savedSeller);
    }

    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        AppUser user = userDetails.getUser();

        String token = jwtUtil.generateToken(user.getEmail(),user.getRole());

        return LoginResponse.builder()
                .message("Login successful")
                .email(user.getEmail())
                .role(user.getRole().name())
                .token(token)
                .build();
    }
}
