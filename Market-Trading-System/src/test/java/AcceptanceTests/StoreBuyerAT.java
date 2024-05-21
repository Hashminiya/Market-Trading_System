package AcceptanceTests;

import DomainLayer.Market.Store.Discount;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.InMemoryRepository;
import ServiceLayer.ServiceFactory;
import ServiceLayer.Store.StoreBuyerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoreBuyerAT {

    private StoreBuyerService storeBuyerService;
    private ServiceFactory serviceFactory;
    private IStoreFacade storeFacade;
    long STORE_ID;
    long ITEM_ID_1;
    long ITEM_ID_2;
    long ITEM_ID_3;
    String STORE_NAME = "storeName";
    @BeforeEach
    public void setUp() {
        serviceFactory = ServiceFactory.getServiceFactory();
        storeFacade = serviceFactory.getStoreFacade();
        storeBuyerService = StoreBuyerService.getInstance(storeFacade);

        try {

            STORE_ID = storeFacade.createStore("founderId", STORE_NAME, "storeDescription", new InMemoryRepository<Long, Discount>());
            ITEM_ID_1 = storeFacade.addItemToStore("founderId", STORE_ID, "Laptop", 100,7,"High-end laptop",  List.of("Electronics"));
            ITEM_ID_2 = storeFacade.addItemToStore("founderId", STORE_ID, "Phone", 150,10,"Smartphone", List.of("Electronics"));
            ITEM_ID_3 = storeFacade.addItemToStore("founderId", STORE_ID, "Headphones", 50,25,"Noise-cancelling headphones", List.of("Electronics", "Audio"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetAllProductsInfoByStore() {
        Response response = storeBuyerService.getAllProductsInfoByStore(1L);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Map<Long, String> expectedProducts = new HashMap<>();
        expectedProducts.put(ITEM_ID_1, "Laptop");
        expectedProducts.put(ITEM_ID_2, "Phone");
        expectedProducts.put(ITEM_ID_3, "Headphones");

        assertEquals(expectedProducts, response.getEntity());
    }

    @Test
    public void testGetAllStoreInfo() {
        Response response = storeBuyerService.getAllStoreInfo();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Map<Long, String> expectedStores = new HashMap<>();
        expectedStores.put(STORE_ID, STORE_NAME);

        assertEquals(expectedStores, response.getEntity());
    }

    @Test
    public void testSearchInStoreByCategory() {
        Response response = storeBuyerService.searchInStoreByCategory(STORE_ID, "Electronics");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Map<Long, String> expectedProducts = new HashMap<>();
        expectedProducts.put(ITEM_ID_1, "Laptop");
        expectedProducts.put(ITEM_ID_2, "Phone");
        expectedProducts.put(ITEM_ID_3, "Headphones");

        assertEquals(expectedProducts, response.getEntity());
    }

    @Test
    public void testSearchInStoreByKeyWord() {
        Response response = storeBuyerService.searchInStoreByKeyWord(STORE_ID, "Laptop");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Map<Long, String> expectedProducts = new HashMap<>();
        expectedProducts.put(ITEM_ID_1, "Laptop");

        assertEquals(expectedProducts, response.getEntity());
    }

    @Test
    public void testSearchInStoreByKeyWordAndCategory() {
        Response response = storeBuyerService.searchInStoreByKeyWordAndCategory(STORE_ID, "Electronics", "Laptop");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Map<Long, String> expectedProducts = new HashMap<>();
        expectedProducts.put(ITEM_ID_1, "Laptop");

        assertEquals(expectedProducts, response.getEntity());
    }

    @Test
    public void testSearchGenerallyByCategory() {
        Response response = storeBuyerService.searchGenerallyByCategory("Electronics");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Map<Long, String> expectedProducts = new HashMap<>();
        expectedProducts.put(ITEM_ID_1, "Laptop");
        expectedProducts.put(ITEM_ID_2, "Phone");
        expectedProducts.put(ITEM_ID_3, "Headphones");

        assertEquals(expectedProducts, response.getEntity());
    }

    @Test
    public void testSearchGenerallyByKeyWord() {
        Response response = storeBuyerService.searchGenerallyByKeyWord("Laptop");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Map<Long, String> expectedProducts = new HashMap<>();
        expectedProducts.put(ITEM_ID_1, "Laptop");

        assertEquals(expectedProducts, response.getEntity());
    }

    @Test
    public void testSearchGenerallyByKeyWordAndCategory() {
        Response response = storeBuyerService.searchGenerallyByKeyWordAndCategory("Electronics", "Laptop");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        Map<Long, String> expectedProducts = new HashMap<>();
        expectedProducts.put(ITEM_ID_1, "Laptop");

        assertEquals(expectedProducts, response.getEntity());
    }
}