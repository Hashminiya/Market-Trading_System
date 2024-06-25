package ServiceLayer.Store;

import javax.ws.rs.core.Response;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.Store.Store;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("StoreBuyerService")
public class StoreBuyerService implements IStoreBuyerService {
    private static final Logger logger = LogManager.getLogger(StoreBuyerService.class);
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
            logger.info("Retrieved all products info for store: {}", storeId);
            return ResponseEntity.status(200).body(result);
        } catch (Exception ex) {
            logger.error("Error retrieving products info for store: {}", storeId, ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAllStoreInfo() {
        try {
            HashMap<Long, String> result = storeFacade.getAllStoreInfo();
            logger.info("Retrieved all store info");
            return ResponseEntity.status(200).body(result);
        } catch (Exception ex) {
            logger.error("Error retrieving all store info", ex);
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
        } catch (Exception ex) {
            logger.error("Error searching in store by category: {} for store: {}", category, storeId, ex);
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
        } catch (Exception ex) {
            logger.error("Error searching in store by keyword: {} for store: {}", keyWord, storeId, ex);
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
        } catch (Exception ex) {
            logger.error("Error searching in store by keyword: {} and category: {} for store: {}", keyWord, category, storeId, ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
        logger.warn("No results found for keyword: {} and category: {} in store: {}", keyWord, category, storeId);
        return ResponseEntity.status(204).body(EMPTY_RESULT_ERROR);
    }

    @Override
    public ResponseEntity<?> searchGenerallyByCategory(String category) {
        try {
            HashMap<Long, String> result = storeFacade.searchGenerallyByCategory(category);
            if (!result.isEmpty()) {
                logger.info("General search by category: {}", category);
                return ResponseEntity.status(200).body(result);
            }
        } catch (Exception ex) {
            logger.error("Error in general search by category: {}", category, ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
        logger.warn("No results found for general search by category: {}", category);
        return ResponseEntity.status(204).body(EMPTY_RESULT_ERROR);
    }

    @Override
    public ResponseEntity<?> searchGenerallyByKeyWord(String keyWord) {
        try {
            HashMap<Long, String> result = storeFacade.searchGenerallyByKeyWord(keyWord);
            if (!result.isEmpty()) {
                logger.info("General search by keyword: {}", keyWord);
                return ResponseEntity.status(200).body(result);
            }
        } catch (Exception ex) {
            logger.error("Error in general search by keyword: {}", keyWord, ex);
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
        } catch (Exception ex) {
            logger.error("Error in general search by keyword: {} and category: {}", keyWord, category, ex);
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
        } catch (Exception ex) {
            logger.error("Error retrieving all stores with items", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getAllCategories() {
        try {
            Set<String> categories = storeFacade.getAllCategories();
            logger.info("Retrieved all categories");
            return ResponseEntity.status(200).body(categories);
        } catch (Exception ex) {
            logger.error("Error retrieving all categories", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }
}
