package com.example.demo.controller;

import com.example.demo.dto.request.AddressRequest;
import com.example.demo.dto.response.AddressResponse;
import com.example.demo.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/{id}")
    public ResponseEntity addAddress(@RequestBody AddressRequest addressRequest,
                                     @PathVariable int id) {

        AddressResponse response =
                addressService.addAddress(addressRequest, id);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAddress(@PathVariable int id) {

        addressService.deleteAddress(id);

        return new ResponseEntity(
                "Address of customer with ID- " + id + " is deleted successfully.",
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity updateAddress(@RequestBody AddressRequest addressRequest,
                                        @PathVariable int id) {

        AddressResponse response =
                addressService.updateAddress(addressRequest, id);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity getAddressById(@PathVariable int customerId){
        AddressResponse addressResponse =
                addressService.getAddressById(customerId);

        return ResponseEntity.ok(addressResponse);
    }
}