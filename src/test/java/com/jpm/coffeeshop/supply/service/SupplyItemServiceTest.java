package com.jpm.coffeeshop.supply.service;

import com.jpm.coffeeshop.supply.domain.ItemCategory;
import com.jpm.coffeeshop.supply.domain.Supplier;
import com.jpm.coffeeshop.supply.domain.SupplyItem;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SupplyItemServiceTest {

    @Autowired
    private SupplyItemService service;

    private Supplier supplier;

    @BeforeEach
    void setup() {
        supplier = new Supplier("Test Supplier", "07777");
    }

    @Test
    void testCRUD() {
        // CREATE
        SupplyItem item = new SupplyItem(null, "Espresso Beans", ItemCategory.COFFEE_BEANS, 10, 5, supplier);
        SupplyItem saved = service.createSupplyItem(item);
        assertNotNull(saved.getItemID());

        // READ
        var fetched = service.getSupplyItem(saved.getItemID());
        assertTrue(fetched.isPresent());
        assertEquals("Espresso Beans", fetched.get().getName());

        // UPDATE
        SupplyItem update = new SupplyItem(null, "Cow Milk", ItemCategory.MILK, 20, 7, supplier);
        SupplyItem updated = service.updateSupplyItem(saved.getItemID(), update);
        assertEquals("Cow Milk", updated.getName());
        assertEquals(20, updated.getStock());

        // DELETE
        service.deleteSupplyItem(saved.getItemID());
        assertTrue(service.getSupplyItem(saved.getItemID()).isEmpty());
    }

    @Test
    void testSearch() {
        service.createSupplyItem(new SupplyItem(null, "Espresso Beans", ItemCategory.COFFEE_BEANS, 10, 5, supplier));
        service.createSupplyItem(new SupplyItem(null, "Milk", ItemCategory.MILK, 20, 7, supplier));

        // Search by Partial Name
        var found = service.searchByName("Espresso");
        assertFalse(found.isEmpty());
        assertEquals("Espresso Beans", found.get(0).getName());

        // Search by Category
        var found2 = service.searchByCategory(ItemCategory.MILK);
        assertFalse(found2.isEmpty());
        assertEquals("Milk", found2.get(0).getName());
    }

    @Test
    void testUpdateQuantityAndReorderList() {
        SupplyItem beans = service.createSupplyItem(new SupplyItem(null, "Espresso Beans", ItemCategory.COFFEE_BEANS, 10, 5, supplier));
        SupplyItem milk = service.createSupplyItem(new SupplyItem(null, "Milk", ItemCategory.MILK, 20, 7, supplier));

        List<SupplyItem> reorder = service.findAllItemsToReorder();
        assertEquals(0, reorder.size());

        SupplyItem updatedBeans = service.updateQuantity(beans.getItemID(), 5);
        assertEquals(5, updatedBeans.getStock());

        SupplyItem updatedMilk = service.updateQuantity(milk.getItemID(), 5);
        assertEquals(5, updatedMilk.getStock());

        reorder = service.findAllItemsToReorder();
        assertEquals(2, reorder.size());
        assertTrue(reorder.stream().anyMatch(supplyItem -> supplyItem.getName().equals("Espresso Beans")));
        assertTrue(reorder.stream().anyMatch(supplyItem -> supplyItem.getName().equals("Milk")));
    }

}

