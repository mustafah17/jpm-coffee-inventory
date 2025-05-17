package com.jpm.coffeeshop.supply.service;

import com.jpm.coffeeshop.supply.domain.ItemCategory;
import com.jpm.coffeeshop.supply.domain.SupplyItem;

import java.util.List;
import java.util.Optional;

public interface SupplyItemService {

    SupplyItem createSupplyItem (SupplyItem item);
    Optional<SupplyItem> getSupplyItem (Long id);
    List<SupplyItem> getAllSupplyItems();

    // Search methods
    List<SupplyItem> searchByName(String name);
    List<SupplyItem> searchByCategory(ItemCategory category);
    List<SupplyItem> searchBySupplierName(String supplierName);
    List<SupplyItem> searchBySupplierContact(String supplierContact);

    // Update methods
    SupplyItem updateSupplyItem(Long id, SupplyItem item);
    SupplyItem updateQuantity(Long id, int newQuantity);

    // Delete
    void deleteSupplyItem(Long id);

    // Reorder logic
    List<SupplyItem> findAllItemsToReorder();

    // Bulk save (for CSV import)
    List<SupplyItem> saveAll(List<SupplyItem> items);

}
