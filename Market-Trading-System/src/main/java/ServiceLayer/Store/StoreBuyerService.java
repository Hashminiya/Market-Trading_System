package ServiceLayer.Store;

import javax.ws.rs.core.Response;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.Store;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.*;

@Service("StoreBuyerService")
public class StoreBuyerService implements IStoreBuyerService {
    private static final Logger logger = LogManager.getLogger(StoreBuyerService.class);

    @Value("${logging.include-exception:false}")
    private boolean includeException;

    private static StoreBuyerService instance;
    private IStoreFacade storeFacade;
    private final String EMPTY_RESULT_ERROR = "Error: 0 results for search";

    private StoreBuyerService(@Qualifier("StoreController") IStoreFacade storeFacade) {
        this.storeFacade = storeFacade;
    }

    public static synchronized StoreBuyerService getInstance(IStoreFacade storeFacade) {
        if (instance == null) {
            instance = new StoreBuyerService(storeFacade);
        }
        return instance;
    }

    public void clear(){
        storeFacade.clear();
        instance = null;
    }

    @Override
    public ResponseEntity<?> getAllProductsInfoByStore(long storeId) {
        try {
            HashMap<Long, String> result = storeFacade.getAllProductsInfoByStore(storeId);
            List<Item> items = getItemsFromItemIdToItemName(result);
            logger.info("Retrieved all products info for store: {}", storeId);
            return ResponseEntity.status(200).body(items);
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to get all products due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception ex) {
            logException("Error retrieving products info for store: " + storeId, ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAllStoreInfo() {
        try {
            HashMap<Long, String> result = storeFacade.getAllStoreInfo();
            logger.info("Retrieved all store info");
            return ResponseEntity.status(200).body(result);
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to get all store info due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception ex) {
            logException("Error retrieving all store info", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> searchInStoreByCategory(long storeId, String category) {
        try {
            HashMap<Long, String> result = storeFacade.searchInStoreByCategory(storeId, category);
            if (!result.isEmpty()) {
                logger.info("Search in store by category: {} for store: {}", category, storeId);
                return ResponseEntity.status(200).body(result);
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to search in store due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception ex) {
            logException(String.format("Error searching in store by category: %s for store: %d", category, storeId), ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
        logger.warn("No results found for category: {} in store: {}", category, storeId);
        return ResponseEntity.status(204).body(EMPTY_RESULT_ERROR);
    }

    @Override
    public ResponseEntity<?> searchInStoreByKeyWord(long storeId, String keyWord) {
        try {
            HashMap<Long, String> result = storeFacade.searchInStoreByKeyWord(storeId, keyWord);
            if (!result.isEmpty()) {
                logger.info("Search in store by keyword: {} for store: {}", keyWord, storeId);
                return ResponseEntity.status(200).body(result);
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to search in store due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception ex) {
            logException(String.format("Error searching in store by keyword: %s for store: %d", keyWord, storeId), ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
        logger.warn("No results found for keyword: {} in store: {}", keyWord, storeId);
        return ResponseEntity.status(204).body(EMPTY_RESULT_ERROR);
    }

    @Override
    public ResponseEntity<?> searchInStoreByKeyWordAndCategory(long storeId, String category, String keyWord) {
        try {
            HashMap<Long, String> result = storeFacade.searchInStoreByKeyWordAndCategory(storeId, category, keyWord);
            if (!result.isEmpty()) {
                logger.info("Search in store by keyword: {} and category: {} for store: {}", keyWord, category, storeId);
                return ResponseEntity.status(200).body(result);
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to search in store due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception ex) {
            logException(String.format("Error searching in store by keyword: %s and category: %s for store: %d", keyWord, category, storeId), ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
        logger.warn("No results found for keyword: {} and category: {} in store: {}", keyWord, category, storeId);
        return ResponseEntity.status(204).body(EMPTY_RESULT_ERROR);
    }

    @Override
    public ResponseEntity<?> searchGenerallyByCategory(String category) {
        try {
            HashMap<Long, String> result = storeFacade.searchGenerallyByCategory(category);
            List<Item> items = getItemsFromItemIdToItemName(result);
            if (!result.isEmpty()) {
                logger.info("General search by category: {}", items);
                return ResponseEntity.status(200).body(result);
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to search in store due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception ex) {
            logException(String.format("Error searching in general search by category: %s", category), ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
        logger.warn("No results found for general search by category: {}", category);
        return ResponseEntity.status(204).body(EMPTY_RESULT_ERROR);
    }

    @Override
    public ResponseEntity<?> searchGenerallyByKeyWord(String keyWord) {
        try {
            HashMap<Long, String> itemIdToItemName = storeFacade.searchGenerallyByKeyWord(keyWord);//this is map of itemId to itemName
            List<Item> items = getItemsFromItemIdToItemName(itemIdToItemName);
            if (!items.isEmpty()) {
                logger.info("General search by keyword: {}", keyWord);
                return ResponseEntity.status(200).body(items);
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to search in store due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception ex) {
            logException(String.format("Error searching in general search by keyword: %s", keyWord), ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
        logger.warn("No results found for general search by keyword: {}", keyWord);
        return ResponseEntity.status(204).body(EMPTY_RESULT_ERROR);
    }

    @Override
    public ResponseEntity<?> searchGenerallyByKeyWordAndCategory(String category, String keyWord) {
        try {
            HashMap<Long, String> result = storeFacade.searchGenerallyByKeyWordAndCategory(category, keyWord);
            if (!result.isEmpty()) {
                logger.info("General search by keyword: {} and category: {}", keyWord, category);
                return ResponseEntity.status(200).body(result);
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to search in store due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception ex) {
            logException(String.format("Error searching in general search by keyword %s and category %s", keyWord, category), ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
        logger.warn("No results found for general search by keyword: {} and category: {}", keyWord, category);
        return ResponseEntity.status(204).body(EMPTY_RESULT_ERROR);
    }

    @Override
    public ResponseEntity<?> getAllStoresWithItems() {
        try {
            List<Store> stores = storeFacade.findAll();
            Map<Long, Map<String, Object>> storesWithItems = new HashMap<>();
            for (Store store : stores) {
                Map<String, Object> storeDetails = new HashMap<>();
                storeDetails.put("storeName", store.getName());
                storeDetails.put("storeDescription", store.getDescription());
                List<Item> items = store.viewInventory();
                List<Map<String, Object>> itemDetails = new ArrayList<>();
                for (Item item : items) {
                    Map<String, Object> itemDetail = new HashMap<>();
                    itemDetail.put("itemId", item.getId());
                    itemDetail.put("itemName", item.getName());
                    itemDetail.put("itemPrice", item.getPrice());
                    itemDetail.put("stockAmount", item.getQuantity());
                    itemDetail.put("category", item.getCategories());
                    itemDetail.put("description", item.getDescription());
                    itemDetails.add(itemDetail);
                }
                storeDetails.put("items", itemDetails);
                storesWithItems.put(store.getId(), storeDetails);
            }
            return ResponseEntity.status(200).body(storesWithItems);
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to get all stores with items due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception ex) {
            logException("Error retrieving all stores with items", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAllCategories() {
        try {
            Set<String> categories = storeFacade.getAllCategories();
            logger.info("Retrieved all categories");
            return ResponseEntity.status(200).body(categories);
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to get all categories due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception ex) {
            logException("Error retrieving all categories", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    //create private method that get dictionary of itemsId and itemName and return list of items
    private List<Item> getItemsFromItemIdToItemName(HashMap<Long, String> itemIdToItemName) {
        List<Item> items = new ArrayList<>();
        for (Map.Entry<Long, String> entry : itemIdToItemName.entrySet()) {
            Item item = storeFacade.getItem(entry.getKey());
            items.add(item);
        }
        return items;
    }

    private void logException(String message, Exception e) {
        if (includeException) {
            logger.error(message,e);
        } else {
            logger.error("{}, {}", message, e.getMessage());
        }
    }
}
