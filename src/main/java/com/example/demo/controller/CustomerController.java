package com.example.demo.controller;

import com.example.demo.dto.request.CustomerRequest;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.enums.Gender;
import com.example.demo.exception.CustomerNotFound;
import com.example.demo.exception.EmailAlreadyUsed;
import com.example.demo.exception.PhoneAlreadyUsed;
import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping
    public ResponseEntity addCustomer(@RequestBody CustomerRequest customerRequest){
        try{
            CustomerResponse customerResponse = customerService.addCustomer(customerRequest);
            return new ResponseEntity(customerResponse, HttpStatus.OK);
        } catch (EmailAlreadyUsed e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        } catch (PhoneAlreadyUsed e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity getCustomerById(@RequestParam("id") int id){
        try {
            CustomerResponse customerResponse = customerService.getCustomerById(id);
            return new ResponseEntity (customerResponse,HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/gender")
    public ResponseEntity getCustomersByGender(@RequestParam("gender") Gender gender){
        try{
            List<CustomerResponse> customerResponseList = customerService.getCustomersByGender(gender);
            return new ResponseEntity(customerResponseList,HttpStatus.OK);
        } catch (CustomerNotFound e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-age")
    public ResponseEntity getCustomersByAge(@RequestParam("age") int age){
        List<CustomerResponse> customerResponseList = customerService.getCustomersByAge(age);
        return new ResponseEntity(customerResponseList,HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity upadteCustomer(@RequestParam int customerId,
                                         @RequestBody CustomerRequest customerRequest){
        try {
            CustomerResponse customerResponse = customerService.updateCustomer(customerId,customerRequest);
            return new ResponseEntity(customerResponse,HttpStatus.OK);
        }
        catch (RuntimeException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity deleteCustomer(@RequestParam int customerId){
        try{
            String msg = customerService.deleteCustomer(customerId);
            return new ResponseEntity(msg,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity("Customer Id is Invalid",HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/me")
//    public ResponseEntity getLoggedInCustomer()

}
