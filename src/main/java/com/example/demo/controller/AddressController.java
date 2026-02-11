package com.example.demo.controller;

import com.example.demo.dto.request.AddressRequest;
import com.example.demo.dto.response.AddressResponse;
import com.example.demo.exception.CustomerNotFound;
import com.example.demo.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity addAddress (@RequestBody AddressRequest addressRequest,
                           @RequestParam ("id") int id){
        try {
            AddressResponse response = addressService.addAddress(addressRequest, id);
            return new ResponseEntity(response,HttpStatus.OK);
        } catch (CustomerNotFound e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity deleteAddress(@RequestParam ("id") int id){
        try{
            addressService.deleteAddress(id);
            return new ResponseEntity("Address of customer with ID- "+id+" is deleted successfully.",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity updateAddress(@RequestBody AddressRequest addressRequest,
                                        @RequestParam ("id") int id){
        try{
            AddressResponse response = addressService.updateAddress(addressRequest,id);
            return new ResponseEntity(response,HttpStatus.OK);
        }
        catch (CustomerNotFound e){
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

}
