package com.example.demo.security;

import com.example.demo.enums.Role;

public interface AppUser {
    String getEmail();
    String getPassword();
    Role getRole();
}
