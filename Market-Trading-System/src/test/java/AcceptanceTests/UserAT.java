package AcceptanceTests;

import DomainLayer.Market.Util.InMemoryRepository;
import DomainLayer.Market.Util.StorePermission;
import ServiceLayer.ServiceFactory;
import ServiceLayer.Store.StoreManagementService;
import ServiceLayer.User.UserService;
import org.junit.jupiter.api.*;

import javax.ws.rs.core.Response;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserAT {

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
        serviceFactory = ServiceFactory.getServiceFactory();
        serviceFactory.initFactory();
        userService = serviceFactory.getUserService();
        storeSevice = serviceFactory.getStoreManagementService();
        userService.register(USERNAME1, PASSWORD, AGE);
        USERNAME1_TOKEN = (String) userService.login(USERNAME1, PASSWORD).getEntity();
        STOREID = (long) storeSevice.createStore(USERNAME1_TOKEN, "new test store- userAT", "description",new InMemoryRepository<>()).getEntity();
        ITEMID = (long) storeSevice.addItemToStore(USERNAME1_TOKEN, STOREID,"new item", "desctiprion",50,100, List.of("electronics")).getEntity();
    }
    @AfterAll
    public static void tearDown()
    {
        userService.login(USERNAME1,PASSWORD);
        storeSevice.removeStore(USERNAME1_TOKEN,STOREID);
    }

    @Test
    @Order(1)
    public void testGuestEntry() {
        Response response = userService.GuestEntry();
        GUEST_TOKEN = (String) response.getEntity();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(2)
    public void testGuestExit() {
        Response response = userService.GuestExit(GUEST_TOKEN);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(3)
    public void testRegister() {
        Response response = userService.register(USERNAME2, PASSWORD, AGE);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(4)
    public void testLogin() {
        Response response = userService.login(USERNAME2,PASSWORD);
        USERNAME2_TOKEN = (String) response.getEntity();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(5)
    public void testLogout() {
        Response response = userService.logout(USERNAME2_TOKEN);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(6)
    public void testViewShoppingCart() {
        Response response2 = userService.viewShoppingCart(USERNAME1_TOKEN);
        assertEquals(Response.Status.OK.getStatusCode(), response2.getStatus());
        assertEquals("", response2.getEntity());
    }

    @Test
    @Order(7)
    public void testAddPermission() {
        Response response = userService.addPermission(USERNAME1_TOKEN, USERNAME2,STOREID, StorePermission.REMOVE_STORE.toString());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(8)
    public void testRemovePermission() {
        Response response = userService.removePermission(USERNAME1_TOKEN, USERNAME2,STOREID, StorePermission.REMOVE_STORE.toString());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(9)
    public void testAddItemToBasket() {
        Response response = userService.addItemToBasket(USERNAME1_TOKEN, STOREID, ITEMID, 3);
        BASKET_ID = (long) response.getEntity();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(10)
    public void testModifyShoppingCart() {
        Response response = userService.modifyShoppingCart(USERNAME1_TOKEN, BASKET_ID, ITEMID, 5);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(11)
    public void testCheckoutShoppingCart() {
        Response response = userService.checkoutShoppingCart(USERNAME1_TOKEN, CREDIT_CARD, EXPIRY_DATE, CVV, DISCOUNT_CODE);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
