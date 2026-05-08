package com.example.demo.controller;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.security.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void should_ReturnResponse_IfCustomer_isCreated() throws Exception{

        CustomerRequest request = new CustomerRequest();
        request.setName("Dhruv");
        request.setEmail("Dhruvkhurana162@gmail.com");
        request.setPhonenumber("8368799788");

        CustomerResponse response = new CustomerResponse();
        response.setName("Dhruv");
        response.setEmail("Dhruvkhurana162@gmail.com");
        response.setPhonenumber("8368799788");
        response.setCreatedAt(LocalDateTime.now());

        //Mocking the Service Layer
        //Controlling the output
        when(authService.registerCustomer(any(CustomerRequest.class))).thenReturn(response);

        //Performing HTTP Request
        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.name").value("Dhruv"))
                .andExpect(jsonPath("$.email").value("Dhruvkhurana162@gmail.com"))
                .andExpect(jsonPath("$.phonenumber").value("8368799788"))
                .andExpect(jsonPath("$.date").isNotEmpty());
    }

}
