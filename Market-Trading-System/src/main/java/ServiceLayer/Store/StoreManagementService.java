package ServiceLayer.Store;

import DomainLayer.Market.Store.Discount;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.JwtService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.Response;
import java.util.List;

public class StoreManagementService implements IStoreManagementService {
    private static final Logger logger = LogManager.getLogger(StoreManagementService.class);
    String USER_NOT_VALID = "Authentication failed";
    private static StoreManagementService instance;
    private final IStoreFacade storeFacade;
    private JwtService jwtService;
    private UserDetailsService userDetailsService;

    private StoreManagementService(IStoreFacade storeFacade) {
        this.storeFacade = storeFacade;
        jwtService = new JwtService();
        userDetailsService = new InMemoryUserDetailsManager();
    }

    public static synchronized StoreManagementService getInstance(IStoreFacade storeFacade) {
        if (instance == null) {
            instance = new StoreManagementService(storeFacade);
        }
        return instance;
    }

    @Override
    public Response createStore(String founderToken, String storeName, String storeDescription, IRepository<Long, Discount> repository) {
        try {
            String userName = jwtService.extractUsername(founderToken);
            if (jwtService.isValid(founderToken, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.createStore(userName, storeName, storeDescription, repository);
                logger.info("Store created by user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for creating store: {}", founderToken);
                return Response.status(500).entity(USER_NOT_VALID).build();
            }
        } catch (Exception ex) {
            logger.error("Error creating store", ex);
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response addItemToStore(String token, long storeId, String itemName, String description, double itemPrice, int stockAmount, List<String> categories) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.addItemToStore(userName, storeId, itemName, itemPrice, stockAmount, description, categories);
                logger.info("Item added to store by user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for adding item to store: {}", token);
                return Response.status(500).entity(USER_NOT_VALID).build();
            }
        } catch (Exception ex) {
            logger.error("Error adding item to store", ex);
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response updateItem(String token, long storeId, long itemId, String newName, double newPrice, int newAmount) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.updateItem(userName, storeId, itemId, newName, newPrice, newAmount);
                logger.info("Item updated in store by user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for updating item: {}", token);
                return Response.status(500).entity(USER_NOT_VALID).build();
            }
        } catch (Exception ex) {
            logger.error("Error updating item", ex);
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response deleteItem(String token, long storeId, long itemId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.deleteItem(userName, storeId, itemId);
                logger.info("Item deleted from store by user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for deleting item: {}", token);
                return Response.status(500).entity(USER_NOT_VALID).build();
            }
        } catch (Exception ex) {
            logger.error("Error deleting item", ex);
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response changeStorePolicy(String token, long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.changeStorePolicy(userName, storeId);
                logger.info("Store policy changed by user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for changing store policy: {}", token);
                return Response.status(500).entity(USER_NOT_VALID).build();
            }
        } catch (Exception ex) {
            logger.error("Error changing store policy", ex);
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response changeDiscountType(String token, long storeId, String newType) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.changeDiscountType(userName, storeId, newType);
                logger.info("Discount type changed by user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for changing discount type: {}", token);
                return Response.status(500).entity(USER_NOT_VALID).build();
            }
        } catch (Exception ex) {
            logger.error("Error changing discount type", ex);
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response removeStore(String token, long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.removeStore(userName, storeId);
                logger.info("Store removed by user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for removing store: {}", token);
                return Response.status(500).entity(USER_NOT_VALID).build();
            }
        } catch (Exception ex) {
            logger.error("Error removing store", ex);
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response viewManagmentInfo(String token, Long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                logger.info("Viewing management info for store by user: {}", userName);
                return Response.ok().entity(storeFacade.viewStoreManagementInfo(token, storeId)).build();
            } else {
                logger.warn("Invalid token for viewing management info: {}", token);
                return Response.status(500).entity(USER_NOT_VALID).build();
            }
        } catch (Exception ex) {
            logger.error("Error viewing management info", ex);
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response viewInventory(String token, Long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                logger.info("Viewing inventory for store by user: {}", userName);
                return Response.ok().entity(storeFacade.viewInventoryByStoreOwner(userName, storeId)).build();
            } else {
                logger.warn("Invalid token for viewing inventory: {}", token);
                return Response.status(500).entity(USER_NOT_VALID).build();
            }
        } catch (Exception ex) {
            logger.error("Error viewing inventory", ex);
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response viewPurchasesHistory(String token, Long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                logger.info("Viewing purchase history for store by user: {}", userName);
                return Response.ok().entity(storeFacade.viewPurchaseHistory(userName, storeId)).build();
            } else {
                logger.warn("Invalid token for viewing purchase history: {}", token);
                return Response.status(500).entity(USER_NOT_VALID).build();
            }
        } catch (Exception ex) {
            logger.error("Error viewing purchase history", ex);
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response assignStoreOwner(String token, long storeId, String newOwnerId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.assignStoreOwner(userName, storeId, newOwnerId);
                logger.info("Store owner assigned by user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for assigning store owner: {}", token);
                return Response.status(500).entity(USER_NOT_VALID).build();
            }
        } catch (Exception ex) {
            logger.error("Error assigning store owner", ex);
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response assignStoreManager(String token, long storeId, String newManagerI, List<String> permissions) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.assignStoreManager(userName, storeId, newManagerI, permissions);
                logger.info("Store manager assigned by user: {}", userName);
                return Response.ok().build();
            } else {
                logger.warn("Invalid token for assigning store manager: {}", token);
                return Response.status(500).entity(USER_NOT_VALID).build();
            }
        } catch (Exception ex) {
            logger.error("Error assigning store manager", ex);
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }
}
