package AcceptanceTests;

import DomainLayer.Market.Store.Discount.IDiscount;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.InMemoryRepository;
import DomainLayer.Market.Store.Discount.Discount;
import ServiceLayer.ServiceFactory;
import ServiceLayer.Store.StoreManagementService;
import ServiceLayer.User.UserService;
import org.junit.jupiter.api.*;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoreManagementAT{

    private static final String ADMIN_USER_NAME = "SystemManager";
    private static final String ADMIN_PASSWORD = "SystemManagerPassword";
    private static final String FOUNDER_ID = "founderId";
    private static final String PASSWORD = "12345678";
    private static final int AGE = 30;
    private static final String STORE_NAME = "storeName";
    private static final String STORE_DESCRIPTION = "storeDescription";
    private static final String MANAGER_ID = "ManagerId";
    private static  Long ITEM_ID;
    private static String TOKEN;
    private static long STORE_ID;
    private static StoreManagementService storeManagementService;
    private static IRepository<Long, IDiscount> discountRepository;
    private static UserService userService;

    @BeforeAll
    public static void setUp() {
        SetUp.setUp();
        ServiceFactory serviceFactory = ServiceFactory.getServiceFactory();

        discountRepository = new InMemoryRepository<>();
        storeManagementService = serviceFactory.getStoreManagementService();
        storeManagementService.setUserFacade(serviceFactory.getUserFacade());
        userService = serviceFactory.getUserService();
        userService.register(FOUNDER_ID, PASSWORD, AGE);
        userService.register(MANAGER_ID, PASSWORD, AGE);
        ResponseEntity<String> response1 = userService.login(FOUNDER_ID, PASSWORD);
        TOKEN = response1.getBody();
    }

    @AfterAll
    public static void tearDown() {
        storeManagementService.removeStore(TOKEN, STORE_ID);
    }

    @Test
    @Order(1)
    public void test_createStore_should_returnOkStatus_for_valid_parameters() {
        ResponseEntity<?> response = storeManagementService.createStore(TOKEN, "newStoreName", "newStoreDescription", discountRepository);
        STORE_ID =  (Long) response.getBody();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(2)
    public void test_addItemToStore_should_returnOkStatus_and_add_item_for_valid_store_and_valid_item() {
        ResponseEntity<?> response = storeManagementService.addItemToStore(TOKEN, STORE_ID, "itemName", "description", 10.0, 100, List.of("category"));
        ITEM_ID = (Long) response.getBody();
        assertEquals(200, response.getStatusCode());
        HashMap<Long, Integer> inventory = (HashMap<Long, Integer>) storeManagementService.viewInventory(TOKEN, STORE_ID).getBody();
        assertTrue(inventory.containsKey(ITEM_ID));
    }

    @Test
    @Order(3)
    public void test_updateItem_should_returnOkStatus_and_update_item_for_valid_store_and_exists_item() {
        ResponseEntity<?> response = storeManagementService.updateItem(TOKEN, STORE_ID, ITEM_ID, "newName", 20.0, 200);
        assertEquals(200, response.getStatusCode());
        HashMap<Long, Integer> inventory = (HashMap<Long, Integer>) storeManagementService.viewInventory(TOKEN, STORE_ID).getBody();
        assertTrue(inventory.containsKey(ITEM_ID) && inventory.get(ITEM_ID) == 200);
    }

    @Test
    @Order(4)
    public void test_deleteItem_should_returnOkStatus() {
        ResponseEntity<?> response = storeManagementService.deleteItem(TOKEN, STORE_ID, ITEM_ID);
        assertEquals(200, response.getStatusCode());
        HashMap<Long, Integer> inventory = (HashMap<Long, Integer>) storeManagementService.viewInventory(TOKEN, STORE_ID).getBody();
        assertFalse(inventory.containsKey(ITEM_ID));
    }

    @Test
    @Order(5)
    public void test_addDiscount_should_returnOkStatus_for_simple_discount() {
        String discountDetails = "{}";
        ResponseEntity<?> response = storeManagementService.addDiscount(TOKEN, STORE_ID, discountDetails);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(6)
    public void test_addDiscount_should_returnOkStatus_for_complex_discount() {
        String discountDetails = "{}";
        ResponseEntity<?> response = storeManagementService.addDiscount(TOKEN, STORE_ID, discountDetails);
        assertEquals(200, response.getStatusCode());
    }
    @Test
    @Order(7)
    public void test_addPolicy_should_returnOkStatus_for_simple_policy() {
        String policyDetails = "{}";
        ResponseEntity<?> response = storeManagementService.addPolicy(TOKEN, STORE_ID, policyDetails);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(8)
    public void test_addPolicy_should_returnOkStatus_for_complex_policy() {
        String policyDetails = "{}";
        ResponseEntity<?> response = storeManagementService.addPolicy(TOKEN, STORE_ID, policyDetails);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(9)
    public void test_viewManagementInfo_should_returnOkStatus() {
        ResponseEntity<?> response = storeManagementService.viewManagementInfo(TOKEN, STORE_ID);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(10)
    public void test_viewPurchasesHistory_should_returnOkStatus() {
        ResponseEntity<?> response = storeManagementService.viewPurchasesHistory(TOKEN, STORE_ID);
        assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(11)
    public void test_assignStoreManager_should_returnOkStatus() {
        ResponseEntity<?> response = storeManagementService.assignStoreManager(TOKEN, STORE_ID, MANAGER_ID, Arrays.asList("VIEW_STORE_MANAGEMENT_INFO", "VIEW_PURCHASE_HISTORY", "VIEW_INVENTORY"));
        assertEquals(200, response.getStatusCode());
        ResponseEntity<?> response2 = storeManagementService.viewManagementInfo(TOKEN, STORE_ID);
        assertTrue(((HashMap<String, List<String>>) response.getBody()).containsKey(MANAGER_ID));
    }

    @Test
    @Order(12)
    public void test_assignStoreOwner_should_returnOkStatus() {
        ResponseEntity<?> response = storeManagementService.assignStoreOwner(TOKEN, STORE_ID, MANAGER_ID);
        assertEquals(200, response.getStatusCode());
    }
    @Test
    @Order(13)
    public void test_removeStore_should_returnOkStatus() {
        ResponseEntity<?> response = storeManagementService.removeStore(TOKEN, STORE_ID);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
    }
}
