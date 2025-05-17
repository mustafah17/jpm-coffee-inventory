package com.jpm.springboot.coffee_shop.util;

import com.jpm.springboot.coffee_shop.domain.ItemCategory;
import com.jpm.springboot.coffee_shop.domain.Supplier;
import com.jpm.springboot.coffee_shop.domain.SupplyItem;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CsvSupplyItemParser {

    // Returns a list of items ready for bulk saving
    public static List<SupplyItem> parseCsv(MultipartFile file) throws IOException {
        List<SupplyItem> items = new ArrayList<>();
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader("name", "category", "stock", "reorderLevel", "supplierName", "supplierContact")
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .build();
        try (
                Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                CSVParser parser = new CSVParser(reader, format)
        ) {

            for (CSVRecord record : parser) {
                String name = record.get("name");
                ItemCategory itemCategory = ItemCategory.valueOf(record.get("category"));
                int stock = Integer.parseInt(record.get("stock"));
                int reorderLevel = Integer.parseInt(record.get("reorderLevel"));
                String supplierName = record.get("supplierName");
                String supplierContact = record.get("supplierContact");

                Supplier supplier = new Supplier(supplierName, supplierContact);
                SupplyItem item = new SupplyItem(null, name, itemCategory, stock, reorderLevel, supplier);
                items.add(item);
            }
        }
        return items;
    }
}
