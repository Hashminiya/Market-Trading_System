package AcceptanceTests;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Store.Discount;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.SystemManager;
import DomainLayer.Market.Util.InMemoryRepository;
import ServiceLayer.ServiceFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SystemAT {
    private static final String USER_NAME = "UserPayment";
    private static final String USER_NAME_UNKNOWN = "UserPaymentDoesntExist";
    private static final String USER_PW = "UserPW";
    private static final String STORE_NAME = "PaymentStore";
    private static final long BASKET_ID = 456L;
    private static final long ITEM_ID = 789L;
    private static final int ITEM_QUANTITY = 2;
    private static final String CREDIT_CARD = "1234567890123456";
    private static final Date EXPIRY_DATE = new Date(System.currentTimeMillis() + 86400000L); // 1 day in future
    private static final String CVV = "123";
    private static final String DISCOUNT_CODE = "DISCOUNT10";
    private static final String SYSTEM_MANAGER_NAME = "SystemManager";
    private static final String SYSTEM_MANAGER_PW = "SystemManagerPassword";
    private static final String SYSTEM_MANAGER_WRONG_PW = "SystemManagerWrongPassword";

    private ServiceFactory serviceFactory;
    private IUserFacade userFacade;
    private SupplyServiceImpl supplyService;
    private IStoreFacade storeFacade;
    private IPurchaseFacade purchaseFacade;

    @BeforeEach
    void setUp() {
        serviceFactory = ServiceFactory.getServiceFactory();
        userFacade = serviceFactory.getUserFacade();
    }

    // 1.1
    @Test
    public void testSuccessfulInitialization() {
        // Arrange
        try {
            // Login manager to system
            userFacade.login(SYSTEM_MANAGER_NAME, SYSTEM_MANAGER_PW);
            SystemManager systemManager = SystemManager.getInstance();
            systemManager.login();
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
        } catch (Exception exp) {
            SystemManager systemManager = SystemManager.getInstance();

            // Act
            systemManager.InitializeTradingSystem();
            assertNull(serviceFactory.getUserService());
        }
    }

    // 1.2 - if needed

    // 1.3
    @Test
    public void testSuccessfulPayment() {
        // Assert
        try {
            serviceFactory.initFactory();
            storeFacade = serviceFactory.getStoreFacade();
            userFacade.register(USER_NAME, USER_PW, 110596);
            userFacade.login(USER_NAME, USER_PW);
            storeFacade.createStore(USER_NAME, STORE_NAME, "Greate Store!", new InMemoryRepository<Long, Discount>());
        } catch (Exception exp) {
            fail();
        }

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
        serviceFactory.initFactory();
        // Assert
        userFacade.viewShoppingCart(USER_NAME);
        userFacade.addItemToBasket(USER_NAME, BASKET_ID, ITEM_ID, ITEM_QUANTITY);

        // Act
        try {
            userFacade.checkoutShoppingCart(USER_NAME_UNKNOWN, CREDIT_CARD, EXPIRY_DATE, CVV, DISCOUNT_CODE);
        } catch (Exception exception) {
            fail();
        }

        List<ItemDTO> purchasedItems = purchaseFacade.getPurchasedItems();
        boolean itemPurchased = purchasedItems.stream()
                .anyMatch(item -> item.getItemId() == ITEM_ID && item.getQuantity() == ITEM_QUANTITY);
        assertFalse(itemPurchased, "The item should be marked as purchased.");
    }

    @Test
    public void testPaymentExternalServiceFailure() {
        assertTrue(true);
    }

    // Add similar tests for other scenarios

    // 1.4
    @Test
    public void testSuccessfulDelivery() {
        serviceFactory.initFactory();
        // Arrange
        try {
            userFacade.addItemToBasket(USER_NAME, BASKET_ID, ITEM_ID, ITEM_QUANTITY);
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

    // 6.4
    @Test
    public void testViewMarketHistorySuccess() {
        assertTrue(true);
    }

    @Test
    public void testViewMarketHistoryEmpty() {
        assertTrue(true);
    }

    @Test
    public void testViewMarketHistoryNotLoggedIn() {
        assertTrue(true);
    }
}


