package com.shashank.ecommerce.inventory_service.Advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> globalExceptionHandler(Exception ex) {
            return new ResponseEntity<>(
                    ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
    }
}
