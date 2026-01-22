package com.example.demo.transformers;

import com.example.demo.dto.request.AddressRequest;
import com.example.demo.dto.response.AddressResponse;
import com.example.demo.model.Address;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AddressTransformer {

    public Address addressRequestToAddress(AddressRequest addressRequest){
        Address address = Address.builder()
                .houseno(addressRequest.getHouseno())
                .city(addressRequest.getCity())
                .state(addressRequest.getState())
                .pinCode(addressRequest.getPinCode())
                .build();

        return address;
    }

    public AddressResponse addressToAddressResponse(Address address){
        AddressResponse addressResponse = AddressResponse.builder()
                .houseno(address.getHouseno())
                .city(address.getCity())
                .state(address.getState())
                .pinCode(address.getPinCode())
                .build();

        return addressResponse;
    }
}
