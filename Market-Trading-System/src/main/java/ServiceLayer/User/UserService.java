package ServiceLayer.User;

import API.SpringContext;
import DAL.ItemDTO;
import DAL.ShoppingCartDTO;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.ShoppingCart;
import DomainLayer.Market.Util.JwtService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import java.util.Date;
import java.util.List;
import java.lang.reflect.Field;

import java.util.*;

@Service("userService")
public class UserService implements IUserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);

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

    public ResponseEntity<String> guestEntry() {
        try {
            String userName = userFacade.createGuestSession();
            String token = jwtService.generateToken(userName, "GUEST");
            logger.info("Guest session created for user: {}", userName);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            logger.error("Error creating guest session", e);
            return ResponseEntity.status(500).body("Error creating guest session");
        }
    }

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
        } catch (Exception e) {
            logger.error("Error terminating guest session", e);
            return ResponseEntity.status(500).body("Error terminating guest session");
        }
    }

    public ResponseEntity<String> register(String userName, String password, int userAge) {
        try {
            userFacade.register(userName, password, userAge);
            logger.info("User registered: {}", userName);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            logger.error("Error registering user: {}", userName, e);
            return ResponseEntity.status(500).body("Error registering user");
        }
    }

    public ResponseEntity<String> login(String userName, String password) {
        try {
            userFacade.login(userName, password);
            String token = jwtService.generateToken(userName, "REGISTERED");
            logger.info("User logged in: {}", userName);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            //logger.error("Error logging in user: {}", userName, e);
            return ResponseEntity.status(500).body(String.format("Error logging in user %s- %s", userName, e.getMessage()));
        }
    }

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
        } catch (Exception e) {
            logger.error("Error logging out user", e);
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
        } catch (Exception e) {
            logger.error("Error viewing shopping cart", e);
            return ResponseEntity.status(500).body("Error viewing shopping cart");
        }
    }

    @Override
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
        } catch (Exception e) {
            logger.error("Error modifying shopping cart", e);
            return ResponseEntity.status(500).body("Error modifying shopping cart");
        }
    }

    @Override
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
        } catch (Exception e) {
            logger.error("Error adding item to basket", e);
            return ResponseEntity.status(500).body("Error adding item to basket");
        }
    }

    @Override
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
        } catch (Exception e) {
            logger.error("Error adding permission", e);
            return ResponseEntity.status(500).body("Error adding permission");
        }
    }

    @Override
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
        } catch (Exception e) {
            logger.error("Error removing permission", e);
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
                ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO(shoppingCart, SpringContext.getBean(StoreController.class));
                logger.info("Shopping cart retrieved for user: {}", userName);
                return ResponseEntity.ok(shoppingCartDTO);
            } else {
                logger.warn("Invalid token for getting shopping cart: {}", token);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            logger.error("Error getting shopping cart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<Long>> viewUserStoresOwnership(String token){
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userFacade.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                List<Long> ownedStoreIds = userFacade.viewUserStoresOwnership(userName);
                logger.info("User store ownership: {}", ownedStoreIds);
                return ResponseEntity.ok(ownedStoreIds);
            }
            else {
                logger.warn("Invalid token for adding permission: {}", token);
                return ResponseEntity.status(401).build();
            }
        }
        catch (Exception e) {
            logger.error("Error display user store ownership: {}", token, e);
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
        } catch (Exception e) {
            logger.error("Error display user store ownership: {}", token, e);
            return ResponseEntity.status(500).build();
        }
    }
}
