package com.example.demo.security.basicAuth;

import com.example.demo.model.Customer;
import com.example.demo.model.Seller;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsManager implements UserDetailsService {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private SellerRepository sellerRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // Try customer
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);
        if (customerOpt.isPresent()) {
            return new CustomUserDetails(customerOpt.get());
        }

        // Try seller
        Optional<Seller> sellerOpt = sellerRepository.findByEmail(email);
        if (sellerOpt.isPresent()) {
            return new CustomUserDetails(sellerOpt.get());
        }

        throw new UsernameNotFoundException("User not found");
    }
}
