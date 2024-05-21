package UnitTests.DomainLayer.Store;

import DomainLayer.Market.Store.RegularDiscount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RegularDiscountUT {
    private final long DISCOUNT_ID1 = 1L;
    private final double DISCOUNT_PERCENT = 0.2;  // 20% discount
    private final long STORE_ID = 1L;
    private final List<Long> CONDITION_ITEMS = Arrays.asList(1L, 2L, 3L);

    private RegularDiscount conditionalDiscount;

    @BeforeEach
    void setUp() {
        Date expirationDate = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        conditionalDiscount = new RegularDiscount(DISCOUNT_ID1, DISCOUNT_PERCENT, expirationDate, STORE_ID, CONDITION_ITEMS);
    }

    @Test
    void testIsValidWithAllConditionItems() {
        List<Long> items = Arrays.asList(1L, 2L, 3L, 4L);
        assertTrue(conditionalDiscount.isValid(items));
    }

    @Test
    void testIsValidWithoutAllConditionItems() {
        List<Long> items = Arrays.asList(1L, 2L, 4L);
        assertFalse(conditionalDiscount.isValid(items));
    }

    @Test
    void testIsValidAfterExpirationDate() {
        Date pastDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        conditionalDiscount = new RegularDiscount(DISCOUNT_ID1, DISCOUNT_PERCENT, pastDate, STORE_ID, CONDITION_ITEMS);
        List<Long> items = Arrays.asList(1L, 2L, 3L, 4L);
        assertFalse(conditionalDiscount.isValid(items));
    }

    @Test
    void testCalculatePriceWithValidDiscount() {
        double originalPrice = 100.0;
        double expectedPrice = originalPrice * (1 - DISCOUNT_PERCENT);
        assertEquals(expectedPrice, conditionalDiscount.calculatePrice(originalPrice, ""));
    }

    @Test
    void testCalculatePriceWithZeroDiscount() {
        double originalPrice = 100.0;
        conditionalDiscount = new RegularDiscount(DISCOUNT_ID1, 0, new Date(), STORE_ID, CONDITION_ITEMS);
        assertEquals(originalPrice, conditionalDiscount.calculatePrice(originalPrice, ""));
    }

    @Test
    void testGetDiscountId() {
        assertEquals(DISCOUNT_ID1, conditionalDiscount.getId());
    }

}

