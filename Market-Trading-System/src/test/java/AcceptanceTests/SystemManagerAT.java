package AcceptanceTests;

import API.Utils.SpringContext;
import DomainLayer.Market.Purchase.PurchaseController;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;
import ServiceLayer.Market.ISystemManagerService;
import javax.ws.rs.core.Response;

import ServiceLayer.Market.SystemManagerService;
import ServiceLayer.ServiceFactory;
import ServiceLayer.User.UserService;
import SetUp.ApplicationTest;
import SetUp.cleanUpDB;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ApplicationTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)

public class SystemManagerAT {

   private static final String ADMIN_TOKEN = "adminToken"; // Replace with actual admin token

    private static SystemManagerService systemManagerService;
    private static UserService userService;
    private static final String ADMIN_USER_NAME ="admin" ;
    private static final String ADMIN_PASSWORD = "admin";
    private static String MANAGER_TOKEN;

    @BeforeAll
    public static void setUp() {
        SpringContext.getBean(StoreController.class).setUserFacade(SpringContext.getBean(UserController.class));
        systemManagerService = SpringContext.getBean(SystemManagerService.class);
        userService = SpringContext.getBean(UserService.class);
        SpringContext.getBean(PurchaseController.class).setUserFacade(SpringContext.getBean(UserController.class));
        userService.register(ADMIN_USER_NAME, ADMIN_PASSWORD, 25);
        MANAGER_TOKEN = userService.login(ADMIN_USER_NAME, ADMIN_PASSWORD).getBody();
    }

    @AfterAll
    public static void tearDown() {
        if(!cleanUpDB.clearDB()) {
            systemManagerService.clear();
            userService.clear();
        }
    }

    @Test
    @Order(1)
    public void testViewMarketPurchaseHistory() {
        ResponseEntity<?> response = systemManagerService.viewMarketPurchaseHistory(MANAGER_TOKEN);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(2)
    public void testCloseStore() {
        //Place holder test- this functionality is not required right now

        assert (true);
    }

    @Test
    @Order(3)
    public void testCloseMarket() {
        //Place holder test- this functionality is not required right now
        assert (true);
    }
}
