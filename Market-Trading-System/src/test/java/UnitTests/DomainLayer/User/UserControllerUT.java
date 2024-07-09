//package UnitTests.DomainLayer.User;
//
//import DomainLayer.Market.User.ShoppingCart;
//import DomainLayer.Market.User.UserController;
//import DomainLayer.Market.User.User;
//import DomainLayer.Market.Store.IStoreFacade;
//import DomainLayer.Market.Purchase.IPurchaseFacade;
//import DAL.ItemDTO;
//import DomainLayer.Repositories.UserRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.lang.reflect.Field;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class UserControllerUT {
//
//    private final String USERNAME_TEST = "testUser";
//    private final String TEST_PASSWORD = "password123";
//    private final String INCORRECT_PASSWORD_MSG = "wrong password";
//    private final String USER_ALREADY_EXIST_MSG = "username already exists";
//    private final String USER_NOT_EXIST_MSG = "user not exists";
//
//    @Autowired
//    private UserController userController;
//
//    @Autowired
//    private UserRepository users;
//
//    @Autowired
//    private IStoreFacade storeFacade;
//
//    @Autowired
//    private IPurchaseFacade purchaseFacade;
//
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @BeforeEach
//    public void setUp() throws Exception {
//        resetUserControllerInstance();
//        userController.clear();
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        userController.clear();
//        storeFacade.clear();
//        purchaseFacade.clear();
//    }
//
//    private void resetUserControllerInstance() throws Exception {
//        Field instance = UserController.class.getDeclaredField("userControllerInstance");
//        instance.setAccessible(true);
//        instance.set(null, null);
//    }
//
//    @Test
//    public void test_createGuestSession_should_return_guest_name() {
//        String guestName = userController.createGuestSession();
//        assertTrue(guestName.startsWith("guest"));
//        verify(users, times(1)).save(any(User.class));
//    }
//
//    @Test
//    public void test_terminateGuestSession_should_return_guest_name() {
//        String guestName = userController.createGuestSession();
//        userController.terminateGuestSession(guestName);
//
//        verify(users, times(1)).delete(guestName);
//    }
//
//    @Test
//    public void test_register_should_register_given_user() throws Exception {
//        when(users.existsById(USERNAME_TEST)).thenReturn(false);
//        userController.register(USERNAME_TEST, TEST_PASSWORD, 25);
//        verify(users, times(1)).save(any(User.class));
//    }
//
//    @Test
//    public void test_register_should_throw_exception_for_user_already_registered() {
//        when(users.existsById(USERNAME_TEST)).thenReturn(true);
//        Exception exception = assertThrows(Exception.class, () -> {
//            userController.register(USERNAME_TEST, TEST_PASSWORD, 25);
//        });
//        assertEquals(USER_ALREADY_EXIST_MSG, exception.getMessage());
//    }
//
////    @Test
////    public void test_login_should_return_true() throws Exception {
////        User user = new User();
////        user.setPassword(passwordEncoder.encode(TEST_PASSWORD));
////        when(users.findById(USERNAME_TEST)).thenReturn(java.util.Optional.of(user));
////        assertTrue(userController.login(USERNAME_TEST, TEST_PASSWORD));
////    }
////
////    @Test
////    public void test_login_should_throw_exception_for_invalid_password() throws Exception {
////        User user = new User();
////        user.setPassword(passwordEncoder.encode(TEST_PASSWORD));
////        when(users.findById(USERNAME_TEST)).thenReturn(java.util.Optional.of(user));
////        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
////            userController.login(USERNAME_TEST, "wrongPassword");
////        });
////        assertEquals(INCORRECT_PASSWORD_MSG, exception.getMessage());
////    }
//
//    @Test
//    public void test_login_should_throw_exception_for_user_doesnt_exist() {
//        when(users.findById(USERNAME_TEST)).thenReturn(java.util.Optional.empty());
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            userController.login(USERNAME_TEST, TEST_PASSWORD);
//        });
//        assertEquals(USER_NOT_EXIST_MSG, exception.getMessage());
//    }
//
//    @Test
//    public void test_logout_should_log_user_out() {
//        User user = new User();
//        when(users.findById(USERNAME_TEST)).thenReturn(java.util.Optional.of(user));
//        userController.logout(USERNAME_TEST);
//        verify(users, times(1)).save(user);
//    }
//
//    @Test
//    public void test_viewShoppingCart_return_shopping_cart_string() {
//        User user = new User();
//        ShoppingCart cart = mock(ShoppingCart.class);
//        user.setShoppingCart(cart);
//        when(users.findById(USERNAME_TEST)).thenReturn(java.util.Optional.of(user));
//        try {
//            when(cart.viewShoppingCart(storeFacade)).thenReturn("Shopping cart content");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        String content = assertDoesNotThrow(() -> userController.viewShoppingCart(USERNAME_TEST));
//        assertEquals("Shopping cart content", content);
//    }
//
//    @Test
//    public void test_modifyShoppingCart_should_call_function_once() {
//        User user = new User();
//        when(users.findById(USERNAME_TEST)).thenReturn(java.util.Optional.of(user));
//        userController.modifyShoppingCart(USERNAME_TEST, 1L, 2L, 3);
//        verify(users, times(1)).save(user);
//    }
//
//    @Test
//    public void test_checkoutShoppingCart_should_call_functions_once() throws Exception {
//        User user = new User();
//        List<ItemDTO> items = Arrays.asList(mock(ItemDTO.class));
//        when(users.findById(USERNAME_TEST)).thenReturn(java.util.Optional.of(user));
//        when(user.checkoutShoppingCart(storeFacade, "DISCOUNT")).thenReturn(items);
//        when(user.getShoppingCart().getShoppingCartPrice()).thenReturn(100.0);
//        userController.checkoutShoppingCart(USERNAME_TEST, "1234567890123456", new Date(), "123", "DISCOUNT");
//        verify(purchaseFacade, times(1)).checkout(eq(USERNAME_TEST), eq("1234567890123456"), any(Date.class), eq("123"), eq(items), eq(100.0));
//    }
//
//    @Test
//    public void test_assignStoreOwner_should_call_function_once() {
//        User assigneeUser = new User();
//        User assignerUser = new User();
//        when(users.findById(USERNAME_TEST)).thenReturn(java.util.Optional.of(assignerUser));
//        when(users.findById("assignee")).thenReturn(java.util.Optional.of(assigneeUser));
//        userController.assignStoreOwner(USERNAME_TEST, "assignee", 1L);
//        verify(users, times(1)).save(assigneeUser);
//    }
//
//    @Test
//    public void test_assignStoreManager_should_call_function_once() {
//        User assigneeUser = new User();
//        User assignerUser = new User();
//        List<String> permissions = Arrays.asList("MANAGE_PRODUCTS", "VIEW_STATS");
//        when(users.findById(USERNAME_TEST)).thenReturn(java.util.Optional.of(assignerUser));
//        when(users.findById("assignee")).thenReturn(java.util.Optional.of(assigneeUser));
//        userController.assignStoreManager(USERNAME_TEST, "assignee", 1L, permissions);
//        verify(users, times(1)).save(assigneeUser);
//    }
//
//    @Test
//    public void test_addItemToBasket_should_call_function_once() throws Exception {
//        User user = new User();
//        when(users.findById(USERNAME_TEST)).thenReturn(java.util.Optional.of(user));
//        userController.addItemToBasket(USERNAME_TEST, 1L, 2L, 3);
//        verify(users, times(1)).save(user);
//    }
//
//    @Test
//    public void test_getUserPermission_should_return_user_store_permissions() {
//        User user = new User();
//        List<String> permissions = Arrays.asList("MANAGE_PRODUCTS", "VIEW_STATS");
//        when(users.findById(USERNAME_TEST)).thenReturn(java.util.Optional.of(user));
//        when(user.getStorePermissions(1L)).thenReturn(permissions);
//        List<String> result = userController.getUserPermission(USERNAME_TEST, 1L);
//        assertEquals(permissions, result);
//    }
//
//    @Test
//    public void test_isRegister_should_return_true() {
//        User user = new User();
//        when(users.findById(USERNAME_TEST)).thenReturn(java.util.Optional.of(user));
//        when(user.isRegister()).thenReturn(true);
//        assertTrue(userController.isRegister(USERNAME_TEST));
//    }
//}
