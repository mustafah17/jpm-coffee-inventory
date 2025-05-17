package com.jpm.springboot.coffee_shop.domain;

import jakarta.persistence.Embeddable;

// Using a record instead of a class since this data is purely informational
@Embeddable
public record Supplier(String name, String contact) {}
