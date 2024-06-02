package ServiceLayer.User;

import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.JwtService;
import API.model.UserModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.Optional;


@Service
public class UserService implements IUserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    private IUserFacade userFacade;
    private JwtService jwtService;
    private UserDetailsService userDetailsService;
    private static UserService instance;

    private UserService(IUserFacade userFacade) {
        this.userDetailsService = new InMemoryUserDetailsManager();
        this.userFacade = userFacade;
        this.jwtService = new JwtService();
    }

    public static synchronized UserService getInstance(IUserFacade userFacade) {
        if (instance == null) {
            instance = new UserService(userFacade);
        }
        return instance;
    }

    public Response GuestEntry() {
        try {
            String userName = userFacade.createGuestSession();
            String token = jwtService.generateToken(userName, "GUEST");
            logger.info("Guest session created for user: {}", userName);
            return Response.ok(token).build();
        } catch (Exception e) {
            logger.error("Error creating guest session", e);
            return Response.serverError().build();
        }
    }

    public Response GuestExit(String token) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                userFacade.terminateGuestSession(userName);
                logger.info("Guest session terminated for user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for guest exit: {}", token);
                return Response.status(401).build();
            }
        } catch (Exception e) {
            logger.error("Error terminating guest session", e);
            return Response.serverError().build();
        }
    }

    public Response register(String userName, String password, int userAge) {
        try {
            userFacade.register(userName, password, userAge);
            logger.info("User registered: {}", userName);
            return Response.ok().build();
        } catch (Exception e) {
            logger.error("Error registering user: {}", userName, e);
            return Response.serverError().build();
        }
    }

    public Response login(String userName, String password) {
        try {
            userFacade.login(userName, password);
            String token = jwtService.generateToken(userName, "REGISTERED");
            logger.info("User logged in: {}", userName);
            return Response.ok(token).build();
        } catch (Exception e) {
            logger.error("Error logging in user: {}", userName, e);
            return Response.serverError().build();
        }
    }

    public Response logout(String token) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                userFacade.logout(userName);
                logger.info("User logged out: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for logout: {}", token);
                return Response.status(401).build();
            }
        } catch (Exception e) {
            logger.error("Error logging out user", e);
            return Response.serverError().build();
        }
    }

    public Response viewShoppingCart(String token) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                logger.info("Viewing shopping cart for user: {}", userName);
                return Response.ok(userFacade.viewShoppingCart(userName)).build();
            } else {
                logger.warn("Invalid token for viewing shopping cart: {}", token);
                return Response.status(401).build();
            }
        } catch (Exception e) {
            logger.error("Error viewing shopping cart", e);
            return Response.serverError().build();
        }
    }

    @Override
    public Response modifyShoppingCart(String token, long basketId, long itemId, int newQuantity) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                userFacade.modifyShoppingCart(userName, basketId, itemId, newQuantity);
                logger.info("Modified shopping cart for user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for modifying shopping cart: {}", token);
                return Response.status(401).build();
            }
        } catch (Exception e) {
            logger.error("Error modifying shopping cart", e);
            return Response.serverError().build();
        }
    }

    @Override
    public Response checkoutShoppingCart(String token, String creditCard, Date expiryDate, String cvv, String discountCode) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                userFacade.checkoutShoppingCart(userName, creditCard, expiryDate, cvv, discountCode);
                logger.info("Checkout shopping cart for user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for checkout: {}", token);
                return Response.status(401).build();
            }
        } catch (Exception e) {
            logger.error("Error during checkout", e);
            return Response.serverError().build();
        }
    }

    @Override
    public Response addItemToBasket(String token, long basketId, long itemId, int quantity) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                userFacade.addItemToBasket(userName, basketId, itemId, quantity);
                logger.info("Added item to basket for user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for adding item to basket: {}", token);
                return Response.status(401).build();
            }
        } catch (Exception e) {
            logger.error("Error adding item to basket", e);
            return Response.serverError().build();
        }
    }

    @Override
    public Response addPermission(String token, long storeId, String permission) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                userFacade.addPermission(userName, storeId, permission);
                logger.info("Added permission for user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for adding permission: {}", token);
                return Response.status(401).build();
            }
        } catch (Exception e) {
            logger.error("Error adding permission", e);
            return Response.serverError().build();
        }
    }

    @Override
    public Response removePermission(String token, long storeId, String permission) {
        try {
            String userName = jwtService.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if (userName != null && jwtService.isValid(token, userDetails)) {
                userFacade.removePermission(userName, storeId, permission);
                logger.info("Removed permission for user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for removing permission: {}", token);
                return Response.status(401).build();
            }
        } catch (Exception e) {
            logger.error("Error removing permission", e);
            return Response.serverError().build();
        }
    }

    public Optional<UserModel> getUser(int id){
        Optional<UserModel> optional = Optional.empty();
        optional = Optional.of(new UserModel("noam", "12345", 1));
        return optional;
    }
}
