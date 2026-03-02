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
import com.example.demo.transformers.AddressTransformer;
import com.example.demo.transformers.CustomerTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final Validation validation;

    public AddressResponse addAddress(AddressRequest addressRequest, int customerId) {
        Customer customer = validation.checkIfCustomerExist(customerId);
        Address address = AddressTransformer.addressRequestToAddress(addressRequest);
        customer.setAddress(address);
        Customer savedCustomer = customerRepository.save(customer);
        AddressResponse addressResponse = AddressTransformer.addressToAddressResponse(address);
        addressResponse.setCustomerResponse(CustomerTransformer.customerToCustomerResponse(savedCustomer));
        return addressResponse;
    }

    @Transactional
    public void deleteAddress(int customerId) {

        // Validate customer existence
        Customer customer = validation.checkIfCustomerExist(customerId);

        // Fetch existing address
        Address address = customer.getAddress();

        // If no address exists, deletion is not possible
        if (address == null) {
            throw new InvalidRequestException("Please add an address before attempting to delete.");
        }

        // Remove address mapping
        customer.setAddress(null);

        // Persist the change
        customerRepository.save(customer);
    }

    @Transactional
    public AddressResponse updateAddress(AddressRequest addressRequest, int customerId) {

        // Validate customer existence
        Customer customer = validation.checkIfCustomerExist(customerId);

        // Fetch existing address
        Address address = customer.getAddress();

        // If address does not exist, update is not allowed
        if (address == null) {
            throw new ResourceNotFoundException("No address found to update.");
        }

        // Update address fields
        address.setHouseno(addressRequest.getHouseno());
        address.setPinCode(addressRequest.getPinCode());
        address.setState(addressRequest.getState());
        address.setCity(addressRequest.getCity());

        // Address is automatically updated due to transactional context
        return AddressTransformer.addressToAddressResponse(address);
    }

    public AddressResponse getAddressById(int customerId) {

        Customer customer = validation.checkIfCustomerExist(customerId);
        Address address = customer.getAddress();
        CustomerResponse customerResponse = CustomerTransformer.customerToCustomerResponse(customer);
        AddressResponse addressResponse = AddressTransformer.addressToAddressResponse(address);
        addressResponse.setCustomerResponse(customerResponse);

        return addressResponse;
    }
}
