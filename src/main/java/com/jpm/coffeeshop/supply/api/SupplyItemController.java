package com.jpm.coffeeshop.supply.api;

import com.jpm.coffeeshop.supply.domain.ItemCategory;
import com.jpm.coffeeshop.supply.domain.SupplyItem;
import com.jpm.coffeeshop.supply.exception.SupplyItemNotFoundException;
import com.jpm.coffeeshop.supply.service.SupplyItemService;
import com.jpm.coffeeshop.supply.util.CsvSupplyItemParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Supply Items", description = "API for managing coffe shop supply items")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/supply-items")
public class SupplyItemController {
    private final SupplyItemService service;

    // 1. CRUD interface to manage supply items by Item ID.
    @PostMapping
    @Operation(summary = "Create a new supply item")
    public ResponseEntity<SupplyItem> create (@RequestBody SupplyItem item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSupplyItem(item));
    }

    @GetMapping
    @Operation(summary = "Get all supply items")
    public List<SupplyItem> getAll() {
        return service.getAllSupplyItems();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get supply item by ID")
    public ResponseEntity<SupplyItem> getByID(@PathVariable Long id) {
        return service.getSupplyItem(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new SupplyItemNotFoundException("Item not found"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update supply team by ID")
    public SupplyItem update(@PathVariable Long id, @RequestBody SupplyItem item) {
        return service.updateSupplyItem(id, item);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete supply item by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteSupplyItem(id);
    }

    // 2. Search API to find supply items by any of their attributes.
    @GetMapping("/search")
    @Operation(summary = "Find supply items by any of their attributes")
    public List<SupplyItem> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ItemCategory category,
            @RequestParam(required = false) String supplierName,
            @RequestParam(required = false) String supplierContact) {
        if (name != null) return service.searchByName(name);
        if (category != null) return service.searchByCategory(category);
        if (supplierName != null) return service.searchBySupplierName(supplierName);
        if (supplierContact != null) return service.searchBySupplierContact(supplierContact);
        return List.of();
    }

    // 3. REST API to update the quantity of a supply item.
    @PatchMapping("/{id}/quantity")
    @Operation(summary = "Update the quantity of a supply item")
    public SupplyItem updateQuantity(@PathVariable Long id, @RequestParam int quantity) {
        return service.updateQuantity(id, quantity);
    }

    // 4. REST API to generate a reorder list based on the reorder level.
    @GetMapping("/to-reorder")
    @Operation(summary = "Generate a reorder list")
    public List<SupplyItem> getItemsToReorder() {
        return service.findAllItemsToReorder();
    }

    // 5. REST API to Import supply items from CSV (or any other format you prefer).
    @PostMapping("/import")
    @Operation(summary = "Import supply items from CSV")
    public List<SupplyItem> importFromCsv(@RequestParam("file") MultipartFile file) {
        try {
            List<SupplyItem> items = CsvSupplyItemParser.parseCsv(file);
            return service.saveAll(items);
        } catch (IOException e) {
            throw new RuntimeException("Failed to import CSV: " + e.getMessage(), e);
        }
    }

}
