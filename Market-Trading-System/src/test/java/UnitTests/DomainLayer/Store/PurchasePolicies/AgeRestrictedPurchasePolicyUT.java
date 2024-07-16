package UnitTests.DomainLayer.Store.PurchasePolicies;


import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.StorePurchasePolicy.AgeRestrictedPurchasePolicy;
import DomainLayer.Market.User.IUserFacade;
import SetUp.ApplicationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ApplicationTest.class)
public class AgeRestrictedPurchasePolicyUT {

    private static final long POLICY_ID = 1L;
    private static final String POLICY_NAME = "Age Restricted Policy";
    private static final int MIN_AGE = 18;
    private static final List<Long> ITEM_IDS = List.of(1L, 2L);
    private static final List<String> CATEGORIES = List.of("alcohol");
    private static final boolean IS_STORE = false;
    private static final String UNDERAGE_USER = "underageUser";
    private static final String ADULT_USER = "adultUser";

    @Mock
    private static IUserFacade userFacade;

    private static AgeRestrictedPurchasePolicy ageRestrictedPurchasePolicy;
    private static Item item1;
    private static Item item2;

    @BeforeAll
    static void setUp() {
        userFacade = mock(IUserFacade.class);
        item1 = new Item(1L, "item1", "item1 description", List.of("alcohol"), 1L);
        item2 = new Item(2L, "item2", "item2 description", List.of("non-alcohol"), 1L);
        ageRestrictedPurchasePolicy = new AgeRestrictedPurchasePolicy(POLICY_NAME, POLICY_ID, MIN_AGE, ITEM_IDS, CATEGORIES, IS_STORE);
        ageRestrictedPurchasePolicy.setUserFacade(userFacade);

        // Mock user ages
        when(userFacade.getUserAge(UNDERAGE_USER)).thenReturn(16);
        when(userFacade.getUserAge(ADULT_USER)).thenReturn(20);
    }

    @Test
    void test_isValid_should_return_false_for_underage_user_with_restricted_item() {
        HashMap<Item, Integer> itemsInBasket = new HashMap<>();
        itemsInBasket.put(item1, 1);

        assertFalse(ageRestrictedPurchasePolicy.isValid(itemsInBasket, UNDERAGE_USER));
    }

    @Test
    void test_isValid_should_return_true_for_underage_user_with_non_restricted_item() {
        HashMap<Item, Integer> itemsInBasket = new HashMap<>();
        itemsInBasket.put(item2, 1);

        assertTrue(ageRestrictedPurchasePolicy.isValid(itemsInBasket, UNDERAGE_USER));
    }

    @Test
    void test_isValid_should_return_true_for_adult_user_with_restricted_item() {
        HashMap<Item, Integer> itemsInBasket = new HashMap<>();
        itemsInBasket.put(item1, 1);

        assertTrue(ageRestrictedPurchasePolicy.isValid(itemsInBasket, ADULT_USER));
    }

    @Test
    void test_isValid_should_return_true_for_adult_user_with_non_restricted_item() {
        HashMap<Item, Integer> itemsInBasket = new HashMap<>();
        itemsInBasket.put(item2, 1);

        assertTrue(ageRestrictedPurchasePolicy.isValid(itemsInBasket, ADULT_USER));
    }
}
