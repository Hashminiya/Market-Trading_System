package ServiceLayer.Store;

import API.InitCommand;
import DAL.PolicyDTO;
import DomainLayer.Market.Store.Discount.Discount;
import DomainLayer.Market.Store.Discount.IDiscount;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.*;

@Service("StoreManagementService")
public class StoreManagementService implements IStoreManagementService {
    private static final Logger logger = LogManager.getLogger(StoreManagementService.class);
    String USER_NOT_VALID = "Authentication failed";
    private static StoreManagementService instance;
    private final IStoreFacade storeFacade;
    private JwtService jwtService;
    private IUserFacade userFacade;

    @Autowired
    public StoreManagementService(@Qualifier("StoreController") IStoreFacade storeFacade,@Qualifier("userController") IUserFacade userFacade) {
        this.storeFacade = storeFacade;
        this.jwtService = new JwtService();
        this.userFacade = userFacade;
        StoreManagementService.instance = this;
    }

    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    public void setUserFacade(IUserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public static synchronized StoreManagementService getInstance(IStoreFacade storeFacade, IUserFacade userFacade) {
        if (instance == null) {
            instance = new StoreManagementService(storeFacade,userFacade);
        }
        return instance;
    }

    public void clear(){
        storeFacade.clear();
        userFacade.clear();
        instance = null;
    }

    @Override
    @InitCommand(name = "createStore")
    public ResponseEntity<?> createStore(String founderToken, String storeName, String storeDescription) {
        try {
            String userName = jwtService.extractUsername(founderToken);
            if (jwtService.isValid(founderToken, userFacade.loadUserByUsername(userName))) {
                //return ResponseEntity.status(200).body(storeFacade.viewStoreManagementInfo(userName, storeId))
                ResponseEntity<Long> response = ResponseEntity.status(200).body(storeFacade.createStore(userName, storeName, storeDescription));
                logger.info("Store created by user: {}", userName);
                return response;
            } else {
                logger.warn("Invalid token for creating store: {}", founderToken);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (Exception ex) {
            logger.error("Error creating store", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @InitCommand(name = "addItemToStore")
    @Override
    public ResponseEntity<?> addItemToStore(String token, long storeId, String itemName, String description, double itemPrice, int stockAmount, List<String> categories) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                ResponseEntity<Long> response = ResponseEntity.status(200).body(storeFacade.addItemToStore(userName, storeId, itemName, itemPrice, stockAmount, description, categories));
                logger.info("Item added to store by user: {}", userName);
                return response;
            } else {
                logger.warn("Invalid token for adding item to store: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (Exception ex) {
            logger.error("Error adding item to store", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> updateItem(String token, long storeId, long itemId, String newName, double newPrice, int newAmount) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                storeFacade.updateItem(userName, storeId, itemId, newName, newPrice, newAmount);
                logger.info("Item updated in store by user: {}", userName);
                return ResponseEntity.ok().body(String.format("Item updated in store by user %s", userName));
            } else {
                logger.warn("Invalid token for updating item: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (Exception ex) {
            logger.error("Error updating item", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> deleteItem(String token, long storeId, long itemId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                storeFacade.deleteItem(userName, storeId, itemId);
                logger.info("Item deleted from store by user: {}", userName);
                return ResponseEntity.ok().body(String.format("Item deleted from store by user %s", userName));
            } else {
                logger.warn("Invalid token for deleting item: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (Exception ex) {
            logger.error("Error deleting item", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> changeStorePolicy(String token, long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                storeFacade.changeStorePolicy(userName, storeId);
                logger.info("Store policy changed by user: {}", userName);
                return ResponseEntity.ok().body(String.format("Store policy changed by user %s", userName));
            } else {
                logger.warn("Invalid token for changing store policy: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (Exception ex) {
            logger.error("Error changing store policy", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> changeDiscountType(String token, long storeId, String newType) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                storeFacade.changeDiscountType(userName, storeId, newType);
                logger.info("Discount type changed by user: {}", userName);
                return ResponseEntity.ok().body(String.format("Discount type changed by user %s", userName));
            } else {
                logger.warn("Invalid token for changing discount type: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (Exception ex) {
            logger.error("Error changing discount type", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> removeStore(String token, long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                storeFacade.removeStore(userName, storeId);
                logger.info("Store removed by user: {}", userName);
                return ResponseEntity.ok().body(String.format("Store removed by user %s", userName));
            } else {
                logger.warn("Invalid token for removing store: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (Exception ex) {
            logger.error("Error removing store", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> viewManagementInfo(String token, long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                logger.info("Viewing management info for store by user: {}", userName);
                return ResponseEntity.status(200).body(storeFacade.viewStoreManagementInfo(userName, storeId));
            } else {
                logger.warn("Invalid token for viewing management info: {}", token);
                return ResponseEntity.status(401).body(token);
            }
        } catch (Exception ex) {
            logger.error("Error viewing management info", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> viewInventory(String token, long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                logger.info("Viewing inventory for store by user: {}", userName);
                return ResponseEntity.ok().body(storeFacade.viewInventoryByStoreOwner(userName, storeId));
            } else {
                logger.warn("Invalid token for viewing inventory: {}", token);
                return ResponseEntity.status(401).body(token);
            }
        } catch (Exception ex) {
            logger.error("Error viewing inventory", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> viewPurchasesHistory(String token, long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                logger.info("Viewing purchase history for store by user: {}", userName);
                return ResponseEntity.ok().body(storeFacade.viewPurchaseHistory(userName, storeId));
            } else {
                logger.warn("Invalid token for viewing purchase history: {}", token);
                return ResponseEntity.status(401).body(token);
            }
        } catch (Exception ex) {
            logger.error("Error viewing purchase history", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @InitCommand(name = "assignStoreOwner")
    @Override
    public ResponseEntity<String> assignStoreOwner(String token, long storeId, String newOwnerId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                storeFacade.assignStoreOwner(userName, storeId, newOwnerId);
                logger.info("Store owner assigned by user: {}", userName);
                return ResponseEntity.ok().body(String.format("Store owner assigned by user %s", userName));
            } else {
                logger.warn("Invalid token for assigning store owner: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (Exception ex) {
            logger.error("Error assigning store owner", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @InitCommand(name = "assignStoreManager")
    @Override
    public ResponseEntity<String> assignStoreManager(String token, long storeId, String newManagerI, List<String> permissions) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                storeFacade.assignStoreManager(userName, storeId, newManagerI, permissions);
                logger.info("Store manager assigned by user: {}", userName);
                return ResponseEntity.ok().body(String.format("Store manager assigned by user %s", userName));
            } else {
                logger.warn("Invalid token for assigning store manager: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (Exception ex) {
            logger.error("Error assigning store manager", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> addDiscount(String token, long storeId, String discountDetails) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                storeFacade.addDiscount(userName, storeId, discountDetails);
                logger.info("Discount added by user: {}", userName);
                return ResponseEntity.ok().body(String.format("Discount added by user %s", userName));
            } else {
                logger.warn("Invalid token for adding discount to store: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (Exception ex) {
            logger.error("Error adding discount", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> addPolicy(String token, long storeId, String policyDetails) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                storeFacade.addPolicy(userName, storeId, policyDetails);
                logger.info("Policy added by user: {}", userName);
                return ResponseEntity.ok().body(String.format("Policy added by user %s", userName));
            } else {
                logger.warn("Invalid token for adding discount to store: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (Exception ex) {
            logger.error("Error adding policy", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }


    @Override
    public ResponseEntity<?> viewInventoryByStoreNameAndToken(String token, String storeName) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                List<Store> userStores = storeFacade.findStoresByOwner(userName);

                for (Store store : userStores) {
                    if (store.getName().equalsIgnoreCase(storeName)) {
                        List<Item> items = store.viewInventory();
                        List<Map<String, Object>> itemDetails = new ArrayList<>();

                        for (Item item : items) {
                            Map<String, Object> itemDetail = new HashMap<>();
                            itemDetail.put("itemId", item.getId());
                            itemDetail.put("itemName", item.getName());
                            itemDetail.put("itemPrice", item.getPrice());
                            itemDetail.put("stockAmount", item.getQuantity());
                            itemDetail.put("category", item.getCategories());
                            itemDetail.put("storeId", store.getId());
                            itemDetails.add(itemDetail);
                        }

                        return ResponseEntity.ok(itemDetails);
                    }
                }
                return ResponseEntity.status(404).body("Store not found");
            } else {
                logger.warn("Invalid token for viewing inventory: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (Exception ex) {
            logger.error("Error viewing inventory by store name and token", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> viewAllPolicies(String token, String storeName) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                List<Store> userStores = storeFacade.findStoresByOwner(userName);

                for (Store store : userStores) {
                    if (store.getName().equalsIgnoreCase(storeName)) {
                        List<PolicyDTO> policies = store.getPolicies();
                        return ResponseEntity.ok(policies);
                    }
                }
                return ResponseEntity.status(404).body("Store not found");
            } else {
                logger.warn("Invalid token for viewing inventory: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (Exception ex) {
            logger.error("Error viewing inventory by store name and token", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }
}
