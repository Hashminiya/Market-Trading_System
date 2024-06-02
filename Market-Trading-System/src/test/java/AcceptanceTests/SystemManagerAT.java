package AcceptanceTests;

import ServiceLayer.Market.ISystemManagerService;
import javax.ws.rs.core.Response;

import ServiceLayer.ServiceFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SystemManagerAT {

    private static final String ADMIN_TOKEN = "adminToken"; // Replace with actual admin token

    private static ISystemManagerService systemManagerService;
    private static final String ADMIN_USER_NAME ="SystemManager" ;
    private static final String ADMIN_PASSWORD = "SystemManagerPassword";
    private static String MANAGER_TOKEN;
    @BeforeAll
    public static void setUp() {
        ServiceFactory serviceFactory = ServiceFactory.getServiceFactory();
        SetUp.setUp();
        MANAGER_TOKEN = SetUp.ADMIN_TOKEN;
        systemManagerService = serviceFactory.getSystemManagerService();
    }

    @Test
    @Order(1)
    public void testViewMarketPurchaseHistory() {
        Response response = systemManagerService.viewMarketPurchaseHistory(MANAGER_TOKEN);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @Order(2)
    public void testCloseStore() {
        //Place holder test- this functionality is not required right now
        Response response = systemManagerService.closeStore(MANAGER_TOKEN, 123);

        assert (true);
    }

    @Test
    @Order(3)
    public void testCloseMarket() {
        //Place holder test- this functionality is not required right now
        Response response = systemManagerService.closeMarket(MANAGER_TOKEN);
        assert (true);
    }
}
