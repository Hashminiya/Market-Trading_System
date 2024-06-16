package UnitTests.DomainLayer.Store;

import DomainLayer.Market.Store.Discount.HiddenDiscount;
import DomainLayer.Market.Store.Discount.ICondition;
import DomainLayer.Market.Store.Discount.RegularDiscount;
import DomainLayer.Market.Store.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.cert.CertificateExpiredException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HiddenDiscountUT {
    private static final long DISCOUNT_ID = 1L;
    private static final long DISCOUNT2_ID = 2L;
    private static final double DISCOUNT_PERCENT = 0.2;  // 20% discount
    private static final long STORE_ID = 1L;
    private static final String CODE= "code";
    private final String INVALID_CODE = "INVALIDCODE";
    static Date FUTURE_EXPIRATION = new Date(125, 11, 31, 23, 59, 59); // December 31, 2025
    static Date PAST_EXPIRATION = new Date(120, 0, 1, 0, 0, 0); // January 1, 2020
    private static HiddenDiscount validDiscount;


    private static ICondition condition;

    static Item item1;
    static Item item2;

    @BeforeAll
    static void setUp() {
        condition = mock(ICondition.class);
        item1 = new Item(1L, "item1", "item1 description", List.of("c1","c2", "c3"));
        item2 = new Item(2L, "item2", "item2 description", List.of("c2"));
        validDiscount = new HiddenDiscount(DISCOUNT_ID, DISCOUNT_PERCENT, FUTURE_EXPIRATION, STORE_ID, List.of(1L, 2L), null,false,CODE);
    }

    @Test
    void test_isValid_should_return_true_for_validDiscount_and_correct_code() {
        assertTrue(validDiscount.isValid(Map.of(item1,1), CODE));
    }

    @Test
    void test_isValid_should_return_false_for_false_invalid_code() {
        assertFalse(validDiscount.isValid(Map.of(item1,1), INVALID_CODE));
    }

    @Test
    void test_calculatePrice_should_return_correct_map_after_discounts_for_validDiscount() throws InterruptedException {
        item1.setPrice(100);
        double expectedPrice = 100 * (1 - DISCOUNT_PERCENT);
        assertEquals(expectedPrice, validDiscount.calculatePrice(Map.of(item1, item1.getPrice()), Map.of(item1, 1),CODE).get(item1));
    }
}
