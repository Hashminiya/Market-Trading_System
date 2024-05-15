package AcceptanceTests;

import DomainLayer.Market.Store.IStoreFacade;
import ServiceLayer.Store.StoreBuyerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import javax.ws.rs.core.Response;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StoreBuyerAT {
    final long category = 1;
    final long storeId = 1;
    final String keyWord = "testKeyword";
    final String name = "testItemName";

    @Mock
    private IStoreFacade storeFacadeMock;

    @InjectMocks
    private StoreBuyerService storeBuyerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 2.1
    @Test
    void testGetAllProductsInfoByStore() {
        HashMap<Long, HashMap<String, String>> expectedResult = new HashMap<>();

        when(storeFacadeMock.getAllProductsInfoByStore(storeId)).thenReturn(expectedResult);

        Response response = storeBuyerService.getAllProductsInfoByStore(storeId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.1
    @Test
    void testGetAllStoreInfo() {
        HashMap<Long, HashMap<String, String>> expectedResult = new HashMap<>();

        when(storeFacadeMock.getAllStoreInfo(storeId)).thenReturn(expectedResult);

        Response response = storeBuyerService.getAllStoreInfo(storeId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.2a + 2.2b
    @Test
    void testSearchStoreByCategory() {
        HashMap<Long, String> expectedResult = new HashMap<>();

        when(storeFacadeMock.searchStoreByCategory(category)).thenReturn(expectedResult);

        Response response = storeBuyerService.searchStoreByCategory(category);


        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.2a + 2.2b
    @Test
    void testSearchItemByCategory() {
        HashMap<Long, String> expectedResult = new HashMap<>();

        when(storeFacadeMock.searchItemByCategory(category)).thenReturn(expectedResult);

        Response response = storeBuyerService.searchItemByCategory(category);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.2a + 2.2b
    @Test
    void testSearchStoreByKeyWord() {
        HashMap<Long, String> expectedResult = new HashMap<>();

        when(storeFacadeMock.searchStoreByKeyWord(keyWord)).thenReturn(expectedResult);

        Response response = storeBuyerService.searchStoreByKeyWord(keyWord);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.2a + 2.2b
    @Test
    void testSearchItemByKeyWord() {
        HashMap<Long, String> expectedResult = new HashMap<>();

        when(storeFacadeMock.searchItemByKeyWord(keyWord)).thenReturn(expectedResult);

        Response response = storeBuyerService.searchItemByKeyWord(keyWord);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.2a + 2.2b
    @Test
    void testSearchStoreByName() {
        HashMap<Long, String> expectedResult = new HashMap<>();

        when(storeFacadeMock.searchStoreByName(name)).thenReturn(expectedResult);

        Response response = storeBuyerService.searchStoreByName(name);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.2a + 2.2b
    @Test
    void testSearchItemByName() {
        HashMap<Long, String> expectedResult = new HashMap<>();

        when(storeFacadeMock.searchItemByName(name)).thenReturn(expectedResult);

        Response response = storeBuyerService.searchItemByName(name);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.3 - TODO : Implement..
    @Test
    void testSavingItemsInShoppingCat()
    {

    }
}
