import ServiceLayer.ServiceFactory;
import ServiceLayer.Store.StoreManagementService;
import ServiceLayer.Store.StoreBuyerService;
import ServiceLayer.User.UserService;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.logging.Logger;

public class MainApp {
    private static final Logger logger = Logger.getLogger(MainApp.class.getName());

    public static void main(String[] args) {
        // Services - the other option is to make an init, and ask for get()..
        StoreManagementService storeManagementService = ServiceFactory.createStoreManagementService();
        StoreBuyerService storeBuyerService = ServiceFactory.createStoreBuyerService();
        UserService userService = ServiceFactory.createUserService();

        // Run scenarios
        try {
            // User registration and login
            userService.register("Alice", "password123");
            userService.register("Bob", "password456");

            Response loginResponseAlice = userService.login("Alice", "password123");
            String aliceToken = (String) loginResponseAlice.getEntity();
            logger.info("Alice logged in with token: " + aliceToken);

            Response loginResponseBob = userService.login("Bob", "password456");
            String bobToken = (String) loginResponseBob.getEntity();
            logger.info("Bob logged in with token: " + bobToken);

            // Create a store
            storeManagementService.createStore(1, "Alice's Bookstore", "A place for books");

            // Add items to the store
            storeManagementService.addItemToStore(1, 1, "Harry Potter", 29.99, 100, Arrays.asList("Fiction"));
            storeManagementService.addItemToStore(1, 1, "The Hobbit", 19.99, 50, Arrays.asList("Fantasy"));

            // View all products in the store
            Response productsResponse = storeBuyerService.getAllProductsInfoByStore(1);
            logger.info("Products in Alice's Bookstore: " + productsResponse.getEntity().toString());

            // User adds item to their shopping cart
            userService.addItemToBasket(aliceToken, 1, 2); // Adding 2 copies of item with ID 1 (Harry Potter)

            // View shopping cart
            Response shoppingCartResponse = userService.viewShoppingCart(aliceToken);
            logger.info("Alice's shopping cart: " + shoppingCartResponse.getEntity().toString());

            // Checkout shopping cart
            userService.checkoutShoppingCart(aliceToken);
            logger.info("Alice's shopping cart checked out successfully");

            // Logout users
            userService.logout(aliceToken);
            userService.logout(bobToken);

        } catch (Exception e) {
            logger.severe("Error during scenario execution: " + e.getMessage());
        }
    }

}
