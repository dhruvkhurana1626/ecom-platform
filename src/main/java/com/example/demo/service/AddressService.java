package com.example.demo.service;

import com.example.demo.Utility.Validation;
import com.example.demo.dto.request.AddressRequest;
import com.example.demo.dto.response.AddressResponse;
import com.example.demo.dto.response.CustomerResponse;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.security.utility.SecurityUtil;
import com.example.demo.transformers.AddressTransformer;
import com.example.demo.transformers.CustomerTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final Validation validation;

    public AddressResponse addAddress(AddressRequest addressRequest) {

        String email = SecurityUtil.getCurrentUserEmail();
        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);

        Address address = AddressTransformer.addressRequestToAddress(addressRequest);

        // Important: set owning side
        address.setCustomer(customer);

        // Handle default logic
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            customer.getAddresses()
                    .forEach(addr -> addr.setIsDefault(false));
        }

        Address savedAddress = addressRepository.save(address);
        return AddressTransformer.addressToAddressResponse(savedAddress);
    }

    @Transactional
    public void deleteAddress(Integer addressId) {

        String email = SecurityUtil.getCurrentUserEmail();

        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);
        Address address = validation.checkAddressByAddressID_ReturnAddress(addressId);

        // Ownership check (important for security)
        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new InvalidRequestException("You cannot delete this address");
        }

        addressRepository.delete(address);
    }

    @Transactional
    public AddressResponse updateAddress(Integer addressId,
                                         AddressRequest addressRequest) {

        String email = SecurityUtil.getCurrentUserEmail();

        Customer customer =
                validation.checkCustomerByEmail_ReturnCustomer(email);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found"));

        // Ownership check
        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new InvalidRequestException("You cannot update this address");
        }

        // Update fields
        address.setHouseno(addressRequest.getHouseno());
        address.setPinCode(addressRequest.getPinCode());
        address.setState(addressRequest.getState());
        address.setCity(addressRequest.getCity());

        // Handle default flag change
        if (Boolean.TRUE.equals(addressRequest.getIsDefault())) {
            customer.getAddresses().forEach(addr -> addr.setIsDefault(false));
            address.setIsDefault(true);
        }

        return AddressTransformer.addressToAddressResponse(address);
    }

    public List<AddressResponse> getAllAddresses() {

        String email = SecurityUtil.getCurrentUserEmail();

        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);

        List<Address> addresses = customer.getAddresses();
        List<AddressResponse> addressResponses = new ArrayList<>();
        for(Address address : addresses){
            addressResponses.add(AddressTransformer.addressToAddressResponse(address));
        }
        
        return addressResponses;
    }

    @Transactional
    public void setDefault(Integer addressId) {

        String email = SecurityUtil.getCurrentUserEmail();

        Customer customer =
                validation.checkCustomerByEmail_ReturnCustomer(email);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found"));

        // Ownership check (security)
        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new InvalidRequestException("You cannot modify this address");
        }

        // Unset previous default
        customer.getAddresses()
                .forEach(addr -> addr.setIsDefault(false));

        // Set new default
        address.setIsDefault(true);
    }
}
