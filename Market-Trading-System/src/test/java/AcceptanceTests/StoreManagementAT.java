package AcceptanceTests;

import DomainLayer.Market.Store.Discount.*;
import DomainLayer.Market.Util.LogicalRule;
import DomainLayer.Market.Store.Store;
import DomainLayer.Repositories.DiscountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class StoreManagementAT {

    private Store store;

    @Mock
    private DiscountRepository discountRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        store = new Store(1L, "founderId", "Test Store", "Test Description", null, discountRepository);
    }

    @Test
    public void testAddRegularDiscount() throws Exception {
        String discountDetails = "{ \"@type\": \"RegularDiscount\", \"id\": 10, \"percent\": 5.0, \"expirationDate\": \"2024-12-31T23:59:59Z\", \"storeId\": 1, \"items\": [1001, 1002], \"categories\": [\"Electronics\"], \"conditions\": { \"@type\": \"ConditionComposite\", \"conditions\": [{ \"@type\": \"Condition\", \"itemId\": 3001, \"count\": 2 }], \"rule\": \"AND\" } }";

        store.addDiscount(discountDetails);

        verify(discountRepository, times(1)).save(any(BaseDiscount.class));
    }

    @Test
    public void testAddHiddenDiscount() throws Exception {
        String discountDetails = "{ \"@type\": \"HiddenDiscount\", \"id\": 20, \"percent\": 10.0, \"expirationDate\": \"2024-12-31T23:59:59Z\", \"storeId\": 1, \"items\": [2001, 2002], \"categories\": [\"Books\"], \"code\": \"SECRET10\" }";

        store.addDiscount(discountDetails);

        verify(discountRepository, times(1)).save(any(BaseDiscount.class));
    }

    @Test
    public void testAddLogicalDiscountComposite() throws Exception {
        String discountDetails = "{ \"@type\": \"LogicalDiscountComposite\", \"id\": 30, \"name\": \"Composite Discount\", \"logicalRule\": \"AND\", \"decision\": 0, \"discounts\": [ { \"@type\": \"RegularDiscount\", \"id\": 31, \"percent\": 5.0, \"expirationDate\": \"2024-12-31T23:59:59Z\", \"storeId\": 1, \"items\": [3001, 3002], \"categories\": [\"Clothes\"] }, { \"@type\": \"RegularDiscount\", \"id\": 32, \"percent\": 10.0, \"expirationDate\": \"2024-12-31T23:59:59Z\", \"storeId\": 1, \"items\": [4001, 4002], \"categories\": [\"Toys\"] } ] }";

        store.addDiscount(discountDetails);

        verify(discountRepository, times(1)).save(any(BaseDiscount.class));
    }

    @Test
    public void testAddNumericDiscountComposite() throws Exception {
        String discountDetails = "{ \"@type\": \"NumericDiscountComposite\", \"id\": 40, \"discounts\": [ { \"@type\": \"RegularDiscount\", \"id\": 41, \"percent\": 5.0, \"expirationDate\": \"2024-12-31T23:59:59Z\", \"storeId\": 1, \"items\": [5001, 5002], \"categories\": [\"Sports\"] }, { \"@type\": \"RegularDiscount\", \"id\": 42, \"percent\": 10.0, \"expirationDate\": \"2024-12-31T23:59:59Z\", \"storeId\": 1, \"items\": [6001, 6002], \"categories\": [\"Outdoors\"] } ], \"numericRule\": \"MAX\" }";

        store.addDiscount(discountDetails);

        verify(discountRepository, times(1)).save(any(BaseDiscount.class));
    }

    @Test
    public void testAddDiscountInvalidDetails() {
        String invalidDiscountDetails = "{ \"@type\": \"InvalidDiscount\", \"id\": 50, \"percent\": 15.0 }";

        Exception exception = assertThrows(Exception.class, () -> store.addDiscount(invalidDiscountDetails));

        assertTrue(exception.getMessage().contains("Error while creating discount"));
        verify(discountRepository, never()).save(any(BaseDiscount.class));
    }

    @Test
    public void testConditionComposite() {
        ICondition condition1 = items -> items.getOrDefault(1001L, 0) >= 2;
        ICondition condition2 = items -> items.getOrDefault(1002L, 0) >= 1;

        ConditionComposite orCondition = new ConditionComposite(Arrays.asList(condition1, condition2), LogicalRule.OR);
        ConditionComposite andCondition = new ConditionComposite(Arrays.asList(condition1, condition2), LogicalRule.AND);
        ConditionComposite xorCondition = new ConditionComposite(Arrays.asList(condition1, condition2), LogicalRule.XOR);

        Map<Long, Integer> items = new HashMap<>();
        items.put(1001L, 2);
        items.put(1002L, 1);

        assertTrue(orCondition.isValid(items));
        assertTrue(andCondition.isValid(items));
        assertFalse(xorCondition.isValid(items));

        items.put(1002L, 0);
        assertTrue(orCondition.isValid(items));
        assertFalse(andCondition.isValid(items));
        assertTrue(xorCondition.isValid(items));
    }
}
