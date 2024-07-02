//package UnitTests.DomainLayer.User;
//
//import DomainLayer.Market.Purchase.IPurchaseFacade;
//import DomainLayer.Market.ShoppingBasket;
//import DomainLayer.Market.Store.IStoreFacade;
//import DomainLayer.Market.User.ShoppingCart;
//import DomainLayer.Market.Util.IRepository;
//import DAL.ItemDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mockito;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class ShoppingCartUT {
//    private ShoppingCart shoppingCart;
//    private IRepository<Long, ShoppingBasket> basketRepositoryMock;
//    private IStoreFacade storeFacadeMock;
//
//    @BeforeEach
//    void setUp() {
//        basketRepositoryMock = mock(IRepository.class);
//        storeFacadeMock = mock(IStoreFacade.class);
//        shoppingCart = new ShoppingCart(basketRepositoryMock);
//    }
//
//    @Test
//    void test_viewShoppingCart_should_return_shopping_cart_string() throws Exception {
//        List<ShoppingBasket> baskets = new ArrayList<>();
//        ShoppingBasket basket = mock(ShoppingBasket.class);
//        baskets.add(basket);
//
//        when(basketRepositoryMock.findAll()).thenReturn(baskets);
//        when(basket.toString()).thenReturn("BasketContent");
//
//        String result = shoppingCart.viewShoppingCart(storeFacadeMock);
//
//        verify(storeFacadeMock).calculateBasketPrice(basket, null);
//        assertEquals("BasketContent", result);
//    }
//
//    @Test
//    void test_modifyShoppingCart_should_update_items_quantity() {
//        ShoppingBasket basket = mock(ShoppingBasket.class);
//        when(basketRepositoryMock.findById(1L)).thenReturn(basket);
//
//        shoppingCart.modifyShoppingCart(1L, 1001L, 5);
//
//        verify(basket).updateItemQuantity(1001L, 5);
//    }
//
//    @Test
//    void test_modifyShoppingCart_should_throw_exception_for_basket_not_found() {
//        when(basketRepositoryMock.findById(1L)).thenReturn(null);
//
//        assertThrows(IllegalArgumentException.class, () -> shoppingCart.modifyShoppingCart(1L, 1001L, 5));
//    }
//
//    @Test
//    void test_addItemBasket_should_add_items() throws Exception{
//        ShoppingBasket basket = mock(ShoppingBasket.class);
//        when(basketRepositoryMock.findAll()).thenReturn(List.of(basket));
//        when(basket.getStoreId()).thenReturn(1L);
//        when(storeFacadeMock.addItemToShoppingBasket(basket, 1L, 1001L, 5)).thenReturn(true);
//
//        assertDoesNotThrow(() ->shoppingCart.addItemBasket(1L, 1001L, 5, storeFacadeMock));
//
//        //verify(basket).addItem(1001L, 5);
//    }
//
//    @Test
//    void test_checkoutShoppingCart_should_return_items_in_shopping_cart() throws Exception {
//        ShoppingBasket basket = mock(ShoppingBasket.class);
//        when(basketRepositoryMock.findAll()).thenReturn(List.of(basket));
//        when(storeFacadeMock.checkValidBasket(basket, "user")).thenReturn(true);
//        when(basket.checkoutShoppingBasket(storeFacadeMock)).thenReturn(new ArrayList<>());
//
//        List<ItemDTO> items = shoppingCart.checkoutShoppingCart("user", storeFacadeMock, "code");
//
//        verify(storeFacadeMock).calculateBasketPrice(basket, "code");
//        assertTrue(items.isEmpty());
//    }
//
//    @Test
//    void test_checkoutShoppingCart_should_throe_exception_for_invalid_basket() throws InterruptedException {
//        ShoppingBasket basket = mock(ShoppingBasket.class);
//        when(basketRepositoryMock.findAll()).thenReturn(List.of(basket));
//        when(storeFacadeMock.checkValidBasket(basket, "user")).thenReturn(false);
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            shoppingCart.checkoutShoppingCart("user", storeFacadeMock, "code");
//        });
//
//        assertEquals("couldn't complete checkout- invalid basket", exception.getMessage());
//    }
//
//    @Test
//    void test_getBaskets_should_return_all_baskets_in_cart() {
//        List<ShoppingBasket> baskets = new ArrayList<>();
//        when(basketRepositoryMock.findAll()).thenReturn(baskets);
//
//        List<ShoppingBasket> result = shoppingCart.getBaskets();
//
//        assertSame(baskets, result);
//    }
//
//    @Test
//    void test_deleteShoppingBasket_should_delete_one_basket() {
//        shoppingCart.deleteShoppingBasket(1L);
//
//        verify(basketRepositoryMock).delete(1L);
//    }
//
//    @Test
//    void test_getShoppingCartPrice_should_return_cart_total_price() {
//        ShoppingBasket basket = mock(ShoppingBasket.class);
//        when(basketRepositoryMock.findAll()).thenReturn(List.of(basket));
//        when(basket.getBasketTotalPrice()).thenReturn(100.0);
//
//        double totalPrice = shoppingCart.getShoppingCartPrice();
//
//        assertEquals(100.0, totalPrice);
//    }
//
//    @Test
//    void test_clear_should_delete_all_baskets() {
//        ShoppingBasket basket = mock(ShoppingBasket.class);
//        when(basketRepositoryMock.findAll()).thenReturn(List.of(basket));
//        when(basket.getId()).thenReturn(1L);
//
//        shoppingCart.clear();
//
//        verify(basketRepositoryMock).delete(1L);
//    }
//}
