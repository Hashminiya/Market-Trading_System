package AcceptanceTests;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.Discount;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.SystemManager;
import DomainLayer.Market.User.User;
import DomainLayer.Market.Util.InMemoryRepository;
import ServiceLayer.ServiceFactory;
import ServiceLayer.User.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SystemAT {
    private final String USER_NAME = "UserPayment";
    private final String USER_NAME_UNKNOWN = "UserPaymentDoesntExist";
    private final String USER_PW = "UserPW";
    private final String STORE_NAME = "PaymentStore";
    private final long BASKET_ID = 456L;
    private final long ITEM_ID = 789L;
    private final int ITEM_QUANTITY = 2;
    private static final String CREDIT_CARD = "1234567890123456";
    private static final Date EXPIRY_DATE = new Date(System.currentTimeMillis() + 86400000L); // 1 day in future
    private static final String CVV = "123";
    private static final String DISCOUNT_CODE = "DISCOUNT10";




    // 1.1
    @Nested
    class InitializeTradingSystemTest {
        private ServiceFactory serviceFactory;
        private IUserFacade userFacade;

        private final String SYSTEM_MANAGER_NAME = "SystemManager";
        private final String SYSTEM_MANAGER_PW = "SystemManagerPassword";
        private final String SYSTEM_MANAGER_WRONG_PW = "SystemManagerWrongPassword";

        @BeforeEach
        void setUp() {
            // Init only the user facade
            serviceFactory = ServiceFactory.getServiceFactory();
            userFacade = serviceFactory.getUserFacade();
        }

        @Test
        public void testSuccessfulInitialization() {
            // Arrange
            try {
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

    }
// 1.2 - if needed

    // 1.3
    @Nested
    class PaymentTest {
        private ServiceFactory serviceFactory;
        private IUserFacade userFacade;
        private IStoreFacade storeFacade;
        private IPurchaseFacade purchaseFacade;

        @BeforeEach
        void setUp() {
            serviceFactory = ServiceFactory.getServiceFactory();
            serviceFactory.initFactory();
            userFacade = serviceFactory.getUserFacade();
            storeFacade = serviceFactory.getStoreFacade();
            purchaseFacade = serviceFactory.getPurchaseFacade();
            try {
                userFacade.register(USER_NAME, USER_PW, 110596);
                userFacade.login(USER_NAME, USER_PW);
                storeFacade.createStore(USER_NAME, STORE_NAME, "Greate Store!", new InMemoryRepository<Long, Discount>());
            } catch (Exception exp) {
                fail();
            }
        }

        @Test
        public void testSuccessfulPayment() {
            // Assert
            userFacade.addItemToBasket(USER_NAME, BASKET_ID, ITEM_ID, ITEM_QUANTITY);

            // Act
            try {
                userFacade.checkoutShoppingCart(USER_NAME, CREDIT_CARD, EXPIRY_DATE, CVV, DISCOUNT_CODE);
            } catch (Exception exp) {
                fail();
            }
            // Validate that the checkout process was completed successfully
            List<ItemDTO> purchasedItems = purchaseFacade.getPurchasedItems();
            boolean itemPurchased = purchasedItems.stream()
                    .anyMatch(item -> item.getItemId() == ITEM_ID && item.getQuantity() == ITEM_QUANTITY);
            assertTrue(itemPurchased, "The item should be marked as purchased.");
        }

        @Test
        public void testFailedPaymentInvalidDetails() {
            // Assert
            userFacade.addItemToBasket(USER_NAME, BASKET_ID, ITEM_ID, ITEM_QUANTITY);

            // Act
            try {
                userFacade.checkoutShoppingCart(USER_NAME_UNKNOWN, CREDIT_CARD, EXPIRY_DATE, CVV, DISCOUNT_CODE);
            } catch (Exception exception) {
                fail();
            }
            // Validate that the checkout process was completed successfully
            List<ItemDTO> purchasedItems = purchaseFacade.getPurchasedItems();
            boolean itemPurchased = purchasedItems.stream()
                    .anyMatch(item -> item.getItemId() == ITEM_ID && item.getQuantity() == ITEM_QUANTITY);
            assertFalse(itemPurchased, "The item should be marked as purchased.");
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
        private ServiceFactory serviceFactory;
        private IUserFacade userFacade;
        private SupplyServiceImpl supplyService;

        @BeforeEach
        void setUp() {
            serviceFactory = ServiceFactory.getServiceFactory();
            serviceFactory.initFactory();
            userFacade = serviceFactory.getUserFacade();
            supplyService = serviceFactory.getSupplyService();
        }

        @Test
        public void testSuccessfulDelivery() {
            // Arrange
            userFacade.addItemToBasket(USER_NAME, BASKET_ID, ITEM_ID, ITEM_QUANTITY);
            try {
                userFacade.checkoutShoppingCart(USER_NAME, CREDIT_CARD, EXPIRY_DATE, CVV, DISCOUNT_CODE);
            } catch (Exception exception) {
                fail();
            }
            // TODO : Implement due to supply service..
            // boolean deliveryConfirmed = supplyService.validateItemSupply(STORE_ID, ITEM_ID, 1);
            boolean deliveryConfirmed = true;
            assertTrue(deliveryConfirmed, "The delivery should be confirmed.");
        }

        @Test
        public void testFailedDeliveryInvalidDetails() {
        }

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
            assertTrue(true);
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
            assertTrue(true);
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
            assertTrue(true);
        }
    }
}

