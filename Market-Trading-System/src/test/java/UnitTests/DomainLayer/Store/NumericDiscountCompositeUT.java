package UnitTests.DomainLayer.Store;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import DomainLayer.Market.Store.Discount.*;
import DomainLayer.Market.Store.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class NumericDiscountCompositeUT {

    private NumericDiscountComposite maxComposite;
    private NumericDiscountComposite addComposite;
    private IDiscount discount1;
    private IDiscount discount2;
    private Item item1;
    private Item item2;
    private Map<Item, Double> itemsPrices;
    private Map<Item, Integer> itemsCount;

    @BeforeEach
    public void setUp() {
        discount1 = mock(IDiscount.class);
        discount2 = mock(IDiscount.class);
        item1 = mock(Item.class);
        item2 = mock(Item.class);

        itemsPrices = new HashMap<>();
        itemsPrices.put(item1, 100.0);
        itemsPrices.put(item2, 200.0);

        itemsCount = new HashMap<>();
        itemsCount.put(item1, 1);
        itemsCount.put(item2, 2);

        maxComposite = new NumericDiscountComposite(1L, Arrays.asList(discount1, discount2), "MAX");
        addComposite = new NumericDiscountComposite(2L, Arrays.asList(discount1, discount2), "ADD");
    }

    @Test
    public void testMaxCalculatePrice() throws Exception {
        when(discount1.calculatePrice(itemsPrices, itemsCount, "CODE")).thenReturn(itemsPrices);
        Map<Item, Double> higherPrice = new HashMap<>();
        higherPrice.put(item1, 120.0);
        higherPrice.put(item2, 240.0);
        when(discount2.calculatePrice(itemsPrices, itemsCount, "CODE")).thenReturn(higherPrice);

        Map<Item, Double> result = maxComposite.calculatePrice(itemsPrices, itemsCount, "CODE");
        assertEquals(higherPrice, result);
    }

    @Test
    public void testAddCalculatePrice() throws Exception {
        Map<Item, Double> discount1Prices = new HashMap<>();
        discount1Prices.put(item1, 80.0);
        discount1Prices.put(item2, 160.0);

        Map<Item, Double> discount2Prices = new HashMap<>();
        discount2Prices.put(item1, 60.0);
        discount2Prices.put(item2, 120.0);

        Map<Item, Double> discount1Percents = new HashMap<>();
        discount1Percents.put(item1, 20.0);
        discount1Percents.put(item2, 20.0);

        Map<Item, Double> discount2Percents = new HashMap<>();
        discount2Percents.put(item1, 40.0);
        discount2Percents.put(item2, 40.0);

        when(discount1.calculatePrice(itemsPrices, itemsCount, "CODE")).thenReturn(discount1Prices);
        when(discount2.calculatePrice(itemsPrices, itemsCount, "CODE")).thenReturn(discount2Prices);
        when(discount1.getPercent(itemsPrices, itemsCount, "CODE")).thenReturn(discount1Percents);
        when(discount2.getPercent(itemsPrices, itemsCount, "CODE")).thenReturn(discount2Percents);


        Map<Item, Double> result = addComposite.calculatePrice(itemsPrices, itemsCount, "CODE");
        Map<Item, Double> expectedPrices = new HashMap<>();
        expectedPrices.put(item1, 40.0);
        expectedPrices.put(item2, 80.0);

        assertEquals(expectedPrices, result);
    }
}

