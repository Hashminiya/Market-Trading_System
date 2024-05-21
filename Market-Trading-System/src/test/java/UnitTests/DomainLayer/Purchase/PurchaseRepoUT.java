package UnitTests.DomainLayer.Purchase;

import DAL.ItemDTO;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Purchase.PaymentServiceProxy;
import DomainLayer.Market.Purchase.Purchase;
import DomainLayer.Market.Purchase.PurchaseController;
import DomainLayer.Market.Purchase.SupplyServiceProxy;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PurchaseRepoUT {

    @Test
    void getPurchasesByStore_ReturnsCorrectItems() {
        IRepository<Long, Purchase> purchaseRepo = mock(IRepository.class);
        PaymentServiceProxy paymentServiceProxy = mock(PaymentServiceProxy.class);
        SupplyServiceProxy supplyServiceProxy = mock(SupplyServiceProxy.class);

        // Creating instance of PurchaseController
        PurchaseController purchaseController = new PurchaseController(purchaseRepo, paymentServiceProxy,
                Mockito.mock(PaymentServiceImpl.class),
                supplyServiceProxy, Mockito.mock(SupplyServiceImpl.class));

        // Mocking behavior of purchaseRepo
        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(new Purchase("userID1", 30.0, 1L, createItemsList(8383), paymentServiceProxy, supplyServiceProxy));
        purchaseList.add(new Purchase("userID2", 20.0, 2L, createItemsList(8282), paymentServiceProxy, supplyServiceProxy));
        when(purchaseRepo.findAll()).thenReturn(purchaseList);

        // Testing getPurchasesByStore
        HashMap<Long, List<ItemDTO>> result = purchaseController.getPurchasesByStore(8282);

        assertEquals(2, result.size());
        assertTrue(result.containsKey(1L));
        assertEquals(2, result.get(2L).size());
    }

    @Test
    void getPurchasedItems_ReturnsCorrectItems() {
        IRepository<Long, Purchase> purchaseRepo = mock(IRepository.class);
        PaymentServiceProxy paymentServiceProxy = mock(PaymentServiceProxy.class);
        SupplyServiceProxy supplyServiceProxy = mock(SupplyServiceProxy.class);

        // Creating instance of PurchaseController
        PurchaseController purchaseController = new PurchaseController(purchaseRepo, paymentServiceProxy,
                Mockito.mock(PaymentServiceImpl.class),
                supplyServiceProxy, Mockito.mock(SupplyServiceImpl.class));

        // Mocking behavior of inventoryReduceItems
        List<ItemDTO> inventoryReduceItems = createItemsList(8282);
        purchaseController.checkout("userId","123123123", new Date(),"123",inventoryReduceItems,1000);

        // Testing getPurchasedItems
        List<ItemDTO> result = purchaseController.getPurchasedItems();

        assertEquals(1, result.size());
    }

    private List<ItemDTO> createItemsList(long storeId) {
        List<ItemDTO> items = new ArrayList<>();
        items.add(new ItemDTO(876123, "Chips",20,8282,500));
        items.add(new ItemDTO(98142, "Bamba",40,8282,1000));

        return items;
    }

}
