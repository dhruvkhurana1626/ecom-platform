package com.example.demo.exception;

public class EmailNotFound extends RuntimeException {
    public EmailNotFound(String message) {
        super(message);
    }
}