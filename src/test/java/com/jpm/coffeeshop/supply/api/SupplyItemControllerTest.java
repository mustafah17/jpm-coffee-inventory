package com.jpm.coffeeshop.supply.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpm.coffeeshop.supply.domain.ItemCategory;
import com.jpm.coffeeshop.supply.domain.Supplier;
import com.jpm.coffeeshop.supply.domain.SupplyItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SupplyItemControllerTest {

    static final String API_SUPPLY_ITEMS = "/api/supply-items";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private SupplyItem sampleItem() {
        return new SupplyItem(
                null,
                "Espresso Beans",
                ItemCategory.COFFEE_BEANS,
                10,
                5,
                new Supplier("sampleSupplier", "07777"));
    }

    @Test
    void testCreateGetDeleteSupplyItem() throws Exception {
        // CREATE
        String json = objectMapper.writeValueAsString(sampleItem());
        String responseBody = mockMvc.perform(post(API_SUPPLY_ITEMS).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemID").exists())
                .andReturn().getResponse().getContentAsString();

        // Extract ID
        SupplyItem created = objectMapper.readValue(responseBody, SupplyItem.class);
        String id = "/" + created.getItemID();

        // GET by ID
        mockMvc.perform(get(API_SUPPLY_ITEMS + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Espresso Beans"));

        // DELETE
        mockMvc.perform(delete(API_SUPPLY_ITEMS + id)).andExpect(status().isNoContent());

        // GET after Delete
        mockMvc.perform(get(API_SUPPLY_ITEMS + id)).andExpect(status().isNotFound());
    }

    @Test
    void testSearchByName() throws Exception {
        String json = objectMapper.writeValueAsString(sampleItem());
        mockMvc.perform(post(API_SUPPLY_ITEMS).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(get(API_SUPPLY_ITEMS + "/search").param("name", "Espresso"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Espresso Beans"));
    }

    @Test
    void testUpdateQuantity() throws Exception {
        // Create item
        String json = objectMapper.writeValueAsString(sampleItem());
        String responseBody = mockMvc.perform(post(API_SUPPLY_ITEMS).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        SupplyItem created = objectMapper.readValue(responseBody, SupplyItem.class);
        String id = "/" + created.getItemID();

        // PATCH quantity
        mockMvc.perform(patch(API_SUPPLY_ITEMS + id + "/quantity").param("quantity", "12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(12));
    }

    @Test
    void testReorder() throws Exception {
        String json = objectMapper.writeValueAsString(sampleItem());
        mockMvc.perform(post(API_SUPPLY_ITEMS).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        // Check reorder list empty
        mockMvc.perform(get(API_SUPPLY_ITEMS + "/to-reorder"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));

        // Create an item that needs to be reordered
        mockMvc.perform(post(API_SUPPLY_ITEMS).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new SupplyItem(null, "Milk", ItemCategory.MILK, 5, 5, new Supplier("sampleSupplier", "07777")))))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        // Check reorder list
        mockMvc.perform(get(API_SUPPLY_ITEMS + "/to-reorder"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Milk"));
    }

    @Test
    void testImportFromCsv() throws Exception {
        String csv = """
                name,category,stock,reorderLevel,supplierName,supplierContact
                Sugar,SUGAR,30,5,testSupplier,07777
                Milk,MILK,15,7,testSupplier,07777
                """;

        MockMultipartFile file = new MockMultipartFile("file", csv.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(API_SUPPLY_ITEMS + "/import").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Sugar"))
                .andExpect(jsonPath("$[1].name").value("Milk"));
    }
}
