package UnitTests.DomainLayer.User;

import DomainLayer.Market.User.*;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DAL.ItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerUT {
    private final String USERNAME_TEST = "testUser";
    private final String TEST_PASSWORD = "password123";
    private final String INCORRECT_PASSWORD_MSG  = "wrong password";
    private final String USER_ALREADY_EXIST_MSG = "username already exists";
    private final String USER_NOT_EXIST_MSG = "user not exists";

    @Mock
    private IRepository<String, User> users;
    @Mock
    private IStoreFacade storeFacade;
    @Mock
    private IPurchaseFacade purchaseFacade;
    private BCryptPasswordEncoder passwordEncoder;
    private UserController userController;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
        resetUserControllerInstance();
        userController = UserController.getInstance(users);
        userController.setStoreFacade(storeFacade);
        userController.setPurchaseFacade(purchaseFacade);
    }

    private void resetUserControllerInstance() throws Exception {
        Field instance = UserController.class.getDeclaredField("userControllerInstance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void testCreateGuestSession() {
        String guestName = userController.createGuestSession();
        assertTrue(guestName.startsWith("guest"));
        verify(users, times(1)).save(any(User.class));
    }

    @Test
    public void testTerminateGuestSession() {
        String guestName = userController.createGuestSession();
        userController.terminateGuestSession(guestName);
        verify(users, times(1)).delete(guestName);
    }

    @Test
    public void testRegister() throws Exception {
        when(users.findById(USERNAME_TEST)).thenReturn(null);
        userController.register(USERNAME_TEST, TEST_PASSWORD, 25);
        verify(users, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        when(users.findById(USERNAME_TEST)).thenReturn(mock(User.class));
        Exception exception = assertThrows(Exception.class, () -> {
            userController.register(USERNAME_TEST, TEST_PASSWORD, 25);
        });
        assertEquals(USER_ALREADY_EXIST_MSG, exception.getMessage());
    }

    @Test
    public void testLogin() throws Exception {
        User user = mock(User.class);
        when(users.findById(USERNAME_TEST)).thenReturn(user);
        when(user.getPassword()).thenReturn(passwordEncoder.encode(TEST_PASSWORD));
        when(user.login()).thenReturn(true);

        assertTrue(userController.login(USERNAME_TEST, TEST_PASSWORD));
    }

    @Test
    public void testLoginWrongPassword() throws Exception {
        User user = mock(User.class);
        when(users.findById(USERNAME_TEST)).thenReturn(user);
        when(user.getPassword()).thenReturn(passwordEncoder.encode(TEST_PASSWORD));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userController.login(USERNAME_TEST, "wrongPassword");
        });
        assertEquals(INCORRECT_PASSWORD_MSG, exception.getMessage());
    }

    @Test
    public void testLoginUserNotExist() {
        when(users.findById(USERNAME_TEST)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userController.login(USERNAME_TEST, TEST_PASSWORD);
        });
        assertEquals(USER_NOT_EXIST_MSG, exception.getMessage());
    }

    @Test
    public void testLogout() {
        User user = mock(User.class);
        when(users.findById(USERNAME_TEST)).thenReturn(user);

        userController.logout(USERNAME_TEST);
        verify(user, times(1)).logout();
    }

    @Test
    public void testViewShoppingCart() {
        User user = mock(User.class);
        ShoppingCart cart = mock(ShoppingCart.class);
        when(users.findById(USERNAME_TEST)).thenReturn(user);
        when(user.getShoppingCart()).thenReturn(cart);
        when(cart.viewShoppingCart()).thenReturn("Shopping cart content");

        String content = userController.viewShoppingCart(USERNAME_TEST);
        assertEquals("Shopping cart content", content);
    }

    @Test
    public void testModifyShoppingCart() {
        User user = mock(User.class);
        when(users.findById(USERNAME_TEST)).thenReturn(user);

        userController.modifyShoppingCart(USERNAME_TEST, 1L, 2L, 3);
        verify(user, times(1)).modifyShoppingCart(1L, 2L, 3);
    }

    @Test
    public void testCheckoutShoppingCart() throws Exception {
        User user = mock(User.class);
        List<ItemDTO> items = Arrays.asList(mock(ItemDTO.class));
        when(users.findById(USERNAME_TEST)).thenReturn(user);
        when(user.checkoutShoppingCart(storeFacade, "DISCOUNT")).thenReturn(items);
        when(user.getShoppingCart()).thenReturn(mock(ShoppingCart.class));
        when(user.getShoppingCart().getShoppingCartPrice()).thenReturn(100.0);

        userController.checkoutShoppingCart(USERNAME_TEST, "1234567890123456", new Date(), "123", "DISCOUNT");

        verify(purchaseFacade, times(1)).checkout(eq(USERNAME_TEST), eq("1234567890123456"), any(Date.class), eq("123"), eq(items), eq(100.0));
        verify(storeFacade, times(1)).purchaseOccurs();
    }

    @Test
    public void testAssignStoreOwner() {
        User user = mock(User.class);
        when(users.findById(USERNAME_TEST)).thenReturn(user);

        userController.assignStoreOwner(USERNAME_TEST, 1L);
        verify(user, times(1)).assignStoreOwner(1L);
    }

    @Test
    public void testAssignStoreManager() {
        User user = mock(User.class);
        when(users.findById(USERNAME_TEST)).thenReturn(user);
        List<String> permissions = Arrays.asList("MANAGE_PRODUCTS", "VIEW_STATS");

        userController.assignStoreManager(USERNAME_TEST, 1L, permissions);
        verify(user, times(1)).assignStoreManager(1L, permissions);
    }

    @Test
    public void testAddItemToBasket() {
        User user = mock(User.class);
        when(users.findById(USERNAME_TEST)).thenReturn(user);

        userController.addItemToBasket(USERNAME_TEST, 1L, 2L, 3);
        verify(user, times(1)).addItemToBasket(1L, 2L, 3);
    }

    @Test
    public void testGetUserPermission() {
        User user = mock(User.class);
        when(users.findById(USERNAME_TEST)).thenReturn(user);
        List<String> permissions = Arrays.asList("MANAGE_PRODUCTS", "VIEW_STATS");
        when(user.getStorePermissions(1L)).thenReturn(permissions);

        List<String> result = userController.getUserPermission(USERNAME_TEST, 1L);
        assertEquals(permissions, result);
    }

    @Test
    public void testIsRegister() {
        User user = mock(User.class);
        when(users.findById(USERNAME_TEST)).thenReturn(user);
        when(user.isRegister()).thenReturn(true);

        assertTrue(userController.isRegister(USERNAME_TEST));
    }
}
