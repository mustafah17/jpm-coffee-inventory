package com.jpm.coffeeshop.supply.util;

import com.jpm.coffeeshop.supply.domain.ItemCategory;
import com.jpm.coffeeshop.supply.domain.SupplyItem;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvSupplyItemParserTest {

    @Test
    void parseValidCsvFile() throws Exception {
        String csv = """
                name,category,stock,reorderLevel,supplierName,supplierContact
                Sugar,SUGAR,30,5,testSupplier,07777
                Milk,MILK,15,7,testSupplier,07777
                """;

        MockMultipartFile file = new MockMultipartFile("file", csv.getBytes());

        List<SupplyItem> items = CsvSupplyItemParser.parseCsv(file);

        assertEquals(2, items.size());
        assertEquals("Sugar", items.get(0).getName());
        assertEquals(ItemCategory.SUGAR, items.get(0).getCategory());
        assertEquals("testSupplier", items.get(0).getSupplier().name());
        assertEquals("Milk", items.get(1).getName());
        assertEquals(ItemCategory.MILK, items.get(1).getCategory());
        assertEquals("07777", items.get(1).getSupplier().contact());
    }

    @Test
    void invalidCsvFile() {
        String csv = """
                name,category,stock,reorderLevel,supplierName,supplierContact
                Sugar,NOT_A_CATEGORY,30,5,testSupplier,07777
                Milk,MILK,15,7,testSupplier,07777
                """;

        MockMultipartFile file = new MockMultipartFile("badFile", csv.getBytes());

        assertThrows(Exception.class, () -> CsvSupplyItemParser.parseCsv(file));
    }

}
