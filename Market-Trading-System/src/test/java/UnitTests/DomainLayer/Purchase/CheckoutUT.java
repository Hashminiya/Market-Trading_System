package UnitTests.DomainLayer.Purchase;
import DAL.ItemDTO;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Purchase.PaymentServiceProxy;
import DomainLayer.Market.Purchase.Purchase;
import DomainLayer.Market.Purchase.PurchaseController;
import DomainLayer.Market.Purchase.SupplyServiceProxy;
import DomainLayer.Market.Util.IRepository;
import ServiceLayer.ServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CheckoutUT {

    @BeforeEach
    void setUp(){

    }

    @Test
    void checkout_ValidItems_SuccessfullyCheckedOut() {
        // Mocking dependencies
        ServiceFactory  serviceFactory= ServiceFactory.getServiceFactory();
        IRepository<Long, Purchase> purchaseRepo = mock(IRepository.class);
        PaymentServiceProxy paymentServiceProxy = mock(PaymentServiceProxy.class);
        SupplyServiceProxy supplyServiceProxy = mock(SupplyServiceProxy.class);

        // Creating items for checkout
        List<ItemDTO> items = new ArrayList<>();
        items.add(new ItemDTO(876123, "Chips",20,8282,500));
        items.add(new ItemDTO(98142, "Bamba",40,8282,1000));

        // Creating instance of PurchaseController
        PurchaseController purchaseController =  PurchaseController.getInstance(purchaseRepo, paymentServiceProxy,
                supplyServiceProxy);

        // Mocking behavior of paymentServiceProxy
        when(paymentServiceProxy.validateCreditCard(anyString(), any(Date.class), anyString(), anyDouble())).thenReturn(true);
        // Mocking behavior of supplyServiceProxy
        when(supplyServiceProxy.validateCartSupply(anyList())).thenReturn(true);

        // Testing checkout process
        assertDoesNotThrow(() -> purchaseController.checkout("userID", "1234567890123456", new Date(), "123", items, 1500));

        // Verifying that save method is called on purchaseRepo
        verify(purchaseRepo, times(1)).save(any(Purchase.class));
    }

    @Test
    void checkout_NoItems_ExceptionThrown() {
        IRepository<Long, Purchase> purchaseRepo = mock(IRepository.class);
        PaymentServiceProxy paymentServiceProxy = mock(PaymentServiceProxy.class);
        SupplyServiceProxy supplyServiceProxy = mock(SupplyServiceProxy.class);

        PurchaseController purchaseController =  PurchaseController.getInstance(purchaseRepo, paymentServiceProxy,
                supplyServiceProxy);

        List<ItemDTO> items = new ArrayList<>();

        // Testing checkout with no items
        assertThrows(RuntimeException.class, () -> purchaseController.checkout("userID", "1234567890123456", new Date(), "123", items, 300));
    }
}
