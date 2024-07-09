//package UnitTests.DomainLayer.Store;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import DomainLayer.Market.Store.Discount.*;
//import DomainLayer.Market.Store.Item;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.*;
//
//public class LogicalDiscountCompositeUT {
//
//    private LogicalDiscountComposite orComposite;
//    private LogicalDiscountComposite andComposite;
//    private LogicalDiscountComposite xorComposite;
//    private IDiscount discount1;
//    private IDiscount discount2;
//    private Item item1;
//    private Item item2;
//    private Map<Item, Double> itemsPrices;
//    private Map<Item, Integer> itemsCount;
//
//    @BeforeEach
//    public void setUp() {
//        discount1 = mock(IDiscount.class);
//        discount2 = mock(IDiscount.class);
//        item1 = mock(Item.class);
//        item2 = mock(Item.class);
//
//        itemsPrices = new HashMap<>();
//        itemsPrices.put(item1, 100.0);
//        itemsPrices.put(item2, 200.0);
//
//        itemsCount = new HashMap<>();
//        itemsCount.put(item1, 1);
//        itemsCount.put(item2, 2);
//
//        orComposite = new LogicalDiscountComposite(1L, Arrays.asList(discount1, discount2), "OR", 0);
//        andComposite = new LogicalDiscountComposite(2L, Arrays.asList(discount1, discount2), "AND", 0);
//        xorComposite = new LogicalDiscountComposite(3L, Arrays.asList(discount1, discount2), "XOR", 0);
//    }
//
//    @Test
//    public void testOrCalculatePrice() throws Exception {
//        when(discount1.isValid(itemsCount, "CODE")).thenReturn(true);
//        when(discount2.isValid(itemsCount, "CODE")).thenReturn(false);
//        when(discount1.calculatePrice(itemsPrices, itemsCount, "CODE")).thenReturn(itemsPrices);
//
//        Map<Item, Double> result = orComposite.calculatePrice(itemsPrices, itemsCount, "CODE");
//        assertEquals(itemsPrices, result);
//    }
//
//    @Test
//    public void testAndCalculatePrice() throws Exception {
//        when(discount1.isValid(itemsCount, "CODE")).thenReturn(true);
//        when(discount2.isValid(itemsCount, "CODE")).thenReturn(true);
//        when(discount1.calculatePrice(itemsPrices, itemsCount, "CODE")).thenReturn(itemsPrices);
//        when(discount2.calculatePrice(itemsPrices, itemsCount, "CODE")).thenReturn(itemsPrices);
//
//        Map<Item, Double> result = andComposite.calculatePrice(itemsPrices, itemsCount, "CODE");
//        assertEquals(itemsPrices, result);
//    }
//
//    @Test
//    public void testXorCalculatePrice() throws Exception {
//        when(discount1.isValid(itemsCount, "CODE")).thenReturn(true);
//        when(discount2.isValid(itemsCount, "CODE")).thenReturn(false);
//        when(discount1.calculatePrice(itemsPrices, itemsCount, "CODE")).thenReturn(itemsPrices);
//
//        Map<Item, Double> result = xorComposite.calculatePrice(itemsPrices, itemsCount, "CODE");
//        assertEquals(itemsPrices, result);
//    }
//}
