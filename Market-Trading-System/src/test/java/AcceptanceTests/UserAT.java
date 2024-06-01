package AcceptanceTests;

import DomainLayer.Market.Util.InMemoryRepository;
import DomainLayer.Market.Util.StorePermission;
import DomainLayer.Market.Util.UserPermission;
import ServiceLayer.ServiceFactory;
import ServiceLayer.Store.StoreManagementService;
import ServiceLayer.User.UserService;
import org.junit.jupiter.api.*;

import javax.ws.rs.core.Response;
import java.security.Permission;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserAT {

    private static final String USERNAME = "testUser";
    private static final String USERNAME2 = "testUser2";
    private static final String PASSWORD = "password123";
    private static final int AGE = 25;
    private static final String CREDIT_CARD = "1234-5678-9876-5432";
    private static final Date EXPIRY_DATE = new Date(); // Set an appropriate expiry date
    private static final String CVV = "123";
    private static final String DISCOUNT_CODE = "DISCOUNT10";
    private static long BASKET_ID;
    private static long STOREID;
    private static String TOKEN;
    private static UserService userService;
    private static ServiceFactory serviceFactory;
    private static StoreManagementService storeSevice;

    @BeforeAll
    public static void setUp() {
        serviceFactory = ServiceFactory.getServiceFactory();
        serviceFactory.initFactory();
        userService = serviceFactory.getUserService();
        storeSevice = serviceFactory.getStoreManagementService();

    }
    @AfterAll
    public static void tearDown()
    {
        storeSevice.removeStore(TOKEN,STOREID);
    }

    @Test
    @Order(1)
    public void testGuestEntry() {
        Response response = userService.GuestEntry();
        TOKEN = (String) response.getEntity();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(2)
    public void testGuestExit() {
        Response response = userService.GuestExit(TOKEN);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(3)
    public void testRegister() {
        Response response = userService.register(USERNAME, PASSWORD, AGE);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(4)
    public void testLogin() {
        userService.register(USERNAME2, PASSWORD, AGE);
        Response response = userService.login(USERNAME2,PASSWORD);
        TOKEN = (String) response.getEntity();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(5)
    public void testLogout() {
        Response response = userService.logout(TOKEN);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(6)
    public void testViewShoppingCart() {
        Response response1 = userService.login(USERNAME2,PASSWORD);
        TOKEN = (String) response1.getEntity();
        Response response2 = userService.viewShoppingCart(TOKEN);
        assertEquals(Response.Status.OK.getStatusCode(), response2.getStatus());
        assertEquals("", response2.getEntity());
    }

    @Test
    @Order(7)
    public void testAddPermission() {
        Response storeResponse = storeSevice.createStore(TOKEN, "myStore", "description", new InMemoryRepository<>());
        STOREID = (Long) storeResponse.getEntity();
        Response response = userService.addPermission(TOKEN, USERNAME,STOREID, StorePermission.REMOVE_STORE.toString());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(8)
    public void testRemovePermission() {
        Response response = userService.removePermission(TOKEN, USERNAME,STOREID, StorePermission.REMOVE_STORE.toString());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(9)
    public void testAddItemToBasket() {
        Response response = userService.addItemToBasket(TOKEN, 1L, 1L, 3);
        BASKET_ID = (long) response.getEntity();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(10)
    public void testModifyShoppingCart() {
        Response response = userService.modifyShoppingCart(TOKEN, BASKET_ID, 1L, 5);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(11)
    public void testCheckoutShoppingCart() {
        Response response = userService.checkoutShoppingCart(TOKEN, CREDIT_CARD, EXPIRY_DATE, CVV, DISCOUNT_CODE);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
