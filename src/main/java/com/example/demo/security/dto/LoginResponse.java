package com.example.demo.security;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class LoginResponse {
    private String message;
    private String email;
    private String role;
    private String token;
}
