package com.jpm.springboot.coffee_shop.exception;

public class SupplyItemNotFoundException extends RuntimeException {
    public SupplyItemNotFoundException(String message) {
        super(message);
    }
}
