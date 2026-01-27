package com.example.demo.service;

import com.example.demo.dto.request.AddressRequest;
import com.example.demo.dto.response.AddressResponse;
import com.example.demo.exception.AddressNotFound;
import com.example.demo.exception.CustomerNotFound;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.transformers.AddressTransformer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class AddressService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AddressRepository addressRepository;

    @Transactional
    public AddressResponse addAddress(AddressRequest addressRequest, int id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if(optionalCustomer.isEmpty()){
            throw new CustomerNotFound("Invalid Id");
        }

        Address address = AddressTransformer.addressRequestToAddress(addressRequest);

        Customer customer = optionalCustomer.get();
        customer.setAddress(address);
        customerRepository.save(customer);

        return AddressTransformer.addressToAddressResponse(address);
    }

    @Transactional
    public void deleteAddress(int id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if(optionalCustomer.isEmpty()){
            throw new CustomerNotFound("Customer With ID -" + id + "doesnt exist");
        }

        Customer customer = optionalCustomer.get();

        Address address = customer.getAddress();
        if(address==null){
            throw new AddressNotFound("Pls save at least one address before trying to delete.");
        }

        customer.setAddress(null);
        customerRepository.save(customer);
    }

    public AddressResponse updateAddress(AddressRequest addressRequest, int id) {

        //validating customer id
        Customer customer = customerRepository.findById(id).orElseThrow(()->new CustomerNotFound("Customer ID is Invalid"));

        Address address = customer.getAddress();

        //validation address
        if (address == null) {
            throw new AddressNotFound("No address found to update");
        }

        //update
        address.setHouseno(addressRequest.getHouseno());
        address.setPinCode(addressRequest.getPinCode());
        address.setState(addressRequest.getState());
        address.setCity(addressRequest.getCity());

        //return the response
        return AddressTransformer.addressToAddressResponse(address);
    }
}
