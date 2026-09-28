package com.harika.inventorymanagementsystem.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStock(
            InsufficientStockException e) {

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(
            MethodArgumentNotValidException e) {

        String field = e.getBindingResult()
                .getFieldError()
                .getField();

        String message = e.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(field + ": " + message));
    }

    @ExceptionHandler(InvalidDiscountException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDiscount(
            InvalidDiscountException e) {

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}