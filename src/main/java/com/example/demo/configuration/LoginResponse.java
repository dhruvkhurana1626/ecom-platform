package com.example.demo.configuration;

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
}
