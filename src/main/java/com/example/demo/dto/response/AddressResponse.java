package com.example.demo.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class AddressResponse {
    private String houseno;
    private String city;
    private String state;
    private int pinCode;
}
