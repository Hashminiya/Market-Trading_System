package UnitTests.DomainLayer.User;

import SetUp.ApplicationTest;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.User.ShoppingCart;
import DAL.ItemDTO;
import DomainLayer.Repositories.BasketItemRepository;
import DomainLayer.Repositories.BasketRepository;
import SetUp.cleanUpDB;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ApplicationTest.class)
//@ActiveProfiles("test")
class ShoppingCartUT {

    @Mock
    private BasketRepository basketRepositoryMock;

    @Mock
    private BasketItemRepository basketItemRepositoryMock;

    @Mock
    private IStoreFacade storeFacadeMock;

    @InjectMocks
    private ShoppingCart shoppingCart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    public static void tearDown() {
        cleanUpDB.clearDB();
    }

    @Test
    void test_viewShoppingCart_should_return_shopping_cart_string() throws Exception {
        List<ShoppingBasket> baskets = new ArrayList<>();
        ShoppingBasket basket = mock(ShoppingBasket.class);
        baskets.add(basket);

        when(basket.toString()).thenReturn("BasketContent");

        shoppingCart.setShoppingBaskets(baskets);
        String result = shoppingCart.viewShoppingCart(storeFacadeMock);

        verify(storeFacadeMock).calculateBasketPrice(basket, null);
        assertEquals("BasketContent", result);
    }

    @Test
    void test_modifyShoppingCart_should_update_items_quantity() {
        ShoppingBasket basket = mock(ShoppingBasket.class);
        shoppingCart.setShoppingBaskets(List.of(basket));
        when(basket.getId()).thenReturn(1L);

        shoppingCart.modifyShoppingCart(1L, 1001L, 5);

        verify(basket).updateItemQuantity(1001L, 5);
    }

    @Test
    void test_modifyShoppingCart_should_throw_exception_for_basket_not_found() {
        when(basketRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> shoppingCart.modifyShoppingCart(1L, 1001L, 5));
    }

    @Test
    void test_checkoutShoppingCart_should_return_items_in_shopping_cart() throws Exception {
        ShoppingBasket basket = mock(ShoppingBasket.class);
        shoppingCart.setShoppingBaskets(List.of(basket));
        when(storeFacadeMock.checkValidBasket(any(), eq("user"))).thenReturn(true);
        when(basket.checkoutShoppingBasket(any())).thenReturn(new ArrayList<>());

        List<ItemDTO> items = shoppingCart.checkoutShoppingCart("user", storeFacadeMock, "code");

        verify(storeFacadeMock).calculateBasketPrice(any(), eq("code"));
        assertTrue(items.isEmpty());
    }

    @Test
    void test_checkoutShoppingCart_should_throw_exception_for_invalid_basket() throws InterruptedException {
        ShoppingBasket basket = mock(ShoppingBasket.class);
        shoppingCart.setShoppingBaskets(List.of(basket));
        when(storeFacadeMock.checkValidBasket(any(), eq("user"))).thenReturn(false);
        Exception exception = assertThrows(Exception.class, () -> {
            try {
                shoppingCart.checkoutShoppingCart("user", storeFacadeMock, "code");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        assertEquals("couldn't complete checkout- invalid basket", exception.getMessage());
    }

    @Test
    void test_getBaskets_should_return_all_baskets_in_cart() {
        List<ShoppingBasket> baskets = new ArrayList<>();

        shoppingCart.setShoppingBaskets(baskets);

        List<ShoppingBasket> result = shoppingCart.getBaskets();

        assertEquals(baskets, result);
    }

    @Test
    void test_deleteShoppingBasket_should_delete_one_basket() {
        ShoppingBasket basket = mock(ShoppingBasket.class);
        shoppingCart.setShoppingBaskets(List.of(basket));
        when(basket.getId()).thenReturn(1L);

        shoppingCart.deleteShoppingBasket(1L);

        verify(basketRepositoryMock).deleteById(1L);
    }

    @Test
    void test_getShoppingCartPrice_should_return_cart_total_price() {
        ShoppingBasket basket = mock(ShoppingBasket.class);
        shoppingCart.setShoppingBaskets(List.of(basket));
        when(basket.getBasketTotalPrice()).thenReturn(100.0);

        double totalPrice = shoppingCart.getShoppingCartPrice();

        assertEquals(100.0, totalPrice);
    }

    @Test
    void test_clear_should_delete_all_baskets() {
        ShoppingBasket basket = mock(ShoppingBasket.class);
        shoppingCart.setShoppingBaskets(List.of(basket));
        when(basket.getId()).thenReturn(1L);

        shoppingCart.clear();

        // Ensure the baskets are cleared in memory
        assertTrue(shoppingCart.getBaskets().isEmpty());

        // Note: Additional logic should be added to delete all from the database if needed
        // verify(basketRepositoryMock).deleteAll();
    }
}
