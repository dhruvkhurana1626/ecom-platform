package com.example.demo.controller;

import com.example.demo.dto.request.AddressRequest;
import com.example.demo.dto.response.AddressResponse;
import com.example.demo.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/add")
    public ResponseEntity addAddress(@RequestBody @Valid AddressRequest addressRequest) {
        AddressResponse response = addressService.addAddress(addressRequest);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable Integer addressId,
            @RequestBody @Valid AddressRequest addressRequest) {

        AddressResponse response = addressService.updateAddress(addressId, addressRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAllAddresses() {
        List<AddressResponse> response = addressService.getAllAddresses();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<Void> setDefaultAddress(
            @PathVariable Integer addressId) {

        addressService.setDefault(addressId);
        return ResponseEntity.ok().build();
    }
}