package com.example.demo.exception;

public class AddressNotFound extends RuntimeException {
    public AddressNotFound(String message) {
        super(message);
    }
}
