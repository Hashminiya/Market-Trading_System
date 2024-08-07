package UnitTests.DomainLayer.Store;

import DomainLayer.Market.Store.Discount.BaseCondition;
import DomainLayer.Market.Store.Discount.HiddenDiscount;
import DomainLayer.Market.Store.Discount.ICondition;
import DomainLayer.Market.Store.Discount.RegularDiscount;
import DomainLayer.Market.Store.Item;
import SetUp.ApplicationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.cert.CertificateExpiredException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ApplicationTest.class)
public class RegularDiscountUT {
    private static final long DISCOUNT_ID = 1L;
    private static final double DISCOUNT_PERCENT = 0.2;  // 20% discount
    private static final long STORE_ID = 1L;
    private final String INVALID_CODE = "INVALIDCODE";
    static Date FUTURE_EXPIRATION = new Date(125, 11, 31, 23, 59, 59); // December 31, 2025
    static Date PAST_EXPIRATION = new Date(120, 0, 1, 0, 0, 0); // January 1, 2020
    private static RegularDiscount validDiscount;

    private static BaseCondition condition;

    static Item item1;
    static Item item2;

    @BeforeAll
    static void setUp() {
        condition = mock(BaseCondition.class);
        item1 = new Item(1L, "item1", "item1 description", List.of("c1", "c2", "c3"), STORE_ID);
        item2 = new Item(2L, "item2", "item2 description", List.of("c2"), STORE_ID);
        validDiscount = new RegularDiscount(
                DISCOUNT_ID,
                "Valid Discount", // name
                DISCOUNT_PERCENT,
                FUTURE_EXPIRATION,
                STORE_ID,
                List.of(1L, 2L),
                null, // categories
                false,
                condition
        );
    }

    @Test
    void test_isValid_should_return_true_for_validDiscount_and_true_condition() {
        when(condition.isValid(anyMap())).thenReturn(true);
        assertTrue(validDiscount.isValid(Map.of(item1,1), null));
    }

    @Test
    void test_isValid_should_return_false_for_false_condition() {
        when(condition.isValid(anyMap())).thenReturn(false);
        assertFalse(validDiscount.isValid(Map.of(item1,1), null));
    }

    @Test
    void test_calculatePrice_should_return_correct_map_after_discounts_for_validDiscount() throws InterruptedException {
        when(condition.isValid(anyMap())).thenReturn(true);
        item1.setPrice(100);
        double expectedPrice = 100 * (1 - DISCOUNT_PERCENT);
        assertEquals(expectedPrice, validDiscount.calculatePrice(Map.of(item1, item1.getPrice()), Map.of(item1, 1),null).get(item1));
    }

}
