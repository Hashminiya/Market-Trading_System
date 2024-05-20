package UnitTests.DomainLayer.Store;

import DomainLayer.Market.Store.Discount;
import DomainLayer.Market.Store.Item;
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
    private final List<String> ITEM_CATEGORIES = List.of("Category1", "Category2");

    @Mock
    private InMemoryRepository<Long, Discount> discountsMock;

    private Item item;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        item = new Item(ITEM_ID, ITEM_NAME, ITEM_DESCRIPTION, discountsMock, ITEM_CATEGORIES);
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
    void testSetDiscount() {
        Discount discount = mock(Discount.class);
        item.setDiscount(discount);
        verify(discountsMock).save(discount);
    }

    @Test
    void testGetDiscounts() {
        Discount discount = mock(Discount.class);
        when(discountsMock.findAll()).thenReturn(List.of(discount));
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
        when(discountsMock.findAll()).thenReturn(List.of());
        assertEquals(ITEM_PRICE, item.getCurrentPrice());
    }

    @Test
    void testGetCurrentPriceWithDiscount() {
        Discount discount = mock(Discount.class);
        when(discount.apply(anyDouble())).thenAnswer(invocation -> (double) invocation.getArgument(0) * 0.9); // 10% discount
        when(discountsMock.findAll()).thenReturn(List.of(discount));

        double expectedPrice = ITEM_PRICE * 0.9;
        assertEquals(expectedPrice, item.getCurrentPrice());
    }
}
