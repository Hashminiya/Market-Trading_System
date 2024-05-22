package UnitTests.DomainLayer.Store;

import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.Purchase.PurchaseController;
import DomainLayer.Market.Store.Discount;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.UserController;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoreControllerUT {

    private final long STORE_ID = 1L;
    private final long ITEM_ID = 1L;
    private final String FOUNDER_ID = "founderId";
    private final String USER_ID = "userId";
    private final String ITEM_NAME = "itemName";
    private final String DESCRIPTION = "description";
    private final double ITEM_PRICE = 10.0;
    private final int STOCK_AMOUNT = 100;
    private final List<String> CATEGORIES = List.of("category");

    @Mock
    private IUserFacade userFacadeMock;

    @Mock
    private IPurchaseFacade purchaseFacadeMock;

    @Mock
    private InMemoryRepository<Long, Store> storesRepoMock;

    @Mock
    private IRepository<Long, Discount> discountRepoMock;


    @Mock
    private Store storeMock;
    @InjectMocks
    private StoreController storeFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storesRepoMock = mock(InMemoryRepository.class);
        userFacadeMock = mock(UserController.class);
        purchaseFacadeMock = mock((PurchaseController.class));
        storeFacade = StoreController.getInstance(storesRepoMock);
        storeFacade.setUserFacade(userFacadeMock);
        storeFacade.setPurchaseFacade(purchaseFacadeMock);
        storeMock = mock(Store.class);
    }

    @Test
    void testCreateStore() throws Exception {
        when(userFacadeMock.isRegister(FOUNDER_ID)).thenReturn(true);

        storeFacade.createStore(FOUNDER_ID, "storeName", "storeDescription", discountRepoMock);

        verify(storesRepoMock).save(any(Store.class));
    }

    @Test
    void testAddItemToStore() throws Exception {
        Store store = mock(Store.class);
        when(storesRepoMock.findById(STORE_ID)).thenReturn(store);
        when(userFacadeMock.checkPermission(USER_ID, STORE_ID, "ADD_ITEM")).thenReturn(true);

        storeFacade.addItemToStore(USER_ID, STORE_ID, ITEM_NAME, ITEM_PRICE, STOCK_AMOUNT, DESCRIPTION, CATEGORIES);

        verify(store).addItem(anyLong(), eq(ITEM_NAME), eq(ITEM_PRICE), eq(STOCK_AMOUNT), eq(DESCRIPTION), eq(CATEGORIES));
    }

    @Test
    void testUpdateItem() throws Exception {
        Store store = mock(Store.class);
        when(storesRepoMock.findById(STORE_ID)).thenReturn(store);
        when(userFacadeMock.checkPermission(USER_ID, STORE_ID, "UPDATE_ITEM")).thenReturn(true);

        storeFacade.updateItem(USER_ID, STORE_ID, ITEM_ID, "newName", 20.0, 200);

        verify(store).updateItem(ITEM_ID, "newName", 20.0, 200);
    }

    @Test
    void testDeleteItem() throws Exception {
        Store store = mock(Store.class);
        when(storesRepoMock.findById(STORE_ID)).thenReturn(store);
        when(userFacadeMock.checkPermission(USER_ID, STORE_ID, "DELETE_ITEM")).thenReturn(true);

        storeFacade.deleteItem(USER_ID, STORE_ID, ITEM_ID);

        verify(store).deleteItem(ITEM_ID);
    }

    @Test
    void testViewInventoryByStoreOwner() throws Exception {
        Store store = mock(Store.class);
        when(storesRepoMock.findById(STORE_ID)).thenReturn(store);
        when(userFacadeMock.checkPermission(USER_ID, STORE_ID, "VIEW_INVENTORY")).thenReturn(true);

        HashMap<Long, Integer> inventory = new HashMap<>();
        when(store.viewInventory()).thenReturn(List.of());

        HashMap<Long, Integer> result = storeFacade.viewInventoryByStoreOwner(USER_ID, STORE_ID);

        assertEquals(inventory, result);
    }

    @Test
    void testAssignStoreOwner() throws Exception {
        when(storesRepoMock.findById(eq(STORE_ID))).thenReturn(storeMock);
//        doNothing().when(storeMock).assignOwner("newOwnerId");
        when(userFacadeMock.checkPermission(USER_ID, STORE_ID, "ASSIGN_OWNER")).thenReturn(true);

        storeFacade.assignStoreOwner(USER_ID, STORE_ID, "newOwnerId");

        verify(storeMock).assignOwner("newOwnerId");
    }

    @Test
    void testAssignStoreManager() throws Exception {
        Store store = mock(Store.class);
        when(storesRepoMock.findById(STORE_ID)).thenReturn(store);
        when(userFacadeMock.checkPermission(USER_ID, STORE_ID, "ASSIGN_MANAGER")).thenReturn(true);

        storeFacade.assignStoreManager(USER_ID, STORE_ID, "newManagerId", List.of("permission"));

        verify(store).assignManager("newManagerId");
    }

    @Test
    void testRemoveStore() throws Exception {
        when(userFacadeMock.checkPermission(USER_ID, STORE_ID, "REMOVE_STORE")).thenReturn(true);

        storeFacade.removeStore(USER_ID, STORE_ID);

        verify(storesRepoMock).delete(STORE_ID);
    }

    @Test
    void testViewStoreManagementInfo() throws Exception {
        Store store = mock(Store.class);
        when(storesRepoMock.findById(STORE_ID)).thenReturn(store);
        when(userFacadeMock.checkPermission(USER_ID, STORE_ID, "VIEW_STORE_MANAGEMENT_INFO")).thenReturn(true);

        HashMap<String, List<String>> managementInfo = new HashMap<>();
        when(store.getManagers()).thenReturn(List.of());
        when(store.getOwners()).thenReturn(List.of());

        HashMap<String, List<String>> result = storeFacade.viewStoreManagementInfo(USER_ID, STORE_ID);

        assertEquals(managementInfo, result);
    }

    @Test
    void testViewPurchaseHistory() throws Exception {
        when(userFacadeMock.checkPermission(USER_ID, STORE_ID, "VIEW_PURCHASE_HISTORY")).thenReturn(true);
        when(purchaseFacadeMock.getPurchasesByStore(STORE_ID)).thenReturn(new HashMap<>());

        HashMap<Long, HashMap<Long, Integer>> result = storeFacade.viewPurchaseHistory(USER_ID, STORE_ID);

        assertNotNull(result);
    }
}
