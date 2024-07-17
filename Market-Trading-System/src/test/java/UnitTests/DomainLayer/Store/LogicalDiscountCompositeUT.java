package UnitTests.DomainLayer.Store;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import DomainLayer.Market.Store.Discount.*;
import DomainLayer.Market.Store.Item;
import SetUp.ApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest(classes = ApplicationTest.class)
public class LogicalDiscountCompositeUT {

    private LogicalDiscountComposite orComposite;
    private LogicalDiscountComposite andComposite;
    private LogicalDiscountComposite xorComposite;
    private BaseDiscount discount1;
    private BaseDiscount discount2;
    private Item item1;
    private Item item2;
    private Map<Item, Double> itemsPrices;
    private Map<Item, Integer> itemsCount;

    @BeforeEach
    public void setUp() {
        discount1 = mock(BaseDiscount.class);
        discount2 = mock(BaseDiscount.class);
        item1 = mock(Item.class);
        item2 = mock(Item.class);

        itemsPrices = new HashMap<>();
        itemsPrices.put(item1, 100.0);
        itemsPrices.put(item2, 200.0);

        itemsCount = new HashMap<>();
        itemsCount.put(item1, 1);
        itemsCount.put(item2, 2);

        List<BaseDiscount> discounts = new ArrayList<>();
        discounts.add(discount1);
        discounts.add(discount2);

        orComposite = new LogicalDiscountComposite(1L, "OR",discounts, "OR", 0);
        andComposite = new LogicalDiscountComposite(2L, "AND", discounts, "AND", 0);
        xorComposite = new LogicalDiscountComposite(3L, "XOR", discounts, "XOR", 0);
    }

    @Test
    public void testOrCalculatePrice() throws Exception {
        when(discount1.isValid(itemsCount, "CODE")).thenReturn(true);
        when(discount2.isValid(itemsCount, "CODE")).thenReturn(false);
        when(discount1.calculatePrice(itemsPrices, itemsCount, "CODE")).thenReturn(itemsPrices);

        Map<Item, Double> result = orComposite.calculatePrice(itemsPrices, itemsCount, "CODE");
        assertEquals(itemsPrices, result);
    }

    @Test
    public void testAndCalculatePrice() throws Exception {
        when(discount1.isValid(itemsCount, "CODE")).thenReturn(true);
        when(discount2.isValid(itemsCount, "CODE")).thenReturn(true);
        when(discount1.calculatePrice(itemsPrices, itemsCount, "CODE")).thenReturn(itemsPrices);
        when(discount2.calculatePrice(itemsPrices, itemsCount, "CODE")).thenReturn(itemsPrices);

        Map<Item, Double> result = andComposite.calculatePrice(itemsPrices, itemsCount, "CODE");
        assertEquals(itemsPrices, result);
    }

    @Test
    public void testXorCalculatePrice() throws Exception {
        when(discount1.isValid(itemsCount, "CODE")).thenReturn(true);
        when(discount2.isValid(itemsCount, "CODE")).thenReturn(false);
        when(discount1.calculatePrice(itemsPrices, itemsCount, "CODE")).thenReturn(itemsPrices);

        Map<Item, Double> result = xorComposite.calculatePrice(itemsPrices, itemsCount, "CODE");
        assertEquals(itemsPrices, result);
    }
}
