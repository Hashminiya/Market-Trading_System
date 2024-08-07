package UnitTests.DomainLayer.Purchase;
import DAL.ItemDTO;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Purchase.PaymentServiceProxy;
import DomainLayer.Market.Purchase.Purchase;
import DomainLayer.Market.Purchase.PurchaseController;
import DomainLayer.Market.Purchase.SupplyServiceProxy;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.User.UserController;
import DomainLayer.Repositories.PurchaseRepository;
import ServiceLayer.ServiceFactory;
import SetUp.ApplicationTest;
import SetUp.cleanUpDB;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ApplicationTest.class)
public class CheckoutUT {

    PurchaseRepository purchaseRepo;
    PaymentServiceProxy paymentServiceProxy;
    SupplyServiceProxy supplyServiceProxy;
    PurchaseController purchaseController;

    @BeforeEach
    public void setUp() {
        purchaseRepo = mock(PurchaseRepository.class);
        paymentServiceProxy = mock(PaymentServiceProxy.class);
        supplyServiceProxy = mock(SupplyServiceProxy.class);
        purchaseController =  PurchaseController.getInstance(purchaseRepo, paymentServiceProxy,
                supplyServiceProxy);
    }

    @AfterEach
    void tearDown()throws Exception{
        resetPurchaseControllerInstance();
    }

    @AfterAll
    public static void tearDownAll() {
        cleanUpDB.clearDB();
    }

    private void resetPurchaseControllerInstance() throws Exception {
        Field instance = PurchaseController.class.getDeclaredField("purchaseControllerInstance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    @Order(1)
    void test_checkout_should_not_throw_exception_for_valid_info() {

        List<ItemDTO> items = new ArrayList<>();
        items.add(new ItemDTO(876123, "Chips",20,8282,500, new ArrayList<>(), "description"));
        items.add(new ItemDTO(98142, "Bamba",40,8282,1000, new ArrayList<>(), "description"));

        assertDoesNotThrow(() -> purchaseController.checkout("userID", "1234567890123456", new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000), "123", items, 1500));

    }

    @Test
    @Order(2)
    void test_checkout_should_throw_exception_for_empty_items_list() {

        List<ItemDTO> items = new ArrayList<>();

        assertThrows(RuntimeException.class, () -> purchaseController.checkout("userID", "1234567890123456", new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000), "123", items, 300));
    }

    @Test
    @Order(3)
    void test_checkout_should_throw_exception_for_invalid_credit() {

        purchaseController.setPaymentServiceProxy(paymentServiceProxy);

        List<ItemDTO> items = new ArrayList<>();
        items.add(new ItemDTO(876123, "Chips",20,8282,500, new ArrayList<>(), "description"));
        items.add(new ItemDTO(98142, "Bamba",40,8282,1000, new ArrayList<>(), "description"));

        assertThrows(RuntimeException.class, () -> purchaseController.checkout("userID", "1234", new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000), "123", items, 300));
    }

    @Test
    @Order(4)
    void test_checkout_should_throw_exception_for_invalid_supply_details() {

        purchaseController.setSupplyServiceProxy(supplyServiceProxy);

        List<ItemDTO> items = new ArrayList<>();

        assertThrows(RuntimeException.class, () -> purchaseController.checkout("userID", "1234567890123456", new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000), "123", items, 300));
    }
}
