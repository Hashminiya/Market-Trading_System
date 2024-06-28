package UnitTests.DomainLayer.Store;

import DomainLayer.Market.Store.Discount.IDiscount;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.StorePurchasePolicy.PurchasePolicy;
import DomainLayer.Market.Util.IRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StoreUT {

    private final long STORE_ID = 1L;
    private final String FOUNDER_ID = "FOUNDER_NAME";
    private final long ITEM_ID_1 = 1L;
    private final long ITEM_ID_2 = 2L;
    private final String STORE_NAME = "Test Store";
    private final String STORE_DESCRIPTION = "Test Store Description";

    @Mock
    private IRepository<Long, IDiscount> discountsMock;
    @Mock
    private IRepository<Long, PurchasePolicy> policiesMock;

    private Store store;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        store = new Store(STORE_ID, FOUNDER_ID, STORE_NAME, STORE_DESCRIPTION, discountsMock, policiesMock);
    }

    @Test
    void test_assignOwner_should_addOwnerToStore() {
        String newOwnerId = "owner@mail.com";
        store.assignOwner(newOwnerId);
        assertTrue(store.getOwners().contains(newOwnerId));
    }

    @Test
    void test_assignManager_should_addManagerToStore() {
        String newManagerId = "manager@mail.com";
        store.assignManager(newManagerId);
        assertTrue(store.getManagers().contains(newManagerId));
    }

    @Test
    void test_setDescription_should_updateStoreDescription() {
        String newDescription = "New Description";
        store.setDescription(newDescription);
        assertEquals(newDescription, store.getDescription());
    }

    @Test
    void test_addItem_should_addNewItemToStore() throws InterruptedException {
        String itemName = "Test Item";
        double itemPrice = 10.99;
        int itemQuantity = 100;
        String itemDescription = "Item Description";
        List<String> itemCategories = List.of("Category1", "Category2");

        store.addItem(ITEM_ID_1, itemName, itemPrice, itemQuantity, itemDescription, itemCategories);
        List<Item> inventory = store.viewInventory();
        assertEquals(1, inventory.size());
        Item addedItem = inventory.get(0);
        assertEquals(itemName, addedItem.getName());
        assertEquals(itemPrice, addedItem.getPrice());
        assertEquals(itemQuantity, addedItem.getQuantity());
        assertEquals(itemDescription, addedItem.getDescription());
        assertEquals(itemCategories, addedItem.getCategories());
    }


    @Test
    void test_updateItem_should_updateStoreItemDetails() throws InterruptedException {
        store.addItem(ITEM_ID_1, "Old Item", 10.0, 10, "Old Description", List.of("Category1"));

        String newName = "New Item";
        double newPrice = 15.0;
        int newQuantity = 5;

        store.updateItem(ITEM_ID_1, newName, newPrice, newQuantity);

        Item updatedItem = store.viewInventory().get(0);
        assertEquals(newName, updatedItem.getName());
        assertEquals(newPrice, updatedItem.getPrice());
        assertEquals(newQuantity, updatedItem.getQuantity());
    }

    @Test
    void test_deleteItem_should_removeItemFromStore() throws InterruptedException {
        long itemId = 1L;
        store.addItem(ITEM_ID_1, "Test Item", 10.0, 10, "Test Description", List.of("Category1"));

        store.deleteItem(itemId);

        assertTrue(store.viewInventory().isEmpty());
    }

    @Test
    void test_isAvailable_should_returnItemAvailability() throws InterruptedException {
        store.addItem(ITEM_ID_1, "Test Item", 10.0, 10, "Test Description", List.of("Category1"));

        assertTrue(store.isAvailable(ITEM_ID_1, 5));
        assertFalse(store.isAvailable(ITEM_ID_1, 15));
    }

    @Test
    void test_decreaseAmount_should_updateItemQuantity() throws InterruptedException {
        store.addItem(ITEM_ID_1, "Test Item", 10.0, 10, "Test Description", List.of("Category1"));

        store.decreaseAmount(ITEM_ID_1, 5);

        assertEquals(5, store.viewInventory().get(0).getQuantity());
    }

    @Test
    void test_search_should_returnItemsMatchingKeyword() throws InterruptedException {
        String keyword = "Test";
        store.addItem(ITEM_ID_1, "Test Item", 10.0, 10, "Test Description", List.of("Category1"));
        store.addItem(ITEM_ID_2, "Another Item", 20.0, 20, "Another Description", List.of("Category2"));

        List<Item> result = store.search(keyword);
        assertEquals(1, result.size());
        assertEquals("Test Item", result.get(0).getName());
    }

    @Test
    void test_searchWithCategory_should_returnItemsMatchingKeywordAndCategory() throws InterruptedException {
        String category = "testcategory";
        String keyword = "Test";
        store.addItem(ITEM_ID_1, "Test Item", 10.0, 10, "Test Description", List.of(category));
        store.addItem(ITEM_ID_2, "Another Item", 20.0, 20, "Another Description", List.of("Category2"));

        List<Item> result = store.searchKeyWordWithCategory(category, keyword);
        assertEquals(1, result.size());
        assertEquals("Test Item", result.get(0).getName());
    }

    @Test
    void test_getAllCategories_should_returnAllCategories() throws InterruptedException {
        store.addItem(ITEM_ID_1, "Test Item", 10.0, 10, "Test Description", List.of("Category1", "Category2"));
        store.addItem(ITEM_ID_2, "Another Item", 20.0, 20, "Another Description", List.of("Category2", "Category3"));

        List<String> categories = store.getAllCategories();
        assertTrue(categories.contains("category1"));
        assertTrue(categories.contains("category2"));
        assertTrue(categories.contains("category3"));
    }
}
