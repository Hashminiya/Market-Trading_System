package ServiceLayer.User;

import java.net.*;
import java.sql.SQLException;
import API.InitCommand;
import API.SpringContext;
import DAL.ItemDTO;
import DomainLayer.Market.Notifications.Event;
import DomainLayer.Market.Notifications.Publisher;
import DAL.ShoppingCartDTO;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.ShoppingCart;
import DomainLayer.Market.Util.JwtService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service("userService")
public class UserService implements IUserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Value("${logging.include-exception:false}")
    private boolean includeException;

    private IUserFacade userFacade;
    private JwtService jwtService;
    private static UserService instance;

    private UserService(@Qualifier("userController") IUserFacade userFacade) {
        this.userFacade = userFacade;
        this.jwtService = new JwtService();

    }

    public static synchronized UserService getInstance(IUserFacade userFacade) {
        if (instance == null) {
            instance = new UserService(userFacade);
        }
        return instance;
    }

    public void clear() {
        userFacade.clear();
        instance = null;
    }

    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @InitCommand(name = "guestEntry")
    public ResponseEntity<String> guestEntry() {
        try {
            String userName = userFacade.createGuestSession();
            String token = jwtService.generateToken(userName, "GUEST");
            logger.info("Guest session created for user: {}", userName);
            return ResponseEntity.ok(token);
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to create guest session due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception e) {
            logException("Error creating guest session", e);
            return ResponseEntity.status(500).body("Error creating guest session");
        }
    }

    @InitCommand(name = "guestExit")
    public ResponseEntity<String> guestExit(String token) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userFacade.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                userFacade.terminateGuestSession(userName);
                logger.info("Guest session terminated for user: {}", userName);
                return ResponseEntity.ok("Guest session terminated successfully");
            } else {
                logger.warn("Invalid token for guest exit: {}", token);
                return ResponseEntity.status(401).body("Invalid token for guest exit");
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to terminate guest session due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception e) {
            logException("Error terminating guest session", e);
            return ResponseEntity.status(500).body("Error terminating guest session");
        }
    }

    @InitCommand(name = "register")
    public ResponseEntity<String> register(String userName, String password, int userAge) {
        try {
            userFacade.register(userName, password, userAge);
            logger.info("User registered: {}", userName);
            return ResponseEntity.ok("User registered successfully");
        } catch (CannotCreateTransactionException | DataAccessException | SocketTimeoutException | SQLException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to register user due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception e) {
            logException(String.format("Error registering user: %s" ,userName), e);
            return ResponseEntity.status(500).body(String.format("Error registering user: %s\n%s", userName, e.getMessage()));
        }
    }

    @InitCommand(name = "login")
    public ResponseEntity<String> login(String userName, String password) {
        try {
            userFacade.login(userName, password);
            String token = jwtService.generateToken(userName, "REGISTERED");
            logger.info("User logged in: {}", userName);
            return ResponseEntity.ok(token);
        } catch (CannotCreateTransactionException | DataAccessException | SocketTimeoutException | SQLException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to login user due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception e) {
            logException(String.format("Error logging in user: %s", userName), e);
            return ResponseEntity.status(500).body(String.format("Error logging in user %s- %s", userName, e.getMessage()));
        }
    }

    @InitCommand(name = "logout")
    public ResponseEntity<String> logout(String token) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userFacade.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                userFacade.logout(userName);
                logger.info("User logged out: {}", userName);
                return ResponseEntity.ok(String.format("User %s logged out", userName));
            } else {
                logger.warn("Invalid token for logout: {}", token);
                return ResponseEntity.status(401).body(token);
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to logout user due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception e) {
            logException("Error logging out user", e);
            return ResponseEntity.status(500).body("Error logging out user");
        }
    }

    public ResponseEntity<String> viewShoppingCart(String token) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userFacade.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                logger.info("Viewing shopping cart for user: {}", userName);
                return ResponseEntity.ok(userFacade.viewShoppingCart(userName));
            } else {
                logger.warn("Invalid token for viewing shopping cart: {}", token);
                return ResponseEntity.status(401).body(token);
            }
        } catch (CannotCreateTransactionException | DataAccessException | SocketTimeoutException | SQLException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to view shopping cart due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception e) {
            logException("Error viewing shopping cart", e);
            return ResponseEntity.status(500).body("Error viewing shopping cart");
        }
    }

    @Override
    @InitCommand(name = "modifyShoppingCart")
    public ResponseEntity<String> modifyShoppingCart(String token, long basketId, long itemId, int newQuantity) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userFacade.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                userFacade.modifyShoppingCart(userName, basketId, itemId, newQuantity);
                logger.info("Modified shopping cart for user: {}", userName);
                return ResponseEntity.ok(String.format("Modified shopping cart for user %s", userName));
            } else {
                logger.warn("Invalid token for modifying shopping cart: {}", token);
                return ResponseEntity.status(401).body(token);
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to modify shopping cart due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception e) {
            logException("Error modifying shopping cart", e);
            return ResponseEntity.status(500).body("Error modifying shopping cart");
        }
    }

    @Override
    @InitCommand(name = "addItemToBasket")
    public ResponseEntity<String> addItemToBasket(String token, long storeId, long itemId, int quantity) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userFacade.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                logger.info("Added item to basket for user: {}", userName);
                return ResponseEntity.ok(userFacade.addItemToBasket(userName, storeId, itemId, quantity).toString());
            } else {
                logger.warn("Invalid token for adding item to basket: {}", token);
                return ResponseEntity.status(401).body(token);
            }
        } catch (CannotCreateTransactionException | DataAccessException | SocketTimeoutException | SQLException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to add item to basket due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception e) {
            logException("Error adding item to basket", e);
            return ResponseEntity.status(500).body("Error adding item to basket");
        }
    }

    @Override
    @InitCommand(name = "addPermission")
    public ResponseEntity<String> addPermission(String token, String userToPermit, long storeId, String permission) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userFacade.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                userFacade.addPermission(userName, userToPermit, storeId, permission);
                logger.info("Added permission for user: {}", userName);
                return ResponseEntity.ok(String.format("Added permission for user %s", userName));
            } else {
                logger.warn("Invalid token for adding permission: {}", token);
                return ResponseEntity.status(401).body(token);
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to add permission due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception e) {
            logException("Error adding permission", e);
            return ResponseEntity.status(500).body("Error adding permission");
        }
    }

    @Override
    @InitCommand(name = "removePermission")
    public ResponseEntity<String> removePermission(String token, String userToUnPermit, long storeId, String permission) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userFacade.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                userFacade.removePermission(userName, userToUnPermit, storeId, permission);
                logger.info("Removed permission for user: {}", userName);
                return ResponseEntity.ok(String.format("Removed permission for user %s", userName));
            } else {
                logger.warn("Invalid token for removing permission: {}", token);
                return ResponseEntity.status(401).body(token);
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to remove permission due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception e) {
            logException("Error removing permission", e);
            return ResponseEntity.status(500).body("Error removing permission");
        }
    }

    @Override
    public ResponseEntity<?> getShoppingCart(String token) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userFacade.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                ShoppingCart shoppingCart = userFacade.getShoppingCart(userName);
                ShoppingCartDTO shoppingCartDTO = shoppingCart.getShoppingCartDTO(SpringContext.getBean(StoreController.class));
                logger.info("Shopping cart retrieved for user: {}", userName);
                return ResponseEntity.ok(shoppingCartDTO);
            } else {
                logger.warn("Invalid token for getting shopping cart: {}", token);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (CannotCreateTransactionException | DataAccessException | SocketTimeoutException | SQLException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to get shopping cart due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception e) {
            logException("Error getting shopping cart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<Long>> viewUserStoresOwnership(String token) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userFacade.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                List<Long> ownedStoreIds = userFacade.viewUserStoresOwnership(userName);
                logger.info("User store ownership: {}", ownedStoreIds);
                return ResponseEntity.ok(ownedStoreIds);
            } else {
                logger.warn("Invalid token for adding permission: {}", token);
                return ResponseEntity.status(401).build();
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            logException(String.format("Error display user store ownership for token: %s", token), e);
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    public ResponseEntity<List<String>> viewUserStoresNamesOwnership(String token) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userFacade.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                List<String> ownedStoreNames = userFacade.viewUserStoresNamesOwnership(userName);
                logger.info("User store ownership: {}", ownedStoreNames);
                return ResponseEntity.ok(ownedStoreNames);
            } else {
                logger.warn("Invalid token for adding permission: {}", token);
                return ResponseEntity.status(401).build();
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            logException(String.format("Error display user store ownership for token: %s", token), e);
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    @InitCommand(name = "checkoutShoppingCart")
    public ResponseEntity<String> checkoutShoppingCart(String token, String creditCard, Date expiryDate, String cvv, String discountCode) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userFacade.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                userFacade.checkoutShoppingCart(userName, creditCard, expiryDate, cvv, discountCode);
                logger.info("Checkout shopping cart for user: {}", userName);
                return ResponseEntity.ok(String.format("Checkout shopping cart for user %s", userName));
            } else {
                logger.warn("Invalid token for checkout: {}", token);
                return ResponseEntity.status(401).body(token);
            }
        } catch (CannotCreateTransactionException | DataAccessException | SocketTimeoutException | SQLException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to checkout shopping cart due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception e) {
            logException("Error during checkout", e);
            return ResponseEntity.status(500).body("Error during checkout");
        }
    }

    //getShoppingCartTotalPrice
    @Override
    public ResponseEntity<Double> getShoppingCartTotalPrice(String token) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userFacade.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                double totalPrice = userFacade.getShoppingCartTotalPrice(userName);
                logger.info("Total price of shopping cart for user: {}", userName);
                return ResponseEntity.ok(totalPrice);
            } else {
                logger.warn("Invalid token for getting shopping cart total price: {}", token);
                return ResponseEntity.status(401).build();
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            logException("Error getting shopping cart total price", e);
            return ResponseEntity.status(500).build();
        }
    }

    private void logException(String message, Exception e) {
        if (includeException) {
            logger.error(message,e);
        } else {
            logger.error("{}, {}", message, e.getMessage());
        }
    }
}
