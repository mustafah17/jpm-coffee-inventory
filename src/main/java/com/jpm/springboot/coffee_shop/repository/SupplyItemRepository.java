package com.jpm.springboot.coffee_shop.repository;

import com.jpm.springboot.coffee_shop.domain.ItemCategory;
import com.jpm.springboot.coffee_shop.domain.SupplyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// JPA gives CRUD by default
@Repository
public interface SupplyItemRepository extends JpaRepository<SupplyItem, Long> {

    // find all items with a (partial) name match (case-insensitive)
    List<SupplyItem> findByNameContainingIgnoreCase(String name);

    // find all items in catgeory
    List<SupplyItem> findByCategory(ItemCategory category);

    // find all items below or at their reorderLevel
    @Query("SELECT s FROM SupplyItem s WHERE s.stock <= s.reorderLevel")
    List<SupplyItem> findAllItemsToReorder();

    // find all items with a (partial) supplier name match (case-insensitive)
    List<SupplyItem> findBySupplierNameContainingIgnoreCase(String supplierName);

    // find all items with a (partial) contact match (case-insensitive)
    List<SupplyItem> findBySupplierContactContainingIgnoreCase(String supplierContact);
}
