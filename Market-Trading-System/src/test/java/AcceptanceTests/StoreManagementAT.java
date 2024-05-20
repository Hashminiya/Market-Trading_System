package AcceptanceTests;

import DomainLayer.Market.Purchase.PurchaseController;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.InMemoryRepository;
import DomainLayer.Market.Store.Discount;
import ServiceLayer.Store.StoreManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoreManagementAT {

    private String FOUNDER_ID = "founderId";
    private String SOTRE_NAME = "storeName";
    private String STORE_DESCRIPTION= "storeDescription";
    private IStoreFacade storeFacade;
    private StoreManagementService storeManagementService;
    private IRepository<Long, Discount> discountRepository;

    @BeforeEach
    public void setUp() {
        discountRepository = new InMemoryRepository<Long, Discount>();
        storeFacade = new StoreController(new InMemoryRepository<>(), new PurchaseController(), new UserController(new InMemoryRepository<>()));
        storeManagementService = new StoreManagementService(storeFacade);
    }

    @Test
    public void testCreateStore() {
        Response response = storeManagementService.createStore(FOUNDER_ID, SOTRE_NAME, STORE_DESCRIPTION, discountRepository);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testAddItemToStore() {
        storeManagementService.createStore(FOUNDER_ID, SOTRE_NAME, STORE_DESCRIPTION, discountRepository);
        Response response = storeManagementService.addItemToStore("userId", 1L, "itemName", "description", 10.0, 100, List.of("category"));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testUpdateItem() {
        storeManagementService.createStore(FOUNDER_ID, SOTRE_NAME, STORE_DESCRIPTION, discountRepository);
        storeManagementService.addItemToStore("userId", 1L, "itemName", "description", 10.0, 100, List.of("category"));
        Response response = storeManagementService.updateItem("userId", 1L, 1L, "newName", 20.0, 200);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testDeleteItem() {
        storeManagementService.createStore(FOUNDER_ID, SOTRE_NAME, STORE_DESCRIPTION, discountRepository);
        storeManagementService.addItemToStore("userId", 1L, "itemName", "description", 10.0, 100, List.of("category"));
        Response response = storeManagementService.deleteItem("userId", 1L, 1L);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testChangeStorePolicy() {
        storeManagementService.createStore(FOUNDER_ID, SOTRE_NAME, STORE_DESCRIPTION, discountRepository);
        Response response = storeManagementService.changeStorePolicy("userId", 1L);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testChangeDiscountType() {
        storeManagementService.createStore(FOUNDER_ID, SOTRE_NAME, STORE_DESCRIPTION, discountRepository);
        Response response = storeManagementService.changeDiscountType("userId", 1L, "newType");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testRemoveStore() {
        storeManagementService.createStore(FOUNDER_ID, SOTRE_NAME, STORE_DESCRIPTION, discountRepository);
        Response response = storeManagementService.removeStore("userId", 1L);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testViewManagementInfo() {
        storeManagementService.createStore(FOUNDER_ID, SOTRE_NAME, STORE_DESCRIPTION, discountRepository);
        Response response = storeManagementService.viewManagmentInfo("userId", 1L);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        // Add more assertions to verify the content of the response
    }

    @Test
    public void testViewPurchasesHistory() {
        storeManagementService.createStore(FOUNDER_ID, SOTRE_NAME, STORE_DESCRIPTION, discountRepository);
        Response response = storeManagementService.viewPurchasesHistory("userId", 1L);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testAssignStoreOwner() {
        storeManagementService.createStore(FOUNDER_ID, SOTRE_NAME, STORE_DESCRIPTION, discountRepository);
        Response response = storeManagementService.assignStoreOwner("userId", 1L, "newOwnerId");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testAssignStoreManager() {
        storeManagementService.createStore(FOUNDER_ID, SOTRE_NAME, STORE_DESCRIPTION, discountRepository);
        Response response = storeManagementService.assignStoreManager("userId", 1L, "newManagerId");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
