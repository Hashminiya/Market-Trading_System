package AcceptanceTests;

import DomainLayer.Market.Store.Discount;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.User;
import DomainLayer.Market.User.UserController;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.InMemoryRepository;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.Purchase.PurchaseController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagementAT {

    private IUserFacade userFacade;
    private IRepository<String, User> userRepository;
    private IStoreFacade storeFacade;
    private IPurchaseFacade purchaseFacade;

    @BeforeEach
    public void setUp() {
        userRepository = new InMemoryRepository<>();
        purchaseFacade = new PurchaseController();
        storeFacade = new StoreController(new InMemoryRepository<>(), purchaseFacade, new UserController(userRepository, storeFacade, purchaseFacade));
        userFacade = new UserController(userRepository, storeFacade, purchaseFacade);
    }

    @Test
    public void testCreateGuestSession() {
        String guestName = userFacade.createGuestSession();
        assertNotNull(guestName);
        assertTrue(guestName.startsWith("guest"));
//        assertNotNull(userRepository.findById(guestName));
    }

    @Test
    public void testTerminateGuestSession() {
        String guestName = userFacade.createGuestSession();
        userFacade.terminateGuestSession(guestName);
        assertNull(userRepository.findById(guestName));
    }

    @Test
    public void testRegister() throws Exception {
        userFacade.register("newUser", "password123");
        User user = userRepository.findById("newUser");
        assertNotNull(user);
        assertEquals("newUser", user.getUserName());
    }

    @Test
    public void testLogin() throws Exception {
        userFacade.register("newUser", "password123");
        boolean loginResult = userFacade.login("newUser", "password123");
        assertTrue(loginResult);
    }

    @Test
    public void testLogout() throws Exception {
        userFacade.register("newUser", "password123");
        userFacade.login("newUser", "password123");
        userFacade.logout("newUser");
        User user = userRepository.findById("newUser");
        assertFalse(user.isLoggedIn());
    }

    @Test
    public void testViewShoppingCart() throws Exception {
        userFacade.register("newUser", "password123", 25);
        userFacade.login("newUser", "password123");
        String cart = userFacade.viewShoppingCart("newUser");
        assertNotNull(cart);
    }

    @Test
    public void testModifyShoppingCart() throws Exception {
        userFacade.register("newUser", "password123", 25);
        userFacade.login("newUser", "password123");

        // Add an item to the shopping cart
        userFacade.addItemToBasket("newUser", 1, 1, 3);

        // Modify the quantity of the item in the shopping cart
        userFacade.modifyShoppingCart("newUser", 1, 1, 5);

        // Verify that the item quantity has been updated
        String cart = userFacade.viewShoppingCart("newUser");
        assertTrue(cart.contains("itemId: 1, quantity: 5"));
    }

    @Test
    public void testCheckoutShoppingCart() throws Exception {
        // Register and login a new user
        userFacade.register("newUser", "password123", 25);
        userFacade.login("newUser", "password123");

        // Add an item to the shopping cart
        userFacade.addItemToBasket("newUser", 1, 1, 5);

        // Proceed to checkout
        Date expiryDate = new SimpleDateFormat("MM/yyyy").parse("12/2025");
        userFacade.checkoutShoppingCart("newUser", "1234567812345678", expiryDate, "123", "DISCOUNT10");

        // Verify that the shopping cart is now empty
        String cart = userFacade.viewShoppingCart("newUser");
        assertTrue(cart.isEmpty());

        // Verify that the purchase was successful by checking the purchase history or other relevant indicators
        Response purchaseHistoryResponse = storeFacade.viewPurchasesHistory("newUser");
        assertEquals(Response.Status.OK.getStatusCode(), purchaseHistoryResponse.getStatus());
        String purchaseHistory = purchaseHistoryResponse.getEntity().toString();
        assertTrue(purchaseHistory.contains("itemId: 1")); // Adjust based on actual purchase history content
    }


    @Test
    public void testCheckPermission() throws Exception {
        // Register and login a new user
        userFacade.register("newUser", "password123", 25);
        userFacade.login("newUser", "password123");

        // Create a new store
        storeFacade.createStore("newUser", "Store1", "Description1", new InMemoryRepository<Long, Discount>());

        // Initially, the user should not have any permissions for the store
        boolean permission = userFacade.checkPermission("newUser", 1, "ADD_ITEM");
        assertFalse(permission);

        // Assign the user as a store manager with specific permissions
        userFacade.assignStoreManager("newUser", 1, List.of("ADD_ITEM", "REMOVE_ITEM"));

        // Now the user should have the 'ADD_ITEM' permission
        permission = userFacade.checkPermission("newUser", 1, "ADD_ITEM");
        assertTrue(permission);

        // The user should also have the 'REMOVE_ITEM' permission
        permission = userFacade.checkPermission("newUser", 1, "REMOVE_ITEM");
        assertTrue(permission);

        // The user should not have the 'CHANGE_POLICY' permission
        permission = userFacade.checkPermission("newUser", 1, "CHANGE_POLICY");
        assertFalse(permission);
    }
    @Test
    public void testAssignStoreOwner() throws Exception {
        // Register and login the original store owner
        userFacade.register("originalOwner", "password123");
        userFacade.login("originalOwner", "password123");

        // Create a new store by the original owner
        storeFacade.createStore("originalOwner", "Store1", "Description1", new InMemoryRepository<Long, Discount>());

        // Register and login a new user
        userFacade.register("newUser", "password456");
        userFacade.login("newUser", "password456");

        // Assign the new user as a store owner by the original owner
        userFacade.assignStoreOwner("newUser", "Store1", 1L);

        // Check if the new user has the 'OWNER' permission for the store
        List<String> permissions = userFacade.getUserPermission("newUser", 1L);
        assertTrue(permissions.contains("OWNER"));
    }

    @Test
    public void testAssignStoreManager() throws Exception {
        // Register and login the original store owner
        userFacade.register("originalOwner", "password123", 25);
        userFacade.login("originalOwner", "password123");

        // Create a new store by the original owner
        storeFacade.createStore("originalOwner", "Store1", "Description1", new InMemoryRepository<Long, Discount>());

        // Register and login a new user
        userFacade.register("newUser", "password456");
        userFacade.login("newUser", "password456");

        // Assign the new user as a store manager by the original owner with specific permissions
        userFacade.assignStoreManager("newUser", "Store1", 1L, List.of("ADD_ITEM", "REMOVE_ITEM"));

        // Check if the new user has the 'ADD_ITEM' and 'REMOVE_ITEM' permissions for the store
        List<String> permissions = userFacade.getUserPermission("newUser", 1L);
        assertTrue(permissions.contains("ADD_ITEM"));
        assertTrue(permissions.contains("REMOVE_ITEM"));
    }


    @Test
    public void testTerminateGuest() {
        String guestName = userFacade.createGuestSession();
        int guestId = Integer.parseInt(guestName.replace("guest", ""));
        userFacade.terminateGuest(guestId);
        assertNull(userRepository.findById(guestName));
    }

    @Test
    public void testAddItemToBasket() {
        userFacade.register("newUser", "password123", 25);
        userFacade.login("newUser", "password123");
        userFacade.addItemToBasket("newUser", 1L, 1L, 3);
        String cart = userFacade.viewShoppingCart("newUser");
        assertTrue(cart.contains("itemId: 1, quantity: 3"));
    }

    @Test
    public void testChangeUserPermission() {
        userFacade.register("newUser", "password123", 25);
        userFacade.changeUserPermission("newUser", 2);
        User user = userRepository.findById("newUser");
        // Additional assertions to verify permission change
    }

//    @Test
//    public void testIsRegister() throws Exception {
//        // Create a guest session
//        String guestUserName = userFacade.createGuestSession();
//        assertFalse(userFacade.isRegister(guestUserName));
//
//        // Register a new user
//        userFacade.register("newUser", "password123");
//        assertTrue(userFacade.isRegister("newUser"));
//
//        // Ensure the guest user is still not registered
//        assertFalse(userFacade.isRegister(guestUserName));
//    }

}
