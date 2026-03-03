package com.example.demo.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private Integer id;          // useful for frontend
    private String name;
    private String email;
    private String phonenumber;
    private LocalDateTime createdAt;
}
