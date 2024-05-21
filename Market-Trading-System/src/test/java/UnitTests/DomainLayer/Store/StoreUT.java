package UnitTests.DomainLayer.Store;

import DomainLayer.Market.Store.Store;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.Discount;
import DomainLayer.Market.Util.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoreUT {

    private final long STORE_ID = 1L;
    private final String FOUNDER_ID = "FOUNDER_NAME";
    private final long ITEM_ID_1 = 1L;
    private final long ITEM_ID_2 = 2L;
    private final String STORE_NAME = "Test Store";
    private final String STORE_DESCRIPTION = "Test Store Description";

    @Mock
    private InMemoryRepository<Long, Discount> discountsMock;

    private Store store;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        store = new Store(STORE_ID, FOUNDER_ID, STORE_NAME, STORE_DESCRIPTION, discountsMock);
    }

    @Test
    void testGetId() {
        assertEquals(STORE_ID, store.getId());
    }

    @Test
    void testGetName() {
        assertEquals(STORE_NAME, store.getName());
    }

    @Test
    void testGetOwners() {
        assertNotNull(store.getOwners());
        assertTrue(store.getOwners().isEmpty());
    }

    @Test
    void testGetManagers() {
        assertNotNull(store.getManagers());
        assertTrue(store.getManagers().isEmpty());
    }

    @Test
    void testAssignOwner() {
        String newOwnerId = "owner@mail.com";
        store.assignOwner(newOwnerId);
        assertTrue(store.getOwners().contains(newOwnerId));
    }

    @Test
    void testAssignManager() {
        String newManagerId = "manager@mail.com";
        store.assignManager(newManagerId);
        assertTrue(store.getManagers().contains(newManagerId));
    }

    @Test
    void testSetDescription() {
        String newDescription = "New Description";
        store.setDescription(newDescription);
        assertEquals(newDescription, store.getDescription());
    }

    @Test
    void testAddItem() {
        String itemName = "Test Item";
        double itemPrice = 10.99;
        int itemQuantity = 100;
        String itemDescription = "Item Description";
        List<String> itemCategories = List.of("Category1", "Category2");

        store.addItem(ITEM_ID_1 ,itemName, itemPrice, itemQuantity, itemDescription, itemCategories);
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
    void testAddDiscountToItems() {
        long itemId1 = 1L;
        long itemId2 = 2L;
        Discount discount = mock(Discount.class);

        store.addItem(ITEM_ID_1, "Item1", 10.0, 10, "Description1", List.of("Category1"));
        store.addItem(ITEM_ID_2, "Item2", 20.0, 20, "Description2", List.of("Category2"));

        store.addDiscount(List.of(itemId1, itemId2), discount);
        assertEquals(discount, store.getById(ITEM_ID_1).getDiscounts().get(0));
        assertEquals(discount, store.getById(ITEM_ID_2).getDiscounts().get(0));
    }

    @Test
    void testAddStoreDiscount() {
        Discount discount = mock(Discount.class);
        store.addDiscount(discount);
        verify(discountsMock).save(discount);
    }

    @Test
    void testUpdateItem() {
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
    void testDeleteItem() {
        long itemId = 1L;
        store.addItem(ITEM_ID_1, "Test Item", 10.0, 10, "Test Description", List.of("Category1"));

        store.deleteItem(itemId);

        assertTrue(store.viewInventory().isEmpty());
    }

    @Test
    void testIsAvailable() {
        store.addItem(ITEM_ID_1, "Test Item", 10.0, 10, "Test Description", List.of("Category1"));

        assertTrue(store.isAvailable(ITEM_ID_1, 5));
        assertFalse(store.isAvailable(ITEM_ID_1, 15));
    }

    @Test
    void testUpdateAmount() {
        store.addItem(ITEM_ID_1, "Test Item", 10.0, 10, "Test Description", List.of("Category1"));

        store.updateAmount(ITEM_ID_1, 5);

        assertEquals(5, store.viewInventory().get(0).getQuantity());
    }

    @Test
    void testSearch() {
        String keyword = "Test";
        store.addItem(ITEM_ID_1,"Test Item", 10.0, 10, "Test Description", List.of("Category1"));
        store.addItem(ITEM_ID_2,"Another Item", 20.0, 20, "Another Description", List.of("Category2"));

        List<Item> result = store.search(keyword);
        assertEquals(1, result.size());
        assertEquals("Test Item", result.get(0).getName());
    }

    @Test
    void testSearchWithCategory() {
        String category = "Category1";
        String keyword = "Test";
        store.addItem(ITEM_ID_1, "Test Item", 10.0, 10, "Test Description", List.of(category));
        store.addItem(ITEM_ID_2, "Another Item", 20.0, 20, "Another Description", List.of("Category2"));

        List<Item> result = store.searchKeyWordWithCategory(category, keyword);
        assertEquals(1, result.size());
        assertEquals("Test Item", result.get(0).getName());
    }

    @Test
    void testGetAllCategories() {
        store.addItem(ITEM_ID_1, "Test Item", 10.0, 10, "Test Description", List.of("Category1", "Category2"));
        store.addItem(ITEM_ID_2, "Another Item", 20.0, 20, "Another Description", List.of("Category2", "Category3"));

        List<String> categories = store.getAllCategories();
        assertTrue(categories.contains("category1"));
        assertTrue(categories.contains("category2"));
        assertTrue(categories.contains("category3"));
    }
}
