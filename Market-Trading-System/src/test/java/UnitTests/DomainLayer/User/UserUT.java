package UnitTests.DomainLayer.User;

import DAL.ItemDTO;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.User.*;
import DomainLayer.Market.Util.StoreEnum;
import DomainLayer.Market.Util.StorePermission;
import DomainLayer.Market.Util.StoreRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserUT {
    private User user;
    private final String USERNAME_TEST = "testUser";
    private final String TEST_PASSWORD = "password123";
    private final int TEST_AGE = 25;

    @Mock
    private ShoppingCart shoppingCart;

    @Mock
    private IStoreFacade storeFacade;

    @Mock
    private Istate state;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(USERNAME_TEST, TEST_PASSWORD, TEST_AGE, state, false, shoppingCart);
    }

    @AfterEach
    void tearDown() {
        // Reset the singleton instance or any shared state here
        storeFacade.clear();  // Ensure resetInstance() method is available in StoreController
    }

    @Test
    public void test_login_should_return_true() {
        user.setLoggedIn(false);
        when(state.isRegistered()).thenReturn(true);
        assertTrue(user.login());
        assertTrue(user.isRegister());
    }

    @Test
    public void test_login_should_throw_exception_for_user_who_already_logged_in() {
        user.setLoggedIn(true);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.login();
        });
        assertEquals("user already logged in", exception.getMessage());
    }

    @Test
    public void test_logout_should_return_true() {
        user.setLoggedIn(true);
        user.logout();
        assertFalse(user.isRegister());
    }

    @Test
    public void test_logout_should_throw_exception_for_user_who_not_logged_in() {
        user.setLoggedIn(false);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.logout();
        });
        assertEquals("user not logged in", exception.getMessage());
    }

    @Test
    public void test_deleteShoppingBasket_should_remove_shopping_basket() {
        user.setLoggedIn(true);
        user.deleteShoppingBasket(1L);
        verify(shoppingCart, times(1)).deleteShoppingBasket(1L);
    }

    @Test
    public void test_deleteShoppingBasket_should_throw_exception_for_user_who_not_logged_in() {
        user.setLoggedIn(false);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.deleteShoppingBasket(1L);
        });
        assertEquals("user not logged in", exception.getMessage());
    }

    @Test
    public void test_modifyShoppingCart_should_modify_shopping_basket() {
        user.setLoggedIn(true);
        user.modifyShoppingCart(1L, 2L, 3);
        verify(shoppingCart, times(1)).modifyShoppingCart(1L, 2L, 3);
    }

    @Test
    public void test_modifyShoppingCart_should_throw_exception_for_user_who_not_logged_in() {
        user.setLoggedIn(false);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.modifyShoppingCart(1L, 2L, 3);
        });
        assertEquals("user not logged in", exception.getMessage());
    }

    @Test
    public void test_getStorePermissions_should_return_store_permissions_for_user() {
        user.assignStoreManager(1L, Arrays.asList("VIEW_INVENTORY", "VIEW_STORE_MANAGEMENT_INFO"));
        List<String> permissions = user.getStorePermissions(1L);
        assertTrue(permissions.contains("VIEW_INVENTORY"));
        assertTrue(permissions.contains("VIEW_STORE_MANAGEMENT_INFO"));
    }

    @Test
    public void test_checkPermission_return_true_for_user_who_has_VIEW_INVENTORY_permission() {
        user.assignStoreManager(1L, Arrays.asList("VIEW_INVENTORY"));
        assertTrue(user.checkPermission(1L, StorePermission.VIEW_INVENTORY));
    }

    @Test
    public void test_checkPermission_should_throw_exception_for_user_who_not_has_VIEW_STORE_MANAGEMENT_INFO_permission() {
        user.assignStoreManager(1L, Arrays.asList("VIEW_INVENTORY"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.checkPermission(1L, StorePermission.VIEW_STORE_MANAGEMENT_INFO);
        });
        assertEquals("user does not have VIEW_STORE_MANAGEMENT_INFO permission for this store", exception.getMessage());
    }

    @Test
    public void test_assignStoreOwner_should_assign_store_owner_with_all_permissions() {
        user.assignStoreOwner(1L);
        Set<StoreEnum> permissions = new HashSet<>();
        permissions.add(StoreRole.OWNER);
        permissions.addAll(Arrays.asList(StorePermission.values()));
        List<String> permissionNames = user.getStorePermissions(1L);

        for (StoreEnum permission : permissions) {
            assertTrue(permissionNames.contains(permission.name()));
        }
    }

    @Test
    public void test_assignStoreOwner_should_throw_exception_when_user_is_already_store_owner() {
        user.assignStoreOwner(1L);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.assignStoreOwner(1L);
        });
        assertEquals("user is already store owner", exception.getMessage());
    }

    @Test
    public void test_assignStoreManager_should_assign_store_manager_with_specified_permissions() {
        user.assignStoreManager(1L, Arrays.asList("VIEW_INVENTORY", "VIEW_STORE_MANAGEMENT_INFO"));
        List<String> permissions = user.getStorePermissions(1L);
        assertTrue(permissions.contains("VIEW_INVENTORY"));
        assertTrue(permissions.contains("VIEW_STORE_MANAGEMENT_INFO"));
    }

    @Test
    public void test_assignStoreManager_should_throw_exception_when_user_is_already_store_manager() {
        user.assignStoreManager(1L, Arrays.asList("VIEW_INVENTORY", "VIEW_STORE_MANAGEMENT_INFO"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.assignStoreManager(1L, Arrays.asList("VIEW_INVENTORY"));
        });
        assertEquals("user is already store manager", exception.getMessage());
    }

    @Test
    public void test_addItemToBasket_should_call_ShppingCart_once() {
        user.setLoggedIn(true);
        user.addItemToBasket(1L, 2L, 3);
        verify(shoppingCart, times(1)).addItemBasket(1L, 2L, 3);
    }

    @Test
    public void test_addItemToBasket_should_throw_exception_when_user_logged_out() {
        user.setLoggedIn(false);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.addItemToBasket(1L, 2L, 3);
        });
        assertEquals("user not logged in", exception.getMessage());
    }

    @Test
    public void test_isRegister_should_return_true() {
        when(state.isRegistered()).thenReturn(true);
        assertTrue(user.isRegister());
    }

    @Test
    public void test_removePermission_remove_permission_for_manager() {
        user.assignStoreManager(1L, Arrays.asList("VIEW_INVENTORY", "VIEW_STORE_MANAGEMENT_INFO"));
        user.removePermission(1L, StorePermission.VIEW_INVENTORY);
        List<String> permissions = user.getStorePermissions(1L);
        assertFalse(permissions.contains("VIEW_INVENTORY"));
    }

    @Test
    public void test_addPermission_should_add_permission_manager() {
        user.assignStoreManager(1L, Arrays.asList("VIEW_STORE_MANAGEMENT_INFO"));
        user.addPermission(1L, StorePermission.VIEW_INVENTORY);
        List<String> permissions = user.getStorePermissions(1L);
        assertTrue(permissions.contains("VIEW_INVENTORY"));
    }

    @Test
    public void test_checkoutShoppingCart_should_return_list_of_checked_out_items() throws Exception {
        user.setLoggedIn(true);
        List<ItemDTO> items = Arrays.asList(mock(ItemDTO.class));
        when(shoppingCart.checkoutShoppingCart(USERNAME_TEST, storeFacade, "DISCOUNT")).thenReturn(items);

        List<ItemDTO> result = user.checkoutShoppingCart(storeFacade, "DISCOUNT");
        assertEquals(items, result);
        verify(shoppingCart, times(1)).checkoutShoppingCart(USERNAME_TEST, storeFacade, "DISCOUNT");
    }

    @Test
    public void test_checkoutShoppingCart_should_throw_exception_when_user_logged_out() {
        user.setLoggedIn(false);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.checkoutShoppingCart(storeFacade, "DISCOUNT");
        });
        assertEquals("user not logged in", exception.getMessage());
    }

    @Test
    public void test_gettersAndSetters_should_return_valid_user_info() {
        assertEquals(USERNAME_TEST, user.getUserName());
        assertEquals(TEST_PASSWORD, user.getPassword());
        assertEquals(TEST_AGE, user.getUserAge());

        user.setUserName("newUserName");
        user.setUserAge(30);
        Istate newState = mock(Istate.class);
        when(newState.isRegistered()).thenReturn(true);
        user.setUserState(newState);
        user.setLoggedIn(true);
        user.setShoppingCart(mock(ShoppingCart.class));

        assertEquals("newUserName", user.getUserName());
        assertEquals(30, user.getUserAge());
        assertTrue(user.isRegister());
    }
}
