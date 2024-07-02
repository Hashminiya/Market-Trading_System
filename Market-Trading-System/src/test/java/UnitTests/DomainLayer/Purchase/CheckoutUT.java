package UnitTests.DomainLayer.Purchase;
import API.Application;
import API.SpringContext;
import DAL.ItemDTO;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Purchase.PaymentServiceProxy;
import DomainLayer.Market.Purchase.Purchase;
import DomainLayer.Market.Purchase.PurchaseController;
import DomainLayer.Market.Purchase.SupplyServiceProxy;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.User.UserController;
import DomainLayer.Repositories.*;
import ServiceLayer.ServiceFactory;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.SpringApplication;

import javax.print.StreamPrintService;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
        when(paymentServiceProxy.validateCreditCard(anyString(), any(Date.class), anyString(), anyDouble())).thenReturn(true);
        when(supplyServiceProxy.validateCartSupply(anyList())).thenReturn(true);
        purchaseController =  PurchaseController.getInstance(purchaseRepo, paymentServiceProxy,
                supplyServiceProxy);
    }

    @BeforeAll
    public static void springSetUp(){
        SpringApplication.run(Application.class);
    }

    @AfterEach
    void tearDown()throws Exception{
        resetPurchaseControllerInstance();
    }

    @AfterAll
    public static void springTearDown(){
        PurchaseRepository purchaseRepository  = SpringContext.getBean(PurchaseRepository.class);
        purchaseRepository.deleteAll();
        ItemDTORepository itemDTORepository  = SpringContext.getBean(ItemDTORepository.class);
        itemDTORepository.deleteAll();
        BasketItemRepository basketItemRepository  = SpringContext.getBean(BasketItemRepository.class);
        basketItemRepository.deleteAll();
        ItemRepository items = SpringContext.getBean(DbItemRepository.class);
        items.deleteAll();
        BasketRepository baskets = SpringContext.getBean(DbBasketRepository.class);
        baskets.deleteAll();
        StoreRepository stores = SpringContext.getBean(DbStoreRepository.class);
        stores.deleteAll();
        UserRepository users = SpringContext.getBean(DbUserRepository.class);
        users.deleteAll();
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

        assertDoesNotThrow(() -> purchaseController.checkout("userID", "1234567890123456", new Date(), "123", items, 1500));
    }


    @Test
    @Order(2)
    void test_checkout_should_throw_exception_for_empty_items_list() {

        List<ItemDTO> items = new ArrayList<>();

        assertThrows(RuntimeException.class, () -> purchaseController.checkout("userID", "1234567890123456", new Date(), "123", items, 300));
    }

    @Test
    @Order(3)
    void test_checkout_should_throw_exception_for_invalid_credit() {

        when(paymentServiceProxy.validateCreditCard(anyString(), any(Date.class), anyString(), anyDouble())).thenReturn(false);
        purchaseController.setPaymentServiceProxy(paymentServiceProxy);

        List<ItemDTO> items = new ArrayList<>();
        items.add(new ItemDTO(876123, "Chips",20,8282,500, new ArrayList<>(), "description"));
        items.add(new ItemDTO(98142, "Bamba",40,8282,1000, new ArrayList<>(), "description"));

        assertThrows(RuntimeException.class, () -> purchaseController.checkout("userID", "1234567890123456", new Date(), "123", items, 300));
    }

    @Test
    @Order(4)
    void test_checkout_should_throw_exception_for_invalid_supply_details() {

        when(supplyServiceProxy.validateCartSupply(anyList())).thenReturn(false);
        purchaseController.setSupplyServiceProxy(supplyServiceProxy);

        List<ItemDTO> items = new ArrayList<>();
        items.add(new ItemDTO(876123, "Chips",20,8282,500, new ArrayList<>(), "description"));
        items.add(new ItemDTO(98142, "Bamba",40,8282,1000, new ArrayList<>(), "description"));

        assertThrows(RuntimeException.class, () -> purchaseController.checkout("userID", "1234567890123456", new Date(), "123", items, 300));
    }

}
