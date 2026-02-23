package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all "resource not found" type exceptions.
     * These represent valid requests but missing domain entities.
     */
    @ExceptionHandler({
            AddressNotFound.class,
            CustomerNotFound.class,
            ProductNotFound.class,
            SellerNotFound.class
    })
    public ResponseEntity<ErrorResponse> handleResourceNotFound(RuntimeException ex) {

        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles data conflict scenarios (duplicate values).
     * Indicates that resource already exists.
     */
    @ExceptionHandler({
            EmailAlreadyUsed.class,
            PhoneAlreadyUsed.class,
            PanAlreadyUsed.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {

        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Fallback handler for unexpected runtime exceptions.
     * Prevents internal details from leaking to clients.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        return buildErrorResponse(
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /**
     * Centralized method to construct uniform error responses.
     * Ensures consistent API error structure.
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            String message,
            HttpStatus status) {

        ErrorResponse error = new ErrorResponse(
                message,
                status.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, status);
    }
}