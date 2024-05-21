package UnitTests.DomainLayer.User;

import DomainLayer.Market.User.UserController;
import DomainLayer.Market.User.User;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.User.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerUT {
    private final String USERNAME_TEST = "testUser";
    private final String TEST_PASSWORD = "password123";
    private final String SHOPPING_CART_CONTENT_MSG = "Shopping cart content";
    private final String INCORRECT_PASSWORD_MSG  = "password is incorrect";
    private final String USER_ALREADY_EXIST_MSG = "username already exists";
    private final String USER_NOT_EXIST_MSG = "user not exists";
    @Mock
    private IRepository<String, User> usersMock;

    @Mock
    private ShoppingCart cartMock;

    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(usersMock);
    }

    @Test
    void testCreateGuestSession() {
        userController.createGuestSession();
        verify(usersMock, times(1)).save(any(User.class));
    }

    @Test
    void testTerminateGuestSession() {
        String userName = TEST_PASSWORD;
        userController.terminateGuestSession(userName);
        verify(usersMock, times(1)).delete(userName);
    }

    @Test
    void testRegisterUserSuccessfully() throws Exception {
        String userName = USERNAME_TEST;
        String password = TEST_PASSWORD;
        int userAge = 25;

        when(usersMock.findById(userName)).thenReturn(null);
        userController.register(userName, password, userAge);
        verify(usersMock, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        String userName = USERNAME_TEST;
        String password = TEST_PASSWORD;
        int userAge = 25;

        when(usersMock.findById(userName)).thenReturn(new User());
        Exception exception = assertThrows(Exception.class, () -> {
            userController.register(userName, password, userAge);
        });
        assertEquals(USER_ALREADY_EXIST_MSG, exception.getMessage());
    }

    @Test
    void testLoginSuccessfully() throws Exception {
        String userName = USERNAME_TEST;
        String password = TEST_PASSWORD;
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        User user = new User(userName, encodedPassword, 25, null, false, cartMock);

        when(usersMock.findById(userName)).thenReturn(user);
        boolean loginResult = userController.login(userName, password);
        assertTrue(loginResult);
    }

    @Test
    void testLoginUserNotExists() {
        String userName = USERNAME_TEST;
        String password = TEST_PASSWORD;

        when(usersMock.findById(userName)).thenReturn(null);
        Exception exception = assertThrows(Exception.class, () -> {
            userController.login(userName, password);
        });
        assertEquals(USER_NOT_EXIST_MSG, exception.getMessage());
    }

    @Test
    void testLoginIncorrectPassword() {
        String userName = USERNAME_TEST;
        String password = TEST_PASSWORD;
        String incorrectPassword = INCORRECT_PASSWORD_MSG;
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        User user = new User(userName, encodedPassword, 25, null, false, cartMock);

        when(usersMock.findById(userName)).thenReturn(user);
        Exception exception = assertThrows(Exception.class, () -> {
            userController.login(userName, incorrectPassword);
        });
        assertEquals(INCORRECT_PASSWORD_MSG, exception.getMessage());
    }

    @Test
    void testLogout() {
        String userName = USERNAME_TEST;
        User user = mock(User.class);

        when(usersMock.findById(userName)).thenReturn(user);
        userController.logout(userName);
        verify(user, times(1)).logout(userName);
    }

    @Test
    void testViewShoppingCart() {
        String userName = USERNAME_TEST;
        ShoppingCart cart = mock(ShoppingCart.class);
        when(carts.get(userName)).thenReturn(cart);
        when(cart.viewShoppingCart()).thenReturn(SHOPPING_CART_CONTENT_MSG);

        String cartContent = userController.viewShoppingCart(userName);
        assertEquals(SHOPPING_CART_CONTENT_MSG, cartContent);
    }

    @Test
    void testCheckPermission() {
        String userName = USERNAME_TEST;
        boolean hasPermission = userController.checkPermission(userName);
        assertFalse(hasPermission);
    }

    @Test
    void testCheckPermissionWithStoreId() {
        String userName = USERNAME_TEST;
        Long storeId = 1L;
        String permission = "ASSIGN_OWNER";
        boolean hasPermission = userController.checkPermission(userName, storeId, permission);
        assertFalse(hasPermission);
    }

}
