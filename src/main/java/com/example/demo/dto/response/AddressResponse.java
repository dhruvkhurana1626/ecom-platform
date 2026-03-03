package com.example.demo.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class AddressResponse {
    private Integer id;
    private String houseno;
    private String city;
    private String state;
    private String pinCode;
    private Boolean isDefault;
}
