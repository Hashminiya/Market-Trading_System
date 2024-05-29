package UnitTests.DomainLayer.Store;

import DomainLayer.Market.Store.Discount;
import DomainLayer.Market.Store.HiddenDiscount;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.RegularDiscount;
import DomainLayer.Market.Util.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemUT {

    private final long ITEM_ID = 1L;
    private final String ITEM_NAME = "Test Item";
    private final String ITEM_DESCRIPTION = "Test Description";
    private final double ITEM_PRICE = 10.99;
    private final int ITEM_QUANTITY = 100;
    private final double DISCOUNT = 0.8;
    private final String CODE = "code";
    private final List<String> ITEM_CATEGORIES = List.of("Category1", "Category2");

    @Mock
    private InMemoryRepository<Long, Discount> discountsMock;

    private Item item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        discountsMock = new InMemoryRepository<Long,Discount>();
        item = new Item(ITEM_ID, ITEM_NAME, ITEM_DESCRIPTION, new InMemoryRepository<Long,Discount>(), ITEM_CATEGORIES);
        item.setPrice(ITEM_PRICE);
        item.setQuantity(ITEM_QUANTITY);
    }

    @Test
    void testGetId() {
        assertEquals(ITEM_ID, item.getId());
    }

    @Test
    void testGetName() {
        assertEquals(ITEM_NAME, item.getName());
    }

    @Test
    void testSetName() {
        String newName = "New Item Name";
        item.setName(newName);
        assertEquals(newName, item.getName());
    }

    @Test
    void testGetDescription() {
        assertEquals(ITEM_DESCRIPTION, item.getDescription());
    }

    @Test
    void testSetDescription() {
        String newDescription = "New Description";
        item.setDescription(newDescription);
        assertEquals(newDescription, item.getDescription());
    }

    @Test
    void testGetPrice() {
        assertEquals(ITEM_PRICE, item.getPrice());
    }

    @Test
    void testSetPrice() {
        double newPrice = 20.99;
        item.setPrice(newPrice);
        assertEquals(newPrice, item.getPrice());
    }

    @Test
    void testGetQuantity() {
        assertEquals(ITEM_QUANTITY, item.getQuantity());
    }

    @Test
    void testSetQuantity() {
        int newQuantity = 50;
        item.setQuantity(newQuantity);
        assertEquals(newQuantity, item.getQuantity());
    }

    @Test
    void testGetCategories() {
        assertEquals(ITEM_CATEGORIES, item.getCategories());
    }

    @Test
    void testGetDiscounts() {
        Discount discount = mock(Discount.class);
        item.setDiscount(discount);
        List<Discount> discounts = item.getDiscounts();
        assertEquals(1, discounts.size());
        assertEquals(discount, discounts.get(0));
    }

    @Test
    void testDecreaseQuantity() {
        int decreaseBy = 10;
        item.decrease(decreaseBy);
        assertEquals(ITEM_QUANTITY - decreaseBy, item.getQuantity());
    }

    @Test
    void testDecreaseQuantityThrowsExceptionWhenInsufficientQuantity() {
        int decreaseBy = 200;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> item.decrease(decreaseBy));
        assertEquals(String.format("failed to update %s amount", ITEM_NAME), exception.getMessage());
    }

    @Test
    void testGetCurrentPriceWithoutDiscount() {
        try {
            assertEquals(ITEM_PRICE, item.getCurrentPrice(""));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetCurrentPriceWithRegularDiscount() {
        Discount discount = mock(RegularDiscount.class);
        try {
            when(discount.calculatePrice(anyDouble(),anyString())).thenReturn(DISCOUNT*ITEM_PRICE);
            when(discount.isValid(any())).thenReturn(true);
            item.setDiscount(discount);
            double expectedPrice = DISCOUNT*ITEM_PRICE;
            assertEquals(expectedPrice, item.getCurrentPrice(CODE));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetCurrentPriceWithHiddenDiscount() {
        Discount discount = mock(HiddenDiscount.class);
        try {
            when(discount.calculatePrice(anyDouble(),anyString())).thenReturn(DISCOUNT*ITEM_PRICE);
            when(discount.isValid(any())).thenReturn(true);
            item.setDiscount(discount);
            double expectedPrice = DISCOUNT*ITEM_PRICE;
            assertEquals(expectedPrice, item.getCurrentPrice(CODE));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
