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

    /**
     * Service layer encapsulates all business logic,
     * validation rules and persistence operations.
     * Controller remains a thin orchestration layer.
     */
    private final AddressService addressService;

    /**
     * Creates a new address for a given customer ID.
     * Any business exception (e.g., CustomerNotFound)
     * is propagated and handled by GlobalExceptionHandler.
     */
    @PostMapping
    public ResponseEntity addAddress(@RequestBody AddressRequest addressRequest,
                                     @RequestParam("id") int id) {

        AddressResponse response =
                addressService.addAddress(addressRequest, id);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Deletes address by ID.
     * Service layer determines existence and throws exception if invalid.
     */
    @DeleteMapping
    public ResponseEntity deleteAddress(@RequestParam("id") int id) {

        addressService.deleteAddress(id);

        return new ResponseEntity(
                "Address of customer with ID- " + id + " is deleted successfully.",
                HttpStatus.OK
        );
    }

    /**
     * Updates address details for the given ID.
     * Validation and entity consistency are enforced at service layer.
     */
    @PutMapping
    public ResponseEntity updateAddress(@RequestBody AddressRequest addressRequest,
                                        @RequestParam("id") int id) {

        AddressResponse response =
                addressService.updateAddress(addressRequest, id);

        return new ResponseEntity(response, HttpStatus.OK);
    }
}