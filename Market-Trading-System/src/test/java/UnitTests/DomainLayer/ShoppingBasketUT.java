package UnitTests.DomainLayer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import DAL.ItemDTO;
import DomainLayer.Market.Store.IStoreFacade;

public class ShoppingBasketUT {

    @Mock
    private IStoreFacade storeFacade;

    private ShoppingBasket basket;

    private final long ITEM_ID = 10L;
    private final long STORE_ID = 1L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        basket = new ShoppingBasket(STORE_ID);
    }

    @AfterEach
    void tearDown() {
        // Reset the singleton instance or any shared state here
        storeFacade.clear();  // Ensure resetInstance() method is available in StoreController
    }

    @Test
    public void test_addItem_should_add_item_to_basket_successfully() {
        int quantity = 2;

        basket.addItem(ITEM_ID, quantity);

        Map<Long, Integer> items = basket.getItems();
        assertEquals(1, items.size());
        assertEquals(quantity, items.get(ITEM_ID));
    }

    @Test
    public void test_removeItem_should_remove_item_from_basket_for_valid_item_id() {
        int quantity = 2;

        basket.addItem(ITEM_ID, quantity);
        basket.removeItem(ITEM_ID);

        Map<Long, Integer> items = basket.getItems();
        assertTrue(items.isEmpty());
    }

    @Test
    public void test_updateItemQuantity_should_increase_item_quantity() {
        int quantity1 = 2;
        int quantity2 = 1;

        basket.addItem(ITEM_ID, quantity1);
        basket.updateItemQuantity(ITEM_ID, quantity2);

        Map<Long, Integer> items = basket.getItems();
        assertEquals(quantity2, items.get(ITEM_ID));
    }

    @Test
    public void test_updateItemQuantity_should_remove_item_from_basket_when_quantity_zero() {
        int quantity = 2;

        basket.addItem(ITEM_ID, quantity);
        basket.updateItemQuantity(ITEM_ID, 0);

        Map<Long, Integer> items = basket.getItems();
        assertEquals(0, items.size());
    }

    @Test
    public void test_checkoutShoppingBasket_should_return_checked_out_items() throws InterruptedException {
        int quantity = 2;
        double price = 5.0;
        String itemName = "Test Item";

        HashMap<Long, String> productInfo = new HashMap<>();
        productInfo.put(ITEM_ID, itemName);

        basket.addItem(ITEM_ID, quantity);
        HashMap<Long, Double> itemPrices = new HashMap<>();
        itemPrices.put(ITEM_ID, price);
        basket.setItemsPrice(itemPrices);

        Item item = new Item(ITEM_ID, itemName, "description", new ArrayList<>());
        item.setPrice(price);
        item.setQuantity(quantity);
        when(storeFacade.getItem(ITEM_ID)).thenReturn(item);

        List<ItemDTO> checkoutItems = basket.checkoutShoppingBasket(storeFacade);

        assertEquals(1, checkoutItems.size());
        ItemDTO itemDTO = checkoutItems.get(0);
        assertEquals(ITEM_ID, itemDTO.getItemId());
        assertEquals(quantity, itemDTO.getQuantity());
        assertEquals(price, itemDTO.getTotalPrice());
        assertEquals(itemName, itemDTO.getName());
    }

    @Test
    public void test_setBasketTotalPrice_should_return_updated_total_price() {
        int quantity = 2;
        double price = 5.0;

        basket.addItem(ITEM_ID, quantity);
        HashMap<Long, Double> itemPrices = new HashMap<>();
        itemPrices.put(ITEM_ID, price);
        basket.setItemsPrice(itemPrices);

        double expectedTotalPrice = quantity * price;
        basket.setBasketTotalPrice(expectedTotalPrice);

        assertEquals(expectedTotalPrice, basket.getBasketTotalPrice());
    }


    @Test
    public void test_toString_should_return_valid_receipt_string() {
        int quantity = 2;
        double price = 5.0;
        String itemName = "Test Item";

        HashMap<Long, String> productInfo = new HashMap<>();
        productInfo.put(ITEM_ID, itemName);

        basket.addItem(ITEM_ID, quantity);
        HashMap<Long, Double> itemPrices = new HashMap<>();
        itemPrices.put(ITEM_ID, price);
        basket.setItemsPrice(itemPrices);
        basket.setBasketTotalPrice(quantity * price);

        when(storeFacade.getAllProductsInfoByStore(STORE_ID)).thenReturn(productInfo);

        String basketString = basket.toString();
        assertTrue(basketString.contains("Shopping Basket ID: " + basket.getId()));
        assertTrue(basketString.contains("Store ID: " + STORE_ID));
        assertTrue(basketString.contains("Item ID"));
        assertTrue(basketString.contains("Quantity"));
        assertTrue(basketString.contains("Price"));
        assertTrue(basketString.contains("Total"));
        assertTrue(basketString.contains(String.valueOf(ITEM_ID)));
        assertTrue(basketString.contains(String.valueOf(quantity)));
        assertTrue(basketString.contains(String.valueOf(price)));
        assertTrue(basketString.contains(String.valueOf(quantity * price)));
        assertTrue(basketString.contains("Total Basket Price: " + String.format("%.2f", quantity * price)));
    }

    @Test
    public void test_emptyBasketToString_should_return_empty_receipt_string() {
        String basketString = basket.toString();
        assertTrue(basketString.contains("Shopping Basket ID: " + basket.getId()));
        assertTrue(basketString.contains("Store ID: " + STORE_ID));
        assertTrue(basketString.contains("Items:\n"));
        assertTrue(basketString.contains("Total Basket Price: 0.00"));
    }
}
