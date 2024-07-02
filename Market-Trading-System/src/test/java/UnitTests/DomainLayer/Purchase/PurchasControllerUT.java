package UnitTests.DomainLayer.Purchase;

import API.SpringContext;
import DAL.ItemDTO;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Purchase.PaymentServiceProxy;
import DomainLayer.Market.Purchase.Purchase;
import DomainLayer.Market.Purchase.PurchaseController;
import DomainLayer.Market.Purchase.SupplyServiceProxy;
import DomainLayer.Repositories.InMemoryDTORepository;
import DomainLayer.Repositories.InMemoryPurchaseRepository;
import DomainLayer.Repositories.ItemDTORepository;
import DomainLayer.Repositories.PurchaseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PurchasControllerUT {

    PurchaseRepository purchaseRepo;
    PaymentServiceProxy paymentServiceProxy;
    SupplyServiceProxy supplyServiceProxy;
    PurchaseController purchaseController;
    ItemDTORepository itemDTORepository8282;
    ItemDTORepository itemDTORepository8283;


    @BeforeEach
    public void setUp() {
        itemDTORepository8282 = mock(ItemDTORepository.class);
        itemDTORepository8283 = mock(ItemDTORepository.class);
        List<Purchase> purchaseList = new ArrayList<>();
        try (MockedStatic<SpringContext> mockedStatic = mockStatic(SpringContext.class)) {
            mockedStatic.when(() -> SpringContext.getBean(ItemDTORepository.class)).thenReturn(itemDTORepository8282);
            purchaseList.add(new Purchase("userID2", 20.0, 2L, createItemsList(8282), paymentServiceProxy, supplyServiceProxy));
        }
        try (MockedStatic<SpringContext> mockedStatic = mockStatic(SpringContext.class)) {
            mockedStatic.when(() -> SpringContext.getBean(ItemDTORepository.class)).thenReturn(itemDTORepository8283);
            purchaseList.add(new Purchase("userID1", 30.0, 1L, createItemsList(8383), paymentServiceProxy, supplyServiceProxy));
        }
        purchaseRepo = mock(InMemoryPurchaseRepository.class);
        paymentServiceProxy = mock(PaymentServiceProxy.class);
        supplyServiceProxy = mock(SupplyServiceProxy.class);
        when(paymentServiceProxy.validateCreditCard(anyString(), any(Date.class), anyString(), anyDouble())).thenReturn(true);
        when(supplyServiceProxy.validateCartSupply(anyList())).thenReturn(true);
        when(purchaseRepo.findAll()).thenReturn(purchaseList);
        purchaseController =  PurchaseController.getInstance(purchaseRepo, paymentServiceProxy,
                supplyServiceProxy);
        purchaseController.setPurchaseRepo(purchaseRepo);
        purchaseController.setPaymentServiceProxy(paymentServiceProxy);
        purchaseController.setSupplyServiceProxy(supplyServiceProxy);
    }

    @AfterEach
    void tearDown()throws Exception{
        // Reset the singleton instance or any shared state here
        resetPurchaseControllerInstance();
    }

    private void resetPurchaseControllerInstance() throws Exception {
        Field instance = PurchaseController.class.getDeclaredField("purchaseControllerInstance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    @Order(1)
    void test_getPurchasesByStore_should_return_correct_items() {
        try (MockedStatic<SpringContext> mockedStatic = mockStatic(SpringContext.class)) {
            when(itemDTORepository8282.findAll()).thenReturn(createItemsList(8282));
            mockedStatic.when(() -> SpringContext.getBean(ItemDTORepository.class)).thenReturn(new InMemoryDTORepository());
            HashMap<Long, List<ItemDTO>> result = purchaseController.getPurchasesByStore(8282);
            assertEquals(1, result.size());
            assertTrue(result.containsKey(2L));
            assertEquals(2, result.get(2L).size());
        }
    }

    @Test
    @Order(2)
    void test_getPurchasedItems_should_return_correct_items() {

        // Mocking behavior of inventoryReduceItems
        List<ItemDTO> inventoryReduceItems = createItemsList(8282);
        try (MockedStatic<SpringContext> mockedStatic = mockStatic(SpringContext.class)) {
            mockedStatic.when(() -> SpringContext.getBean(ItemDTORepository.class)).thenReturn(itemDTORepository8282);
            purchaseController.checkout("userId","123123123", new Date(),"123",inventoryReduceItems,1500);
            // Testing getPurchasedItems
            List<ItemDTO> result = purchaseController.getPurchasedItems();

            assertEquals(2, result.size());
        }
    }

    private List<ItemDTO> createItemsList(long storeId) {
        List<ItemDTO> items = new ArrayList<>();
        items.add(new ItemDTO(876123, "Chips",20,storeId,500, new ArrayList<>(), "description"));
        items.add(new ItemDTO(98142, "Bamba",40,storeId,1000, new ArrayList<>(), "description"));

        return items;
    }

}
