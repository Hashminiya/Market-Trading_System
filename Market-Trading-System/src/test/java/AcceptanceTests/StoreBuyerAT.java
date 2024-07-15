package AcceptanceTests;

import API.Utils.SpringContext;
import DomainLayer.Market.Store.Discount.Discount;
import DomainLayer.Market.Store.Discount.IDiscount;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;
import ServiceLayer.ServiceFactory;
import ServiceLayer.Store.StoreBuyerService;
import ServiceLayer.User.IUserService;
import ServiceLayer.User.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import SetUp.ApplicationTest;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ApplicationTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class StoreBuyerAT {

    private static StoreBuyerService storeBuyerService;
    private static UserService userService;
    static long STORE_ID;
    static long ITEM_ID_1;
    static long ITEM_ID_2;
    static long ITEM_ID_3;
    static String STORE_NAME = "storeName";
    private static IStoreFacade storeFacade;

    @BeforeAll
    static void setUp() {

        SpringContext.getBean(StoreController.class).setUserFacade(SpringContext.getBean(UserController.class));
        storeFacade = SpringContext.getBean(IStoreFacade.class);
        storeBuyerService = SpringContext.getBean(StoreBuyerService.class);
        userService = SpringContext.getBean(UserService.class);
        userService.clear();
        try {
            userService.register("founderId","12345678",30);
            STORE_ID = storeFacade.createStore("founderId", STORE_NAME, "Store for electronic devices");
            ITEM_ID_1 = storeFacade.addItemToStore("founderId", STORE_ID, "Laptop", 100,7,"High-end laptop",  List.of("Electronics"));
            ITEM_ID_2 = storeFacade.addItemToStore("founderId", STORE_ID, "Phone", 150,10,"Smartphone", List.of("Electronics"));
            ITEM_ID_3 = storeFacade.addItemToStore("founderId", STORE_ID, "Headphones", 50,25,"Noise-cancelling headphones", List.of("Electronics", "Audio"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_getAllProductsInfoByStore_should_return_ok_status_and_valid_items() {
        ResponseEntity<?> response = storeBuyerService.getAllProductsInfoByStore(STORE_ID);
        assertEquals(200, response.getStatusCode().value(), "Expected status code 200 OK");

        Map<Long, String> expectedProducts = new HashMap<>();
        expectedProducts.put(ITEM_ID_1, "Laptop");
        expectedProducts.put(ITEM_ID_2, "Phone");
        expectedProducts.put(ITEM_ID_3, "Headphones");

        List<Item> expectedItems = new ArrayList<>();
        for (Map.Entry<Long, String> entry : expectedProducts.entrySet()) {
            Item item = storeFacade.getItem(entry.getKey());
            expectedItems.add(item);
        }

        List<Item> actualItems = (List<Item>) response.getBody();

        // Ensure the lists have the same size
        assertEquals(expectedItems.size(), actualItems.size(), "The number of items should match");

        // Check if each item's name matches
        for (int i = 0; i < expectedItems.size(); i++) {
            assertEquals(expectedItems.get(i).getName(), actualItems.get(i).getName(),
                    "Item name should match for item at index: " + i);
        }
    }

    @Test
    public void test_getAllStoreInfo_should_return_ok_status_and_valid_stores() {
        ResponseEntity<?> response = storeBuyerService.getAllStoreInfo();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());

        Map<Long, String> expectedStores = new HashMap<>();
        expectedStores.put(STORE_ID, STORE_NAME);

        assertEquals(expectedStores, response.getBody());
    }

    @Test
    public void test_searchInStoreByCategory_should_return_ok_status_and_valid_items() {
        ResponseEntity<?> response = storeBuyerService.searchInStoreByCategory(STORE_ID, "Electronics");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());

        Map<Long, String> expectedProducts = new HashMap<>();
        expectedProducts.put(ITEM_ID_1, "Laptop");
        expectedProducts.put(ITEM_ID_2, "Phone");
        expectedProducts.put(ITEM_ID_3, "Headphones");

        assertEquals(expectedProducts, response.getBody());
    }

    @Test
    public void test_searchInStoreByKeyWord_should_return_ok_status_and_valid_items() {
        ResponseEntity<?> response = storeBuyerService.searchInStoreByKeyWord(STORE_ID, "Laptop");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());

        Map<Long, String> expectedProducts = new HashMap<>();
        expectedProducts.put(ITEM_ID_1, "Laptop");

        assertEquals(expectedProducts, response.getBody());
    }

    @Test
    public void test_searchInStoreByKeyWordAndCategory_should_return_ok_status_and_valid_items() {
        ResponseEntity<?> response = storeBuyerService.searchInStoreByKeyWordAndCategory(STORE_ID, "Electronics", "Laptop");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());

        Map<Long, String> expectedProducts = new HashMap<>();
        expectedProducts.put(ITEM_ID_1, "Laptop");

        assertEquals(expectedProducts, response.getBody());
    }

    @Test
    public void test_searchGenerallyByCategory_should_return_ok_status_and_valid_items() {
        ResponseEntity<?> response = storeBuyerService.searchGenerallyByCategory("Electronics");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());

        Map<Long, String> expectedProducts = new HashMap<>();
        expectedProducts.put(ITEM_ID_1, "Laptop");
        expectedProducts.put(ITEM_ID_2, "Phone");
        expectedProducts.put(ITEM_ID_3, "Headphones");

        assertEquals(expectedProducts, response.getBody());
    }

    @Test
    public void test_searchGenerallyByKeyWord_should_return_ok_status_and_valid_items() {
        ResponseEntity<?> response = storeBuyerService.searchGenerallyByKeyWord("Laptop");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());

        Map<Long, String> expectedProducts = new HashMap<>();
        expectedProducts.put(ITEM_ID_1, "Laptop");

        List<Item> items = new ArrayList<>();
        for (Map.Entry<Long, String> entry : expectedProducts.entrySet()) {
            Item item = storeFacade.getItem(entry.getKey());
            items.add(item);
        }
        assertEquals(items.get(0).getId(), ITEM_ID_1);
        assertEquals(items.get(0).getName(), "Laptop");

    }

    @Test
    public void test_searchGenerallyByKeyWordAndCategory_should_return_ok_status_and_valid_items() {
        ResponseEntity<?> response = storeBuyerService.searchGenerallyByKeyWordAndCategory("Electronics", "Laptop");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());

        Map<Long, String> expectedProducts = new HashMap<>();
        expectedProducts.put(ITEM_ID_1, "Laptop");

        assertEquals(expectedProducts, response.getBody());
    }
}