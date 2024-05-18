package AcceptanceTests;

import DomainLayer.Market.Store.IStoreFacade;
import ServiceLayer.ServiceFactory;
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
    final long CATEGORY = 1;
    final long STORE_ID = 1;
    final String KEY_WORD = "testKeyword";
    final String NAME = "testItemName";

    @Mock
    private IStoreFacade storeFacadeMock;

    @InjectMocks
    private StoreBuyerService storeBuyerService;

    @BeforeEach
    void setUp() {
        ServiceFactory.initFactory();
        MockitoAnnotations.openMocks(this);
    }

    // 2.1
    @Test
    void testGetAllProductsInfoByStore() {
        HashMap<Long, HashMap<String, String>> expectedResult = new HashMap<>();

        when(storeFacadeMock.getAllProductsInfoByStore(STORE_ID)).thenReturn(expectedResult);

        Response response = storeBuyerService.getAllProductsInfoByStore(STORE_ID);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.1
    @Test
    void testGetAllStoreInfo() {
        HashMap<Long, HashMap<String, String>> expectedResult = new HashMap<>();

        when(storeFacadeMock.getAllStoreInfo(STORE_ID)).thenReturn(expectedResult);

        Response response = storeBuyerService.getAllStoreInfo(STORE_ID);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.2a + 2.2b
    @Test
    void testSearchStoreByCategory() {
        HashMap<Long, String> expectedResult = new HashMap<>();

        when(storeFacadeMock.searchStoreByCategory(CATEGORY)).thenReturn(expectedResult);

        Response response = storeBuyerService.searchStoreByCategory(CATEGORY);


        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.2a + 2.2b
    @Test
    void testSearchItemByCategory() {
        HashMap<Long, String> expectedResult = new HashMap<>();

        when(storeFacadeMock.searchItemByCategory(CATEGORY)).thenReturn(expectedResult);

        Response response = storeBuyerService.searchItemByCategory(CATEGORY);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.2a + 2.2b
    @Test
    void testSearchStoreByKeyWord() {
        HashMap<Long, String> expectedResult = new HashMap<>();

        when(storeFacadeMock.searchStoreByKeyWord(KEY_WORD)).thenReturn(expectedResult);

        Response response = storeBuyerService.searchStoreByKeyWord(KEY_WORD);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.2a + 2.2b
    @Test
    void testSearchItemByKeyWord() {
        HashMap<Long, String> expectedResult = new HashMap<>();

        when(storeFacadeMock.searchItemByKeyWord(KEY_WORD)).thenReturn(expectedResult);

        Response response = storeBuyerService.searchItemByKeyWord(KEY_WORD);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.2a + 2.2b
    @Test
    void testSearchStoreByName() {
        HashMap<Long, String> expectedResult = new HashMap<>();

        when(storeFacadeMock.searchStoreByName(NAME)).thenReturn(expectedResult);

        Response response = storeBuyerService.searchStoreByName(NAME);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.2a + 2.2b
    @Test
    void testSearchItemByName() {
        HashMap<Long, String> expectedResult = new HashMap<>();

        when(storeFacadeMock.searchItemByName(NAME)).thenReturn(expectedResult);

        Response response = storeBuyerService.searchItemByName(NAME);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedResult, response.getEntity());
    }

    // 2.3 - TODO : Implement..
    @Test
    void testSavingItemsInShoppingCat()
    {

    }

    //
    @Test
    void testParallelRequests()
    {
        // run few threads...

    }
}
