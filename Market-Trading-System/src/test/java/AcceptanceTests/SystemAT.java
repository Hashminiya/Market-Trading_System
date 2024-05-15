package AcceptanceTests;

import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.StoreController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

// 1.1
public class InitializeTradingSystemTest {

    private MockUserService userService;
    private TradingSystem tradingSystem;

    @Before
    public void setUp() {
        userService = new MockUserService();
        tradingSystem = new TradingSystem(userService);
    }

    @Test
    public void testSuccessfulInitialization() {
        // Arrange
        userService.setUserLoggedIn(true);
        userService.setIsUserAdmin(true); // User is system manager

        // Act
        boolean result = tradingSystem.initialize();

        // Assert
        assertTrue(result);
        assertTrue(tradingSystem.isConnectedToSupplyService());
        assertTrue(tradingSystem.isConnectedToPaymentService());
    }

    @Test
    public void testFailedInitializationNotManager() {
        // Arrange
        userService.setUserLoggedIn(true);
        userService.setIsUserAdmin(false); // User is not system manager

        // Act
        boolean result = tradingSystem.initialize();

        // Assert
        assertFalse(result);
        assertFalse(tradingSystem.isConnectedToSupplyService());
        assertFalse(tradingSystem.isConnectedToPaymentService());
    }
}

// 1.2 - if needed

// 1.3
public class PaymentTest {

    @Test
    public void testSuccessfulPayment() {
        // Arrange
        MockUserService userService = new MockUserService();
        userService.setUserLoggedIn(true);
        MockShoppingCart shoppingCart = new MockShoppingCart(true); // Valid shopping cart
        MockPaymentService paymentService = new MockPaymentService(true); // Successful payment
        TradingSystem tradingSystem = new TradingSystem(userService, paymentService);

        // Act
        boolean result = tradingSystem.processPayment(shoppingCart);

        // Assert
        assertTrue(result);
        assertTrue(paymentService.isPaymentSuccessful());
        assertTrue(shoppingCart.isEmpty()); // Shopping cart emptied
    }

    @Test
    public void testFailedPaymentInvalidDetails() {
        // Arrange
        MockUserService userService = new MockUserService();
        userService.setUserLoggedIn(true);
        MockShoppingCart shoppingCart = new MockShoppingCart(true); // Valid shopping cart
        MockPaymentService paymentService = new MockPaymentService(false); // Failed payment
        TradingSystem tradingSystem = new TradingSystem(userService, paymentService);

        // Act
        boolean result = tradingSystem.processPayment(shoppingCart);

        // Assert
        assertFalse(result);
        assertFalse(paymentService.isPaymentSuccessful());
        assertTrue(shoppingCart.isNotEmpty()); // Shopping cart not emptied
    }

    @Test
    public void testPaymentExternalServiceFailure() {
        // Arrange
        MockUserService userService = new MockUserService();
        userService.setUserLoggedIn(true);
        MockShoppingCart shoppingCart = new MockShoppingCart(true); // Valid shopping cart
        MockPaymentService paymentService = new MockPaymentService(true); // Payment service throws exception
        TradingSystem tradingSystem = new TradingSystem(userService, paymentService);

        // Act
        boolean result = tradingSystem.processPayment(shoppingCart);

        // Assert
        assertFalse(result); // May need adjustment based on exception handling
    }

    // Add similar tests for other scenarios
}

// 1.4
public class DeliveryTest {

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
public class MarketHistoryTest {

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
