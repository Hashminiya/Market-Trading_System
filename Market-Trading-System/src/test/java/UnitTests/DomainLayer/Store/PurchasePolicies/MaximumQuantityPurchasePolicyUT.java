package UnitTests.DomainLayer.Store.PurchasePolicies;

import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.StorePurchasePolicy.MaximumQuantityPurchasePolicy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MaximumQuantityPurchasePolicyUT {

    private static final long POLICY_ID = 1L;
    private static final String POLICY_NAME = "Maximum Quantity Policy";
    private static final int MAX_AMOUNT = 5;
    private static final List<Long> ITEM_IDS = List.of(1L, 2L);
    private static final List<String> CATEGORIES = List.of("category1", "category2");
    private static final boolean IS_STORE = false;

    private static MaximumQuantityPurchasePolicy maximumQuantityPurchasePolicy;
    private static Item item1;
    private static Item item2;

    @BeforeAll
    static void setUp() {
        item1 = new Item(1L, "item1", "item1 description", List.of("category1"),8282);
        item2 = new Item(2L, "item2", "item2 description", List.of("category2"),8282);
        maximumQuantityPurchasePolicy = new MaximumQuantityPurchasePolicy(POLICY_NAME, POLICY_ID, MAX_AMOUNT, ITEM_IDS, CATEGORIES, IS_STORE);
    }

    @Test
    void test_isValid_should_return_false_when_item_exceeds_maximum_quantity() {
        HashMap<Item, Integer> itemsInBasket = new HashMap<>();
        itemsInBasket.put(item1, 6);

        assertFalse(maximumQuantityPurchasePolicy.isValid(itemsInBasket, ""));
    }

    @Test
    void test_isValid_should_return_true_when_item_does_not_exceed_maximum_quantity() {
        HashMap<Item, Integer> itemsInBasket = new HashMap<>();
        itemsInBasket.put(item1, 4);

        assertTrue(maximumQuantityPurchasePolicy.isValid(itemsInBasket, ""));
    }

    @Test
    void test_isValid_should_return_true_when_item_quantity_is_equal_to_maximum() {
        HashMap<Item, Integer> itemsInBasket = new HashMap<>();
        itemsInBasket.put(item1, 5);

        assertTrue(maximumQuantityPurchasePolicy.isValid(itemsInBasket, ""));
    }

    @Test
    void test_isValid_should_return_true_for_items_not_in_policy_list() {
        Item item3 = new Item(3L, "item3", "item3 description", List.of("category3"),8282);
        HashMap<Item, Integer> itemsInBasket = new HashMap<>();
        itemsInBasket.put(item3, 10);

        assertTrue(maximumQuantityPurchasePolicy.isValid(itemsInBasket, ""));
    }

    @Test
    void test_isValid_should_return_true_for_empty_basket() {
        HashMap<Item, Integer> itemsInBasket = new HashMap<>();

        assertTrue(maximumQuantityPurchasePolicy.isValid(itemsInBasket, ""));
    }
}

