package com.example.demo.transformers;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.model.Customer;


public class CustomerTransformer {

    public static Customer customerRequestToCustomer(CustomerRequest customerRequest){
        Customer customer = Customer.builder()
                .name(customerRequest.getName())
                .age(customerRequest.getAge())
                .email(customerRequest.getEmail())
                .gender(customerRequest.getGender())
                .phonenumber(customerRequest.getPhonenumber())
                .build();

        return customer;
    }

    public static CustomerResponse customerToCustomerResponse(Customer customer){
        CustomerResponse customerResponse = CustomerResponse.builder()
                .name(customer.getName())
                .email(customer.getEmail())
                .phonenumber(customer.getPhonenumber())
                .date(customer.getCreatedAt())
                .build();

        return customerResponse;
    }
}
