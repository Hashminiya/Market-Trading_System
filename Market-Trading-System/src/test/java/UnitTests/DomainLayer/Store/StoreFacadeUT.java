package UnitTests.DomainLayer.Store;

import DomainLayer.Market.IRepository;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.IUserFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

public class StoreFacadeUT {

    private final long USER_ID = 1;
    private final long ACTOR_ID = 2;
    private final long STORE_ID = 1;
    private final long FOUNDER_ID = 1;
    private final String TOKEN = "TOKEN";
    private String STORE_NAME = "Test Store";
    @Mock
    IUserFacade userFacadeMock;

    @Mock
    IRepository<Long, Store> stores;

    @InjectMocks
    private IStoreFacade storeFacade = new StoreController();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes annotated fields
    }

    @Test
    void testCreateStore() {
//        // Arrange
//        String storeDescription = "Test Store Description";
//        User founder = new User(FOUNDER_ID, "Founder", "founder@example.com");
//        Store mockStore = new Store(/*pass appropriate parameters*/);
//
//        // Mock behavior of userFacadeMock to return a user object
//        when(userFacadeMock.getUserById(FOUNDER_ID)).thenReturn(founder);
//        when(stores.save(any())).thenReturn(mockStore);
//
//        // Act
//        storeFacade.createStore(FOUNDER_ID, STORE_NAME, storeDescription);
//
//        // Assert
//        verify(userFacadeMock).getUserById(FOUNDER_ID); // Ensure getUserById is called with the correct parameter
//        verify(stores).save(mockStore); // Ensure store is saved
    }

    //
    @Test
    void testCreateStore2() {
//        // Arrange
//        String storeDescription = "Test Store Description";
//
//        // Mock behavior of userFacadeMock if needed
//        // For example, if you expect getUserById to return a user object
//        when(userFacadeMock.getUserById(FOUNDER_ID)).thenReturn(new User(FOUNDER_ID, "Founder", "Founder@example.com"));
//
//        // Act
//        storeFacade.createStore(FOUNDER_ID, STORE_NAME, storeDescription);
//
//        // Assert
//        assertNotNull(createdStore); // Ensure a store object is returned
//        assertEquals(FOUNDER_ID, createdStore.getFounderId()); // Ensure correct founder ID
//        assertEquals(STORE_NAME, createdStore.getName()); // Ensure correct store name
//        assertEquals(storeDescription, createdStore.getDescription()); // Ensure correct store description
    }

    //
    @Test
    void testViewInventoryByStoreOwner() {
//        // Arrange

//
//        // Mock behavior if needed
//        when(userFacadeMock.viewShoppingCart(TOKEN)).thenReturn("shoppingCartData");
//
//        // Act
//        storeFacade.viewInventoryByStoreOwner(USER_ID, STORE_ID);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testAddItemToStore() {
//        // Arrange
//        String itemName = "Test Item";
//        double itemPrice = 10.99;
//        int stockAmount = 20;
//        long categoryId = 1;
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.addItemToStore(USER_ID, STORE_ID, itemName, itemPrice, stockAmount, categoryId);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testUpdateItem() {
//        // Arrange
//        long itemId = 1;
//        String newName = "Updated Item";
//        double newPrice = 15.99;
//        int stockAmount = 30;
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.updateItem(USER_ID, STORE_ID, itemId, newName, newPrice, stockAmount);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testDeleteItem() {
//        // Arrange
//        long itemId = 1;
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.deleteItem(USER_ID, STORE_ID, itemId);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testChangeStorePolicy() {
//        // Arrange
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.changeStorePolicy(USER_ID, STORE_ID);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testChangeDiscountType() {
//        // Arrange
//        String newType = "New Type";
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.changeDiscountType(USER_ID, STORE_ID, newType);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testAssignStoreOwner() {
//        // Arrange
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.assignStoreOwner(ACTOR_ID, USER_ID);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testAssignStoreManager() {
//        // Arrange
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.assignStoreManager(ACTOR_ID, USER_ID);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testRemoveStore() {
//        // Arrange
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.removeStore(USER_ID, STORE_ID);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testViewStoreManagementInfo() {
//        // Arrange
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.viewStoreManagementInfo(USER_ID, STORE_ID);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testViewPurchaseHistory() {
//        // Arrange
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.viewPurchaseHistory(USER_ID, STORE_ID);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testGetAllProductsInfoByStore() {
//        // Arrange
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.getAllProductsInfoByStore(STORE_ID);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testGetAllStoreInfo() {
//        // Arrange
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.getAllStoreInfo(STORE_ID);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testSearchStoreByName() {
//        // Arrange
//        String name = "Test Store";
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.searchStoreByName(name);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testSearchItemByName() {
//         Arrange
//        String name = "Test Item";
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.searchItemByName(name);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testSearchStoreByCategory() {
//        // Arrange
//        long category = 1;
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.searchStoreByCategory(category);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testSearchItemByCategory() {
//        // Arrange
//        long category = 1;
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.searchItemByCategory(category);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testSearchStoreByKeyWord() {
//        // Arrange
//        String keyWord = "Test";
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.searchStoreByKeyWord(keyWord);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testSearchItemByKeyWord() {
//        // Arrange
//        String keyWord = "Test";
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.searchItemByKeyWord(keyWord);
//
//        // Assert
//        // Implement assertions
    }

    @Test
    void testAddItemToShoppingBasket() {
//        // Arrange
//        long itemId = 1;
//
//        // Mock behavior if needed
//        when(userFacadeMock.isLoggedIn()).thenReturn(true);
//
//        // Act
//        storeFacade.addItemToShoppingBasket(USER_ID, STORE_ID, itemId);
//
//        // Assert
//        // Implement assertions
    }
}