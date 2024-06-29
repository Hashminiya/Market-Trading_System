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
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Repositories.PurchaseRepository;
import ServiceLayer.ServiceFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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

    @AfterEach
    void tearDown()throws Exception{
        resetPurchaseControllerInstance();
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
        items.add(new ItemDTO(876123, "Chips",20,8282,500));
        items.add(new ItemDTO(98142, "Bamba",40,8282,1000));

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
        items.add(new ItemDTO(876123, "Chips",20,8282,500));
        items.add(new ItemDTO(98142, "Bamba",40,8282,1000));

        assertThrows(RuntimeException.class, () -> purchaseController.checkout("userID", "1234567890123456", new Date(), "123", items, 300));
    }

    @Test
    @Order(4)
    void test_checkout_should_throw_exception_for_invalid_supply_details() {

        when(supplyServiceProxy.validateCartSupply(anyList())).thenReturn(false);
        purchaseController.setSupplyServiceProxy(supplyServiceProxy);

        List<ItemDTO> items = new ArrayList<>();
        items.add(new ItemDTO(876123, "Chips",20,8282,500));
        items.add(new ItemDTO(98142, "Bamba",40,8282,1000));

        assertThrows(RuntimeException.class, () -> purchaseController.checkout("userID", "1234567890123456", new Date(), "123", items, 300));
    }
}
