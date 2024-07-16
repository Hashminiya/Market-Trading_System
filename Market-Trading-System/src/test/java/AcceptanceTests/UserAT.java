package AcceptanceTests;

import API.Utils.SpringContext;
import DomainLayer.Market.Purchase.PaymentServiceProxy;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;
import DomainLayer.Market.Util.StorePermission;
import ServiceLayer.ServiceFactory;
import ServiceLayer.Store.StoreManagementService;
import ServiceLayer.User.UserService;
import SetUp.ApplicationTest;
import SetUp.cleanUpDB;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import javax.ws.rs.core.Response;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ApplicationTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserAT {
    private static final String ADMIN_USER_NAME ="admin" ;
    private static final String ADMIN_PASSWORD = "admin";
    private static final String USERNAME1 = "testUser";
    private static final String USERNAME2 = "testUser2";
    private static final String PASSWORD = "password123";
    private static final int AGE = 25;
    private static final int AGE_UNDER_18 = 15;
    private static final String CREDIT_CARD = "1234567898765432";
    private static final Date EXPIRY_DATE = new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000); // Set an appropriate expiry date
    private static final String CVV = "123";
    private static final String DISCOUNT_CODE = "DISCOUNT10";
    private static long ITEM2_ID;
    private static long STOREID2 ;
    private static long ITEMID_VODKA;
    private static long ITEMID;
    private static long BASKET_ID;
    private static long STOREID;
    private static String USERNAME1_TOKEN;
    private static String USERNAME2_TOKEN;
    private static String GUEST_TOKEN;
    private static UserService userService;
    private static StoreManagementService storeManagementSevice;
    private static String ALCOHOL_POLICY = "{\n" +
            "    \"@type\": \"AgeRestrictedPurchasePolicy\",\n" +
            "    \"name\": \"Alcohol 18 and above\",\n" +
            "    \"id\": 10002,\n" +
            "    \"minAge\": 18,\n"+
            "    \"items\": null,\n" +
            "    \"categories\": [\"alcohol\"],\n" +
            "    \"isStore\": false\n" +
            "}";

    @Mock
    private static PaymentServiceProxy paymentServiceProxy;

    @BeforeAll
    public static void setUp() {
        SpringContext.getBean(StoreController.class).setUserFacade(SpringContext.getBean(UserController.class));
        userService = SpringContext.getBean(UserService.class);
        storeManagementSevice = SpringContext.getBean(StoreManagementService.class);
//        SpringContext.getBean(StoreManagementService.class).setUserFacade(SpringContext.getBean(UserController.class));

        userService.register(USERNAME1, PASSWORD, AGE);
        USERNAME1_TOKEN = userService.login(USERNAME1, PASSWORD).getBody();
        STOREID = (Long) storeManagementSevice.createStore(USERNAME1_TOKEN, "new test store- userAT", "description").getBody();
        STOREID2 = (Long) storeManagementSevice.createStore(USERNAME1_TOKEN,"second store- userAT ", "descrption").getBody();
        ITEMID = (Long)  storeManagementSevice.addItemToStore(USERNAME1_TOKEN, STOREID,"new item", "desctiprion",50,100, List.of("electronics")).getBody();
        ITEMID_VODKA = (Long) storeManagementSevice.addItemToStore(USERNAME1_TOKEN, STOREID, "vodka", "alcoholic drink",100,50, List.of("alcohol")).getBody();
        ITEM2_ID = (Long)  storeManagementSevice.addItemToStore(USERNAME1_TOKEN, STOREID2,"new item", "desctiprion",50,15, List.of("electronics")).getBody();
    }

    @AfterAll
    public static void tearDown() {
        if(!cleanUpDB.clearDB()) {
            storeManagementSevice.clear();
            userService.clear();
        }
    }

    private static void resetUserControllerInstance() throws Exception {
        Field instance = UserController.class.getDeclaredField("userControllerInstance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    @Order(1)
    public void test_guestEntry_should_return_token_successfully() {
        ResponseEntity<String> response = userService.guestEntry();
        GUEST_TOKEN = response.getBody();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());
    }

    @Test
    @Order(2)
    public void test_guestExit_should_return_ok_status() {
        ResponseEntity<String> response = userService.guestExit(GUEST_TOKEN);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());
    }

    @Test
    @Order(3)
    public void test_register_should_return_ok_status() {
        ResponseEntity<String> response = userService.register(USERNAME2, PASSWORD, AGE_UNDER_18);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());
    }

    @Test
    @Order(4)
    public void test_login_should_return_ok_status() {
        ResponseEntity<String> response = userService.login(USERNAME2,PASSWORD);
        USERNAME2_TOKEN = response.getBody();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());
    }

    @Test
    @Order(5)
    public void test_logout_should_return_ok_status() {
        ResponseEntity<String> response = userService.logout(USERNAME2_TOKEN);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());
    }

    @Test
    @Order(6)
    public void test_viewShoppingCart_should_return_ok_status() {
        ResponseEntity<String> response2 = userService.viewShoppingCart(USERNAME1_TOKEN);
        assertEquals(Response.Status.OK.getStatusCode(), response2.getStatusCode().value());
        assertEquals("", response2.getBody());
    }

    @Test
    @Order(7)
    public void test_addPermission_should_return_ok_status() {
        List<String> permissions = new ArrayList<>();
        permissions.add("REMOVE_STORE");
        USERNAME2_TOKEN = userService.login(USERNAME2,PASSWORD).getBody();
        storeManagementSevice.assignStoreManager(USERNAME1_TOKEN, STOREID, USERNAME2,permissions);
        ResponseEntity<String> response = userService.addPermission(USERNAME1_TOKEN, USERNAME2,STOREID, StorePermission.REMOVE_STORE.toString());
        userService.logout(USERNAME2_TOKEN);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());
    }

    @Test
    @Order(8)
    public void test_removePermission_should_return_ok_status() {
        ResponseEntity<String> response = userService.removePermission(USERNAME1_TOKEN, USERNAME2,STOREID, StorePermission.REMOVE_STORE.toString());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());
    }

    @Test
    @Order(9)
    public void test_addItemToBasket_should_return_ok_status() {
        ResponseEntity<String> response = userService.addItemToBasket(USERNAME1_TOKEN, STOREID, ITEMID, 3);
        BASKET_ID = Long.parseLong(response.getBody());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());
    }

    @Test
    @Order(10)
    public void test_modifyShoppingCart_should_return_ok_status() {
        ResponseEntity<String> response = userService.modifyShoppingCart(USERNAME1_TOKEN, BASKET_ID, ITEMID, 5);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());
    }

    @Test
    @Order(11)
    public void test_checkoutShoppingCart_should_return_ok_status() {
        ResponseEntity<String> response = userService.checkoutShoppingCart(USERNAME1_TOKEN, CREDIT_CARD, EXPIRY_DATE, CVV, DISCOUNT_CODE);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());
        HashMap<Long, Integer> inventory = (HashMap<Long, Integer>)
                storeManagementSevice.viewInventory(USERNAME1_TOKEN,STOREID).getBody();
        assertEquals(95, inventory.get(ITEMID));
    }
    @Test
    @Order(12)
    public void test_checkoutShoppingCart_should_return_error_with_alcohol_item_user_under_18() {
        storeManagementSevice.addPolicy(USERNAME1_TOKEN, STOREID,ALCOHOL_POLICY);
        USERNAME2_TOKEN = userService.login(USERNAME2, PASSWORD).getBody();
        userService.addItemToBasket(USERNAME2_TOKEN, STOREID, ITEMID_VODKA, 1);
        ResponseEntity<String> response = userService.checkoutShoppingCart(USERNAME2_TOKEN, CREDIT_CARD, EXPIRY_DATE, CVV, DISCOUNT_CODE);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatusCode().value());
    }
    @Test
    @Order(13)
    public void test_checkoutShoppingCart_should_return_okResponse_and_update_inventory_with_2_baskets() {

        HashMap<Long, Integer> inventory_before_action_store1 = (HashMap<Long, Integer>)
                storeManagementSevice.viewInventory(USERNAME1_TOKEN,STOREID).getBody();
        HashMap<Long, Integer> inventory_before_action_store2 = (HashMap<Long, Integer>)
                storeManagementSevice.viewInventory(USERNAME1_TOKEN,STOREID2).getBody();

        userService.addItemToBasket(USERNAME1_TOKEN, STOREID, ITEMID_VODKA, 1);
        userService.addItemToBasket(USERNAME1_TOKEN, STOREID2, ITEM2_ID,10);

        ResponseEntity<String> response = userService.checkoutShoppingCart(USERNAME1_TOKEN, CREDIT_CARD, EXPIRY_DATE, CVV, DISCOUNT_CODE);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        HashMap<Long, Integer> inventory_after_action_store1 = (HashMap<Long, Integer>)
                storeManagementSevice.viewInventory(USERNAME1_TOKEN,STOREID).getBody();
        HashMap<Long, Integer> inventory_after_action_store2 = (HashMap<Long, Integer>)
                storeManagementSevice.viewInventory(USERNAME1_TOKEN,STOREID2).getBody();

        inventory_before_action_store1.put(ITEMID_VODKA,inventory_before_action_store1.get(ITEMID_VODKA) - 1);
        inventory_before_action_store2.put(ITEM2_ID, inventory_before_action_store2.get(ITEM2_ID)- 10);
        assertEquals(inventory_before_action_store1, inventory_after_action_store1);
        assertEquals(inventory_before_action_store2, inventory_after_action_store2);
    }
    @Test
    @Order(14)
    public void test_checkoutShoppingCart_should_return_error_with_false_from_credit_card_services() {
        paymentServiceProxy = mock(PaymentServiceProxy.class);
//        try {
//            when(paymentServiceProxy.chargeCreditCard(anyString(),any(),anyString(),anyDouble())).thenReturn(-1);
//        }
//        catch (Exception e){
//            fail();
//        }
        HashMap<Long, Integer> inventory_before_action = (HashMap<Long, Integer>)
                storeManagementSevice.viewInventory(USERNAME1_TOKEN,STOREID).getBody();

        userService.addItemToBasket(USERNAME1_TOKEN, STOREID, ITEMID_VODKA, 1);

        ResponseEntity<String> response = userService.checkoutShoppingCart(USERNAME1_TOKEN, "1234", EXPIRY_DATE, CVV, DISCOUNT_CODE);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatusCode().value());
        HashMap<Long, Integer> inventory_after_action = (HashMap<Long, Integer>)
                storeManagementSevice.viewInventory(USERNAME1_TOKEN,STOREID).getBody();
        assertEquals(inventory_before_action, inventory_after_action);
    }

}
