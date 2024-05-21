package DomainLayer.Market;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import DAL.ItemDTO;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.DataItem;
import DomainLayer.Market.Util.IdGenerator;

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

    @Test
    public void testAddItem() {
        int quantity = 2;

        basket.addItem(ITEM_ID, quantity);

        Map<Long, Integer> items = basket.getItems();
        assertEquals(1, items.size());
        assertEquals(quantity, items.get(ITEM_ID));
    }

    @Test
    public void testRemoveItem() {
        int quantity = 2;

        basket.addItem(ITEM_ID, quantity);
        basket.removeItem(ITEM_ID);

        Map<Long, Integer> items = basket.getItems();
        assertTrue(items.isEmpty());
    }

    @Test
    public void testUpdateItemQuantity() {
        int quantity1 = 2;
        int quantity2 = 1;

        basket.addItem(ITEM_ID, quantity1);
        basket.updateItemQuantity(ITEM_ID, quantity2);

        Map<Long, Integer> items = basket.getItems();
        assertEquals(quantity2, items.get(ITEM_ID));
    }

    @Test
    public void testUpdateItemQuantityToZero() {
        int quantity = 2;

        basket.addItem(ITEM_ID, quantity);
        basket.updateItemQuantity(ITEM_ID, 0);

        Map<Long, Integer> items = basket.getItems();
        assertEquals(0, items.size());
    }

    @Test
    public void testCheckoutShoppingBasket() {
        int quantity = 2;
        double price = 5.0;
        String itemName = "Test Item";

        HashMap<Long, String> productInfo = new HashMap<>();
        productInfo.put(ITEM_ID, itemName);

        basket.addItem(ITEM_ID, quantity);
        HashMap<Long, Double> itemPrices = new HashMap<>();
        itemPrices.put(ITEM_ID, price);
        basket.setItemsPrice(itemPrices);

        when(storeFacade.getAllProductsInfoByStore(1L)).thenReturn(productInfo);

        List<ItemDTO> checkoutItems = basket.checkoutShoppingBasket(storeFacade);

        assertEquals(1, checkoutItems.size());
        ItemDTO item = checkoutItems.get(0);
        assertEquals(ITEM_ID, item.getItemId());
        assertEquals(quantity, item.getQuantity());
        assertEquals(price*quantity, item.getTotalPrice());
        assertEquals(itemName, item.getName());
    }

    @Test
    public void testBasketTotalPrice() {
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
    public void testToString() {
        int quantity = 2;
        double price = 5.0;
        String itemName = "Test Item";

        HashMap<Long, String> productInfo = new HashMap<>();
        productInfo.put(ITEM_ID, itemName);

        basket.addItem(ITEM_ID, quantity);
        HashMap<Long, Double> itemPrices = new HashMap<>();
        itemPrices.put(ITEM_ID, price);
        basket.setItemsPrice(itemPrices);

        when(storeFacade.getAllProductsInfoByStore(1L)).thenReturn(productInfo);
    }
}
