package AcceptanceTests;

import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.SystemManager;
import DomainLayer.Market.User.User;
import ServiceLayer.ServiceFactory;
import ServiceLayer.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SystemAT {
    // 1.1
    @Nested
    class InitializeTradingSystemTest {

        private ServiceFactory serviceFactory;
        private IUserFacade userFacade;

        private final String SYSTEM_MANAGER_NAME = "SystemManager";
        private final String SYSTEM_MANAGER_PW = "SystemManagerPassword";
        private final String SYSTEM_MANAGER_WRONG_PW = "SystemManagerWrongPassword";

        @Test
        public void testSuccessfulInitialization() {
            // Arrange
            try {
                // Init only the user facade
                serviceFactory = ServiceFactory.getServiceFactory();
                userFacade = serviceFactory.getUserFacade();

                // Login manager to system
                userFacade.login(SYSTEM_MANAGER_NAME, SYSTEM_MANAGER_PW);
                verify(any(User.class), times(1)).login();
                SystemManager systemManager = SystemManager.getInstance();

                // Act
                systemManager.InitializeTradingSystem();
                assertNotEquals(serviceFactory.getUserService(), null);
            } catch (Exception exp) {
                fail();
            }
        }

        @Test
        public void testFailedLoginInitialization() {
            try {
                // Arrange
                // Init only the user facade
                serviceFactory = ServiceFactory.getServiceFactory();
                userFacade = serviceFactory.getUserFacade();

                // Login manager to system
                userFacade.login(SYSTEM_MANAGER_NAME, SYSTEM_MANAGER_WRONG_PW);
                verify(any(User.class), times(0)).login();
                SystemManager systemManager = SystemManager.getInstance();

                // Act
                systemManager.InitializeTradingSystem();
                assertNull(serviceFactory.getUserService());
            } catch (Exception exp) {
                fail();
            }
        }


// 1.2 - if needed

        // 1.3
        @Nested
        class PaymentTest {

            @Test
            public void testSuccessfulPayment() {
//            // Arrange
//            MockUserService userService = new MockUserService();
//            userService.setUserLoggedIn(true);
//            MockShoppingCart shoppingCart = new MockShoppingCart(true); // Valid shopping cart
//            MockPaymentService paymentService = new MockPaymentService(true); // Successful payment
//            TradingSystem tradingSystem = new TradingSystem(userService, paymentService);
//
//            // Act
//            boolean result = tradingSystem.processPayment(shoppingCart);
//
//            // Assert
//            assertTrue(result);
//            assertTrue(paymentService.isPaymentSuccessful());
//            assertTrue(shoppingCart.isEmpty()); // Shopping cart emptied
            }

            @Test
            public void testFailedPaymentInvalidDetails() {
//            // Arrange
//            MockUserService userService = new MockUserService();
//            userService.setUserLoggedIn(true);
//            MockShoppingCart shoppingCart = new MockShoppingCart(true); // Valid shopping cart
//            MockPaymentService paymentService = new MockPaymentService(false); // Failed payment
//            TradingSystem tradingSystem = new TradingSystem(userService, paymentService);
//
//            // Act
//            boolean result = tradingSystem.processPayment(shoppingCart);
//
//            // Assert
//            assertFalse(result);
//            assertFalse(paymentService.isPaymentSuccessful());
//            assertTrue(shoppingCart.isNotEmpty()); // Shopping cart not emptied
            }

            @Test
            public void testPaymentExternalServiceFailure() {
//            // Arrange
//            MockUserService userService = new MockUserService();
//            userService.setUserLoggedIn(true);
//            MockShoppingCart shoppingCart = new MockShoppingCart(true); // Valid shopping cart
//            MockPaymentService paymentService = new MockPaymentService(true); // Payment service throws exception
//            TradingSystem tradingSystem = new TradingSystem(userService, paymentService);
//
//            // Act
//            boolean result = tradingSystem.processPayment(shoppingCart);
//
//            // Assert
//            assertFalse(result); // May need adjustment based on exception handling
            }

            // Add similar tests for other scenarios
        }

        // 1.4
        @Nested
        class DeliveryTest {

            @Test
            public void testSuccessfulDelivery() {
                // Arrange
//        MockPaymentService paymentService = new MockPaymentService(true); // Successful payment
//        MockUserService userService = new MockUserService();
//        userService.setUserLoggedIn(true);
//        MockShoppingCart shoppingCart = new MockShoppingCart(true); // Valid shopping cart
//        MockSupplyService supplyService = new MockSupplyService(true); // Successful delivery
//        TradingSystem tradingSystem = new TradingSystem(userService, paymentService, supplyService);
//
//        tradingSystem.processPayment(shoppingCart); // Simulate successful payment
//
//        // Act
//        boolean result = tradingSystem.initiateDelivery();
//
//        // Assert
//        assertTrue(result);
//        assertTrue(supplyService.isDeliveryConfirmed());
            }

            @Test
            public void testFailedDeliveryInvalidDetails() {
                // Arrange (Similar to successful delivery, with invalid user details)
//        MockPaymentService paymentService = new MockPaymentService(true); // Successful payment
//        MockUserService userService = new MockUserService();
//        userService.setUserLoggedIn(true);
//        MockShoppingCart shoppingCart = new MockShoppingCart(true); // Valid shopping cart
//        MockSupplyService supplyService = new MockSupplyService(false); // Failed delivery
//        TradingSystem tradingSystem = new TradingSystem(userService, paymentService, supplyService);
//
//        tradingSystem.processPayment(shoppingCart); // Simulate successful payment
//
//        // Act
//        boolean result = tradingSystem.initiateDelivery();
//
//        // Assert
//        assertFalse(result);
//        assertFalse(supplyService.isDeliveryConfirmed());
            }

            @Test
            public void testDeliveryExternalServiceFailure() {
//        // Arrange (Similar to successful delivery, with external service exception)
//        MockPaymentService paymentService = new MockPaymentService(true); // Successful payment
//        MockUserService userService = new MockUserService();
//        userService.setUserLoggedIn(true);
//        MockShoppingCart shoppingCart = new MockShoppingCart(true); // Valid shopping cart
//        MockSupplyService supplyService = new MockSupplyService(true); // Throws exception
//        TradingSystem tradingSystem = new TradingSystem(userService, paymentService, supplyService);
//
//        tradingSystem.processPayment(shoppingCart); // Simulate successful payment
//
//        // Act
//        boolean result = tradingSystem.initiateDelivery();
//
//        // Assert
//        assertFalse(result); // May need adjustment based on exception handling
            }

            // Add similar tests for other scenarios (e.g., payment cancellation on delivery

        }

        // 6.4
        @Nested
        class MarketHistoryTest {

            @Test
            public void testViewMarketHistorySuccess() {
//        // Arrange
//        MockUserService userService = new MockUserService();
//        userService.setUserLoggedIn(true);
//        userService.setIsUserAdmin(true); // Simulate system manager
//        MarketSystem marketSystem = new MarketSystem(userService);
//
//        // Act (Simulate navigation and data retrieval)
//        List<PastPurchase> pastPurchases = marketSystem.retrieveMarketHistory();
//        boolean result = marketSystem.displayMarketHistory(pastPurchases);
//
//        // Assert
//        assertTrue(result);
//        // Assert that pastPurchases is populated (not empty)
//        assertFalse(pastPurchases.isEmpty());
            }

            @Test
            public void testViewMarketHistoryEmpty() {
//        // Arrange
//        MockUserService userService = new MockUserService();
//        userService.setUserLoggedIn(true);
//        userService.setIsUserAdmin(true); // Simulate system manager
//        MarketSystem marketSystem = new MarketSystem(userService);
//
//        // Act (Simulate navigation and empty data retrieval)
//        List<PastPurchase> pastPurchases = Collections.emptyList();
//        boolean result = marketSystem.displayMarketHistory(pastPurchases);
//
//        // Assert
//        assertTrue(result);
//        // Assert that pastPurchases is empty
//        assertTrue(pastPurchases.isEmpty());
            }

            @Test
            public void testViewMarketHistoryNotLoggedIn() {
//        // Arrange
//        MockUserService userService = new MockUserService();
//        userService.setUserLoggedIn(false);
//        MarketSystem marketSystem = new MarketSystem(userService);
//
//        // Act (Attempt to navigate without login)
//        boolean result = marketSystem.displayMarketHistory(null);
//
//        // Assert
//        assertFalse(result);
            }
        }
    }
}