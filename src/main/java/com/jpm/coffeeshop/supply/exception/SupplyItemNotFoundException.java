package com.jpm.coffeeshop.supply.exception;

public class SupplyItemNotFoundException extends RuntimeException {
    public SupplyItemNotFoundException(String message) {
        super(message);
    }
}
