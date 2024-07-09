//package AcceptanceTests;
//
//import ServiceLayer.Market.ISystemManagerService;
//import javax.ws.rs.core.Response;
//
//import ServiceLayer.ServiceFactory;
//import org.junit.jupiter.api.*;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class SystemManagerAT {
//
//   private static final String ADMIN_TOKEN = "adminToken"; // Replace with actual admin token
//
//    private static ISystemManagerService systemManagerService;
//    private static ServiceFactory serviceFactory;
//    private static final String ADMIN_USER_NAME ="SystemManager" ;
//    private static final String ADMIN_PASSWORD = "SystemManagerPassword";
//    private static String MANAGER_TOKEN;
//    @BeforeAll
//    public static void setUp() {
//        serviceFactory = ServiceFactory.getServiceFactory();
//        SetUp.setUp();
//        MANAGER_TOKEN = SetUp.ADMIN_TOKEN;
//        systemManagerService = serviceFactory.getSystemManagerService();
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        serviceFactory.clear();
//    }
//
//    @Test
//    @Order(1)
//    public void testViewMarketPurchaseHistory() {
//        ResponseEntity<?> response = systemManagerService.viewMarketPurchaseHistory(MANAGER_TOKEN);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    @Order(2)
//    public void testCloseStore() {
//        //Place holder test- this functionality is not required right now
//
//        assert (true);
//    }
//
//    @Test
//    @Order(3)
//    public void testCloseMarket() {
//        //Place holder test- this functionality is not required right now
//        assert (true);
//    }
//}
