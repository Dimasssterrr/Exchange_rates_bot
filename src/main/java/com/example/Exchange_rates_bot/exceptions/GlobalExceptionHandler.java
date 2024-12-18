package com.example.Exchange_rates_bot.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {
    public ResponseEntity<ApiError> handel(RuntimeException ex) {
        ApiError apiError = new ApiError().badRequest(ex.getMessage());
        return new ResponseEntity<>(apiError,apiError.getHttpStatus());
    }

}
