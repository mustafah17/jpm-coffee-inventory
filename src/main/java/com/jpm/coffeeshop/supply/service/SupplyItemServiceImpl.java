package com.jpm.coffeeshop.supply.service;

import com.jpm.coffeeshop.supply.exception.SupplyItemNotFoundException;
import com.jpm.coffeeshop.supply.domain.ItemCategory;
import com.jpm.coffeeshop.supply.domain.SupplyItem;
import com.jpm.coffeeshop.supply.repository.SupplyItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SupplyItemServiceImpl implements SupplyItemService {

    private final SupplyItemRepository repository;

    @Override
    public SupplyItem createSupplyItem(SupplyItem item) {
        return repository.save(item);
    }

    @Override
    public Optional<SupplyItem> getSupplyItem(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<SupplyItem> getAllSupplyItems() {
        return repository.findAll();
    }

    @Override
    public List<SupplyItem> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<SupplyItem> searchByCategory(ItemCategory category) {
        return repository.findByCategory(category);
    }

    @Override
    public List<SupplyItem> searchBySupplierName(String supplierName) {
        return repository.findBySupplierNameContainingIgnoreCase(supplierName);
    }

    @Override
    public List<SupplyItem> searchBySupplierContact(String supplierContact) {
        return repository.findBySupplierContactContainingIgnoreCase(supplierContact);
    }

    @Override
    public SupplyItem updateSupplyItem(Long id, SupplyItem item) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(item.getName());
                    existing.setCategory(item.getCategory());
                    existing.setStock(item.getStock());
                    existing.setReorderLevel(item.getReorderLevel());
                    existing.setSupplier(item.getSupplier());
                    return repository.save(existing);
                }).orElseThrow(() -> new SupplyItemNotFoundException("Supply item not found"));
    }

    @Override
    public SupplyItem updateQuantity(Long id, int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be a negative number");
        }
        return repository.findById(id)
                .map(existing -> {
                    existing.setStock(newQuantity);
                    return repository.save(existing);
                }).orElseThrow(() -> new SupplyItemNotFoundException("Supply item not found"));
    }

    @Override
    public void deleteSupplyItem(Long id) {
        if (!repository.existsById(id)) {
            throw new SupplyItemNotFoundException("Supply item not found");
        }
        repository.deleteById(id);
    }

    @Override
    public List<SupplyItem> findAllItemsToReorder() {
        return repository.findAllItemsToReorder();
    }

    @Override
    public List<SupplyItem> saveAll(List<SupplyItem> items) {
        return repository.saveAll(items);
    }
}
