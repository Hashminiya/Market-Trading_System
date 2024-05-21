package UnitTests.DomainLayer.Store;

import DomainLayer.Market.Store.HiddenDiscount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.cert.CertificateExpiredException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class HiddenDiscountUT {
    private final long DISCOUNT_ID = 1L;
    private final double DISCOUNT_PERCENT = 0.2;  // 20% discount
    private final long STORE_ID = 1L;
    private final String DISCOUNT_CODE = "DISCOUNT2023";
    private final String INVALID_CODE = "INVALIDCODE";

    private HiddenDiscount hiddenDiscount;

    @BeforeEach
    void setUp() {
        Date expirationDate = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000); // 1 day from now
        hiddenDiscount = new HiddenDiscount(DISCOUNT_ID, DISCOUNT_PERCENT, expirationDate, STORE_ID, DISCOUNT_CODE);
    }

    @Test
    void testIsValidBeforeExpirationDate() {
        assertTrue(hiddenDiscount.isValid(null));
    }

    @Test
    void testIsValidAfterExpirationDate() {
        Date pastDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000); // 1 day before
        hiddenDiscount = new HiddenDiscount(DISCOUNT_ID, DISCOUNT_PERCENT, pastDate, STORE_ID, DISCOUNT_CODE);
        assertFalse(hiddenDiscount.isValid(null));
    }

    @Test
    void testCalculatePriceWithValidCode() {
        double originalPrice = 100.0;
        double expectedPrice = originalPrice * (1 - DISCOUNT_PERCENT);
        try {
            assertEquals(expectedPrice, hiddenDiscount.calculatePrice(originalPrice, DISCOUNT_CODE));
        } catch (Exception e) {
            fail("Exception should not be thrown with valid code");
        }
    }

    @Test
    void testCalculatePriceWithInvalidCode() {
        double originalPrice = 100.0;
        Exception exception = assertThrows(Exception.class, () -> hiddenDiscount.calculatePrice(originalPrice, INVALID_CODE));
        assertEquals("Invalid code for discount", exception.getMessage());
    }

    @Test
    void testCalculatePriceAfterExpirationDate() {
        double originalPrice = 100.0;
        Date pastDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000); // 1 day before
        hiddenDiscount = new HiddenDiscount(DISCOUNT_ID, DISCOUNT_PERCENT, pastDate, STORE_ID, DISCOUNT_CODE);
        Exception exception = assertThrows(CertificateExpiredException.class, () -> hiddenDiscount.calculatePrice(originalPrice, DISCOUNT_CODE));
        assertEquals("The Discount is has expired", exception.getMessage());
    }

    @Test
    void testCalculatePriceWithZeroDiscount() {
        double originalPrice = 100.0;
        hiddenDiscount = new HiddenDiscount(DISCOUNT_ID, 0, new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000), STORE_ID, DISCOUNT_CODE);
        try {
            assertEquals(originalPrice, hiddenDiscount.calculatePrice(originalPrice, DISCOUNT_CODE));
        } catch (Exception e) {
            fail("Exception should not be thrown with zero percent discount");
        }
    }

    @Test
    void testGetDiscountId() {
        assertEquals(DISCOUNT_ID, hiddenDiscount.getId());
    }
}
