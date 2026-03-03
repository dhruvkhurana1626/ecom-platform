package com.example.demo.dto.request;
import com.example.demo.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    @NotBlank
    private String name;

    @Min(18)
    private int age;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private Gender gender;

    @NotBlank
    @Size(min = 10, max = 10)
    private String phonenumber;

    @NotBlank
    @Size(min = 6)
    private String password;
}
