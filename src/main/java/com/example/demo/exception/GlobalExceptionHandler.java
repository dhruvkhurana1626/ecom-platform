package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({
            AddressNotFound.class,
            CustomerNotFound.class,
            ProductNotFound.class,
            SellerNotFound.class,
            EmailNotFound.class
    })
    public ResponseEntity<ErrorResponse> handleResourceNotFound(RuntimeException ex) {

        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({
            EmailAlreadyUsed.class,
            PhoneAlreadyUsed.class,
            PanAlreadyUsed.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {

        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        return buildErrorResponse(
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


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