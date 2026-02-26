package com.example.demo.configuration;

import com.example.demo.enums.Role;
import com.example.demo.model.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomerUserDetails implements UserDetails {

    String email;
    String password;

    GrantedAuthority grantedAuthority;

    public CustomerUserDetails(Customer customer){
        this.email = customer.getEmail();
        this.password = customer.getPassword();

        String role = customer.getRole().name();

        Role roles = customer.getRole();
        return new SimpleGrantedAuthority("ROLE_"+role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
