package com.example.Exchange_rates_bot.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
@Slf4j
@NoArgsConstructor
public class ApiError {
    @JsonIgnore
    private HttpStatus httpStatus;
    private String message;
    private String status;

    public ApiError badRequest(String message) {
        log.error(HttpStatus.BAD_REQUEST + " error: " + message);
        return new ApiError(HttpStatus.BAD_REQUEST, message, "error");
    }
}
