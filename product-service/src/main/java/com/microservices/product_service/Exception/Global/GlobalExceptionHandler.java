package com.microservices.product_service.Exception.Global;


import com.microservices.product_service.Exception.ProductNotFoundException;
import com.microservices.product_service.Exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler
    public ErrorResponse handleProductNotFoundException(ProductNotFoundException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .statusCode(404)
                .isSuccess(false)
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .statusCode(404)
                .isSuccess(false)
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    public ErrorResponse handleGenericException(Exception ex) {
        return ErrorResponse.builder()
                .message("Beklenmedik bir hata oluştu: " + ex.getMessage())
                .statusCode(500)
                .isSuccess(false)
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }
}

