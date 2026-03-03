package com.example.demo.configuration;

import com.example.demo.enums.Gender;
import com.example.demo.enums.Role;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner createAdmin() {
        return args -> {

            if (!customerRepository.existsByEmail("admin@shop.com")) {

                Customer admin = Customer.builder()
                        .name("Super Admin")
                        .age(25)
                        .email("admin@shop.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .gender(Gender.MALE)
                        .phonenumber("9999999999")
                        .build();

                customerRepository.save(admin);
            }
        };
    }
}