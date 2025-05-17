package com.jpm.coffeeshop.supply.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

// Using a record instead of a class since this data is purely informational
@Embeddable
@AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "supplier_name")),
        @AttributeOverride(name = "contact", column = @Column(name = "supplier_contact")),
})
public record Supplier(String name, String contact) {}
