package com.example.demo.dto.response;

import jdk.jfr.Category;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class SellerResponse {
    private String name;
    private String email;
}
