package UnitTests.DomainLayer.Store.PurchasePolicies;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.StorePurchasePolicy.PurchasePolicy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class PurchasePolicyUT {
    private static final long POLICY_ID = 1L;
    private static final String POLICY_NAME = "Test Policy";
    private static final long ITEM_ID1 = 1L;
    private static final long ITEM_ID2 = 2L;
    private static final List<Long> ITEM_IDS = List.of(ITEM_ID1, ITEM_ID2);
    private static final List<String> CATEGORIES = List.of("c1", "c2");
    private static final boolean IS_STORE = false;

    private static Item item1;
    private static Item item2;
    private static Item item3;

    @Mock
    private static PurchasePolicy policy;

    @BeforeAll
    static void setUp() {
        item1 = new Item(ITEM_ID1, "item1", "item1 description", List.of("c1", "c3"),8282);
        item2 = new Item(ITEM_ID2, "item2", "item2 description", List.of("c2"),8282);
        item3 = new Item(3L, "item3", "item3 description", List.of("c4"),8282);

        policy = new PurchasePolicy(POLICY_ID, POLICY_NAME, ITEM_IDS, CATEGORIES, IS_STORE) {
            @Override
            public boolean isValid(HashMap<Item, Integer> itemsInBasket, String userDetails) {
                // Mock implementation for test purposes
                return true;
            }
        };
    }

    @Test
    void test_isIncluded_should_return_true_for_item_in_itemsList() {
        assertTrue(policy.isIncluded(item1));
        assertTrue(policy.isIncluded(item2));
    }

    @Test
    void test_isIncluded_should_return_false_for_item_not_in_itemsList() {
        assertFalse(policy.isIncluded(item3));
    }

    @Test
    void test_isIncluded_should_return_true_for_item_in_categories() {
        assertTrue(policy.isIncluded(item1)); // item1 belongs to "c1"
        assertTrue(policy.isIncluded(item2)); // item2 belongs to "c2"
    }

    @Test
    void test_isIncluded_should_return_false_for_item_not_in_categories_or_itemsList() {
        Item itemNotIncluded = new Item(4L, "itemNotIncluded", "description", List.of("c5"),8282);
        assertFalse(policy.isIncluded(itemNotIncluded));
    }

    @Test
    void test_isValid_should_return_true_for_mock_implementation() {
        HashMap<Item, Integer> itemsInBasket = new HashMap<>();
        itemsInBasket.put(item1, 1);
        itemsInBasket.put(item2, 2);

        assertTrue(policy.isValid(itemsInBasket, "userDetails"));
    }

    @Test
    void test_isValid_should_return_false_for_empty_basket() {
        PurchasePolicy invalidPolicy = new PurchasePolicy(POLICY_ID, POLICY_NAME, ITEM_IDS, CATEGORIES, IS_STORE) {
            @Override
            public boolean isValid(HashMap<Item, Integer> itemsInBasket, String userDetails) {
                return itemsInBasket != null && !itemsInBasket.isEmpty();
            }
        };

        assertFalse(invalidPolicy.isValid(new HashMap<>(), "userDetails"));
    }
}
