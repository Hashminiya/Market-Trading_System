package UnitTests;

import DomainLayer.Market.IRepository;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StoreFacadeUT {

    @Mock
    IUserFacade userFacadeMock;

    @Mock
    IRepository<Store> stores;


    @InjectMocks
    private IStoreFacade storeFacade = new StoreController();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes annotated fields
    }


    @Test
    void testCreateStore() {
        long founderId = 1;
        String storeName = "Test Store";
        String storeDescription = "Test Store Description";

        storeFacade.createStore(founderId, storeName, storeDescription);

        verify(storeControllerMock).createStore(founderId, storeName, storeDescription);
    }

    void testCreateStore() {
        // Arrange
        long founderId = 1;
        String storeName = "Test Store";
        String storeDescription = "Test Store Description";

        // Mock behavior of userFacadeMock if needed
        // For example, if you expect getUserById to return a user object
        when(userFacadeMock.getUserById(founderId)).thenReturn(new User(founderId, "Founder", "Founder@example.com"));

        // Act
        storeFacade.createStore(founderId, storeName, storeDescription);

        // Assert
        assertNotNull(createdStore); // Ensure a store object is returned
        assertEquals(founderId, createdStore.getFounderId()); // Ensure correct founder ID
        assertEquals(storeName, createdStore.getName()); // Ensure correct store name
        assertEquals(storeDescription, createdStore.getDescription()); // Ensure correct store description
    }

    @Test
    void testViewInventoryByStoreOwner() {
        long userId = 1;
        long storeId = 2;

        storeFacade.viewInventoryByStoreOwner(userId, storeId);

        verify(storeControllerMock).viewInventoryByStoreOwner(userId, storeId);
    }

    @Test
    void testAddItemToStore() {
        long userId = 1;
        long storeId = 2;
        String itemName = "Test Item";
        double itemPrice = 10.99;
        int stockAmount = 20;
        long categoryId = 3;

        when(storeControllerMock.itemIsExists(storeId, itemName)).thenReturn(false);

        storeFacade.addItemToStore(userId, storeId, itemName, itemPrice, stockAmount, categoryId);

        verify(storeControllerMock).addItemToStore(userId, storeId, itemName, itemPrice, stockAmount, categoryId);
    }

    @Test
    void testFailureAddItemToStore() {
        long userId = 1;
        long storeId = 2;
        String itemName = "Test Item";
        double itemPrice = 10.99;
        int stockAmount = 20;
        long categoryId = 3;

        when(storeControllerMock.itemIsExists(storeId, itemName)).thenReturn(true);

        storeFacade.addItemToStore(userId, storeId, itemName, itemPrice, stockAmount, categoryId);

        verify(storeControllerMock).addItemToStore(userId, storeId, itemName, itemPrice, stockAmount, categoryId);
    }

    @Test
    void testUpdateItem() {
        long userId = 1;
        long storeId = 2;
        long itemId = 3;
        String newName = "Updated Item";
        double newPrice = 15.99;
        int stockAmount = 30;

        storeFacade.updateItem(userId, storeId, itemId, newName, newPrice, stockAmount);

        verify(storeControllerMock).updateItem(userId, storeId, itemId, newName, newPrice, stockAmount);
    }

    @Test
    void testDeleteItem() {
        long userId = 1;
        long storeId = 2;
        long itemId = 3;

        storeFacade.deleteItem(userId, storeId, itemId);

        verify(storeControllerMock).deleteItem(userId, storeId, itemId);
    }

    @Test
    void testChangeStorePolicy() {
        long userId = 1;
        long storeId = 2;

        storeFacade.changeStorePolicy(userId, storeId);

        verify(storeControllerMock).changeStorePolicy(userId, storeId);
    }

    @Test
    void testChangeDiscountType() {
        long userId = 1;
        long storeId = 2;
        String newType = "New Type";

        storeFacade.changeDiscountType(userId, storeId, newType);

        verify(storeControllerMock).changeDiscountType(userId, storeId, newType);
    }

    @Test
    void testAssignStoreOwner() {
        long actorId = 1;
        long userId = 2;

        storeFacade.assignStoreOwner(actorId, userId);

        verify(storeControllerMock).assignStoreOwner(actorId, userId);
    }

    @Test
    void testAssignStoreManager() {
        long actorId = 1;
        long userId = 2;

        storeFacade.assignStoreManager(actorId, userId);

        verify(storeControllerMock).assignStoreManager(actorId, userId);
    }

    @Test
    void testRemoveStore() {
        long userId = 1;
        long storeId = 2;

        storeFacade.removeStore(userId, storeId);

        verify(storeControllerMock).removeStore(userId, storeId);
    }

    @Test
    void testViewStoreManagementInfo() {
        long userId = 1;
        long storeId = 2;

        storeFacade.viewStoreManagementInfo(userId, storeId);

        verify(storeControllerMock).viewStoreManagementInfo(userId, storeId);
    }

    @Test
    void testViewPurchaseHistory() {
        long userId = 1;
        long storeId = 2;

        storeFacade.viewPurchaseHistory(userId, storeId);

        verify(storeControllerMock).viewPurchaseHistory(userId, storeId);
    }

    @Test
    void testGetAllProductsInfoByStore() {
        long storeId = 2;

        storeFacade.getAllProductsInfoByStore(storeId);

        verify(storeControllerMock).getAllProductsInfoByStore(storeId);
    }

    @Test
    void testGetAllStoreInfo() {
        long storeId = 2;

        storeFacade.getAllStoreInfo(storeId);

        verify(storeControllerMock).getAllStoreInfo(storeId);
    }

    @Test
    void testSearchStoreByName() {
        String name = "Test Store";

        storeFacade.searchStoreByName(name);

        verify(storeControllerMock).searchStoreByName(name);
    }

    @Test
    void testSearchItemByName() {
        String name = "Test Item";

        storeFacade.searchItemByName(name);

        verify(storeControllerMock).searchItemByName(name);
    }

    @Test
    void testSearchStoreByCategory() {
        long category = 3;

        storeFacade.searchStoreByCategory(category);

        verify(storeControllerMock).searchStoreByCategory(category);
    }

    @Test
    void testSearchItemByCategory() {
        long category = 3;

        storeFacade.searchItemByCategory(category);

        verify(storeControllerMock).searchItemByCategory(category);
    }

    @Test
    void testSearchStoreByKeyWord() {
        String keyWord = "Test";

        storeFacade.searchStoreByKeyWord(keyWord);

        verify(storeControllerMock).searchStoreByKeyWord(keyWord);
    }

    @Test
    void testSearchItemByKeyWord() {
        String keyWord = "Test";

        storeFacade.searchItemByKeyWord(keyWord);

        verify(storeControllerMock).searchItemByKeyWord(keyWord);
    }

    @Test
    void testAddItemToShoppingBasket() {
        long userId = 1;
        long storeId = 2;
        long itemId = 3;

        storeFacade.addItemToShoppingBasket(userId, storeId, itemId);

        verify(storeControllerMock).addItemToShoppingBasket(userId, storeId, itemId);
    }
}
