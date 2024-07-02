//package UnitTests.DomainLayer.Store;
//
//import DomainLayer.Market.Store.Discount.Discount;
//import DomainLayer.Market.Store.Discount.HiddenDiscount;
//import DomainLayer.Market.Store.Item;
//import DomainLayer.Market.Store.Discount.RegularDiscount;
//import DomainLayer.Market.Util.InMemoryRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class ItemUT {
//
//    private final long ITEM_ID = 1L;
//    private final String ITEM_NAME = "Test Item";
//    private final String ITEM_DESCRIPTION = "Test Description";
//    private final double ITEM_PRICE = 10.99;
//    private final int ITEM_QUANTITY = 100;
//    private final double DISCOUNT = 0.8;
//    private final String CODE = "code";
//    private final List<String> ITEM_CATEGORIES = List.of("Category1", "Category2");
//
//    @Mock
//    private InMemoryRepository<Long, Discount> discountsMock;
//
//    private Item item;
//
//    @BeforeEach
//    void setUp() throws InterruptedException {
//        MockitoAnnotations.openMocks(this);
//        discountsMock = new InMemoryRepository<Long,Discount>();
//        item = new Item(ITEM_ID, ITEM_NAME, ITEM_DESCRIPTION, ITEM_CATEGORIES);
//        item.setPrice(ITEM_PRICE);
//        item.setQuantity(ITEM_QUANTITY);
//    }
//
//    @Test
//    void test_getId_should_returnItemId() {
//        assertEquals(ITEM_ID, item.getId());
//    }
//
//    @Test
//    void test_getName_should_returnItemName() {
//        assertEquals(ITEM_NAME, item.getName());
//    }
//
//    @Test
//    void test_setName_should_updateItemName() throws InterruptedException {
//        String newName = "New Item Name";
//        item.setName(newName);
//        assertEquals(newName, item.getName());
//    }
//
//    @Test
//    void test_getDescription_should_returnItemDescription() {
//        assertEquals(ITEM_DESCRIPTION, item.getDescription());
//    }
//
//    @Test
//    void test_setDescription_should_updateItemDescription() throws InterruptedException {
//        String newDescription = "New Description";
//        item.setDescription(newDescription);
//        assertEquals(newDescription, item.getDescription());
//    }
//
//    @Test
//    void test_getPrice_should_returnItemPrice() {
//        assertEquals(ITEM_PRICE, item.getPrice());
//    }
//
//    @Test
//    void test_setPrice_should_updateItemPrice() throws InterruptedException {
//        double newPrice = 20.99;
//        item.setPrice(newPrice);
//        assertEquals(newPrice, item.getPrice());
//    }
//
//    @Test
//    void test_getQuantity_should_returnItemQuantity() {
//        assertEquals(ITEM_QUANTITY, item.getQuantity());
//    }
//
//    @Test
//    void test_setQuantity_should_updateItemQuantity() throws InterruptedException {
//        int newQuantity = 50;
//        item.setQuantity(newQuantity);
//        assertEquals(newQuantity, item.getQuantity());
//    }
//
//    @Test
//    void test_getCategories_should_returnItemCategories() {
//        assertEquals(ITEM_CATEGORIES, item.getCategories());
//    }
//
//    @Test
//    void test_decreaseQuantity_should_reduceItemQuantity() throws InterruptedException{
//        int decreaseBy = 10;
//        item.decrease(decreaseBy);
//        assertEquals(ITEM_QUANTITY - decreaseBy, item.getQuantity());
//    }
//
//    @Test
//    void test_decreaseQuantity_should_throwExceptionWhenInsufficientQuantity() {
//        int decreaseBy = 200;
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> item.decrease(decreaseBy));
//        assertEquals(String.format("failed to update %s amount", ITEM_NAME), exception.getMessage());
//    }
//
//    @Test
//    void test_getCurrentPrice_should_returnItemPriceWithoutDiscount() {
//        try {
//            assertEquals(ITEM_PRICE, item.getCurrentPrice(""));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
