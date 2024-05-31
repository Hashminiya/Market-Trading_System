package AcceptanceTests;

import DomainLayer.Market.Purchase.PurchaseController;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.InMemoryRepository;
import DomainLayer.Market.Store.Discount;
import ServiceLayer.ServiceFactory;
import ServiceLayer.Store.StoreManagementService;
import ServiceLayer.User.UserService;
import org.junit.jupiter.api.*;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoreManagementAT {

    private static final String FOUNDER_ID = "founderId";
    private static final String PASSWORD = "12345678";
    private static final int AGE = 30;
    private static final String STORE_NAME = "storeName";
    private static final String STORE_DESCRIPTION = "storeDescription";
    private static final String MANAGER_ID = "ManagerId";
    private static String TOKEN;
    private static long STORE_ID;
    private static IStoreFacade storeFacade;
    private static StoreManagementService storeManagementService;
    private static IRepository<Long, Discount> discountRepository;
    private static ServiceFactory serviceFactory;
    private static UserService userService;

    @BeforeAll
    public static void setUp() {
        serviceFactory = ServiceFactory.getServiceFactory();
        serviceFactory.initFactory();
        storeFacade = serviceFactory.getStoreFacade();
        discountRepository = new InMemoryRepository<>();
        storeManagementService = serviceFactory.getStoreManagementService();
        storeManagementService.setUserFacade(serviceFactory.getUserFacade());
        userService = serviceFactory.getUserService();
        userService.register(FOUNDER_ID,PASSWORD,AGE);
        userService.register(MANAGER_ID,PASSWORD,AGE);
        Response response1 = userService.login(FOUNDER_ID, PASSWORD);
        TOKEN = (String) response1.getEntity();
        Response response = storeManagementService.createStore(TOKEN, STORE_NAME, STORE_DESCRIPTION, discountRepository);
        STORE_ID = (long) response.getEntity();
    }

    @Test
    @Order(1)
    public void testCreateStore() {
        // Store creation is handled in setUp, no need to create it here again
        Response response = storeManagementService.createStore(TOKEN, "newStoreName", "newStoreDescription", discountRepository);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(2)
    public void testAddItemToStore() {
        Response response = storeManagementService.addItemToStore(TOKEN, STORE_ID, "itemName", "description", 10.0, 100, List.of("category"));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(3)
    public void testUpdateItem() {
        long item_id = (long) storeManagementService.addItemToStore(TOKEN, STORE_ID, "itemName", "description", 10.0, 100, List.of("category")).getEntity();
        Response response = storeManagementService.updateItem(TOKEN, STORE_ID, item_id, "newName", 20.0, 200);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(4)
    public void testDeleteItem() {
        long item_id = (long) storeManagementService.addItemToStore(TOKEN, STORE_ID, "itemName", "description", 10.0, 100, List.of("category")).getEntity();
        Response response = storeManagementService.deleteItem(TOKEN, STORE_ID, item_id);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(5)
    public void testChangeStorePolicy() {
        Response response = storeManagementService.changeStorePolicy(TOKEN, STORE_ID);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(6)
    public void testChangeDiscountType() {
        Response response = storeManagementService.changeDiscountType(TOKEN, STORE_ID, "newType");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(11)
    public void testRemoveStore() {
        Response response = storeManagementService.removeStore(TOKEN, STORE_ID);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(7)
    public void testViewManagementInfo() {
        Response response = storeManagementService.viewManagmentInfo(TOKEN, STORE_ID);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        // Add more assertions to verify the content of the response
    }

    @Test
    @Order(8)
    public void testViewPurchasesHistory() {
        Response response = storeManagementService.viewPurchasesHistory(TOKEN, STORE_ID);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(9)
    public void testAssignStoreManager() {
        Response response = storeManagementService.assignStoreManager(TOKEN, STORE_ID, MANAGER_ID, Arrays.asList("VIEW_STORE_MANAGEMENT_INFO", "VIEW_PURCHASE_HISTORY", "VIEW_INVENTORY"));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(10)
    public void testAssignStoreOwner() {
        Response response = storeManagementService.assignStoreOwner(TOKEN, STORE_ID, MANAGER_ID);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
