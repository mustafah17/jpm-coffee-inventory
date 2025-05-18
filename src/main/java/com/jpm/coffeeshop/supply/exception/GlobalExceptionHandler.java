package com.jpm.coffeeshop.supply.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SupplyItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleSupplyItemNotFound(SupplyItemNotFoundException exception) {
        return exception.getMessage();
    }
}
