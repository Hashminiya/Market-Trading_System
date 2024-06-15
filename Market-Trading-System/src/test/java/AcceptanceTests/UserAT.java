package AcceptanceTests;

import DomainLayer.Market.User.UserController;
import DomainLayer.Market.Util.InMemoryRepository;
import DomainLayer.Market.Util.StorePermission;
import ServiceLayer.ServiceFactory;
import ServiceLayer.Store.StoreManagementService;
import ServiceLayer.User.UserService;
import org.junit.jupiter.api.*;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserAT {
    private static final String ADMIN_USER_NAME ="SystemManager" ;
    private static final String ADMIN_PASSWORD = "SystemManagerPassword";
    private static final String USERNAME1 = "testUser";
    private static final String USERNAME2 = "testUser2";
    private static final String PASSWORD = "password123";
    private static final int AGE = 25;
    private static final String CREDIT_CARD = "1234-5678-9876-5432";
    private static final Date EXPIRY_DATE = new Date(); // Set an appropriate expiry date
    private static final String CVV = "123";
    private static final String DISCOUNT_CODE = "DISCOUNT10";
    private static long ITEMID;
    private static long BASKET_ID;
    private static long STOREID;
    private static String USERNAME1_TOKEN;
    private static String USERNAME2_TOKEN;
    private static String GUEST_TOKEN;
    private static UserService userService;
    private static ServiceFactory serviceFactory;
    private static StoreManagementService storeSevice;

    @BeforeAll
    public static void setUp() {
        SetUp.setUp();
        serviceFactory = ServiceFactory.getServiceFactory();
        userService = serviceFactory.getUserService();
        storeSevice = serviceFactory.getStoreManagementService();
        userService.register(USERNAME1, PASSWORD, AGE);
        USERNAME1_TOKEN = userService.login(USERNAME1, PASSWORD).getBody();
        STOREID = (Long) storeSevice.createStore(USERNAME1_TOKEN, "new test store- userAT", "description").getBody();
        ITEMID = (Long)  storeSevice.addItemToStore(USERNAME1_TOKEN, STOREID,"new item", "desctiprion",50,100, List.of("electronics")).getBody();
    }

    @AfterAll
    public static void tearDown() throws Exception
    {
        //userService.login(USERNAME1,PASSWORD);
        //storeSevice.removeStore(USERNAME1_TOKEN,STOREID);
        serviceFactory.clear();
        //resetUserControllerInstance();

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
        ResponseEntity<String> response = userService.register(USERNAME2, PASSWORD, AGE);
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
        ResponseEntity<String> response = userService.addPermission(USERNAME1_TOKEN, USERNAME2,STOREID, StorePermission.REMOVE_STORE.toString());
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
        //ResponseEntity<String> response1 = userService.addItemToBasket(USERNAME1_TOKEN, STOREID, ITEMID, 3);
        ResponseEntity<String> response = userService.checkoutShoppingCart(USERNAME1_TOKEN, CREDIT_CARD, EXPIRY_DATE, CVV, DISCOUNT_CODE);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode().value());
    }
}
