package com.example.demo.transformers;

import com.example.demo.dto.request.AddressRequest;
import com.example.demo.dto.response.AddressResponse;
import com.example.demo.model.Address;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AddressTransformer {

    public Address addressRequestToAddress(AddressRequest request) {
        return Address.builder()
                .houseno(request.getHouseno())
                .city(request.getCity())
                .state(request.getState())
                .pinCode(request.getPinCode())
                .isDefault(request.getIsDefault())
                .build();
    }

    public AddressResponse addressToAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .houseno(address.getHouseno())
                .city(address.getCity())
                .state(address.getState())
                .pinCode(address.getPinCode())
                .isDefault(address.getIsDefault())
                .build();
    }
}
