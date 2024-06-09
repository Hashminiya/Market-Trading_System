package DomainLayer.Market.Store;

import DAL.ItemDTO;
import DomainLayer.Market.Util.*;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component("StoreController")
public class StoreController implements IStoreFacade{
    private static StoreController storeControllerInstance;
    private IRepository<Long, Store> storesRepo;
    private IPurchaseFacade purchaseFacade;
    private IUserFacade userFacade;

    private String VIEW_INVENTORY = "VIEW_INVENTORY";
    private String ADD_ITEM = "ADD_ITEM";
    private String UPDATE_ITEM = "UPDATE_ITEM";
    private String DELETE_ITEM = "DELETE_ITEM";
    private String ASSIGN_OWNER = "ASSIGN_OWNER";
    private String ASSIGN_MANAGER = "ASSIGN_MANAGER";
    private String VIEW_STORE_MANAGEMENT_INFO = "VIEW_STORE_MANAGEMENT_INFO";
    private String VIEW_PURCHASE_HISTORY = "VIEW_PURCHASE_HISTORY";
    private String CHANGE_POLICY = "CHANGE_POLICY";
    private String CHANGE_DISCOUNT_TYPE = "CHANGE_DISCOUNT_TYPE";
    private String REMOVE_STORE = "REMOVE_STORE";

    @Autowired
    private StoreController(@Qualifier("InMemoryRepository") IRepository<Long, Store> storesRepo,
                            @Qualifier("purchaseController") IPurchaseFacade purchaseFacadeInstance) {
        this.storesRepo = storesRepo;
        this.purchaseFacade = purchaseFacadeInstance;
    }

    public static synchronized StoreController getInstance(IRepository<Long, Store> storesRepo, IUserFacade userFacadeInstance, IPurchaseFacade purchaseFacadeInstance) {
        if (storeControllerInstance == null) {
            storeControllerInstance = new StoreController(storesRepo, purchaseFacadeInstance);
            // TODO : We assume that when this function called, next line will be setUserFacade..
        }
        return storeControllerInstance;
    }

    @Override
    public void setUserFacade(IUserFacade userFacadeInstance) {
        userFacade = userFacadeInstance;
    }

    @Override
    public void setPurchaseFacade(IPurchaseFacade purchaseFacadeInstance) {
        purchaseFacade = purchaseFacadeInstance;
    }

    private synchronized long generateStoreId(){
        return IdGenerator.generateId();
    }

    @Override
    public long createStore(String founderId, String storeName, String storeDescription, IRepository<Long, Discount> discounts) throws Exception{
        if(!userFacade.isRegister(founderId))
            throw new Exception("User isn't registered, so can't create new store");
        long storeId = generateStoreId();
        Store newStore = new Store(storeId, founderId, storeName, storeDescription, discounts);
        storesRepo.save(newStore);
        userFacade.assignStoreOwner(founderId,storeId);
        return storeId;//for test purposes
    }

    @Override
    public HashMap<Long, Integer> viewInventoryByStoreOwner(String userId, long storeId)throws Exception {
        if(!userFacade.checkPermission(userId, storeId, VIEW_INVENTORY))
            throw new Exception("User doesn't has permission to view the store inventory");
        Store store = storesRepo.findById(storeId);
        List<Item> inventory = store.viewInventory();
        HashMap<Long, Integer> itemsInfo = new HashMap<>();
        for(Item item: inventory){
            itemsInfo.put(item.getId(), item.getQuantity());
        }
        return itemsInfo;
    }

    @Override
    public long addItemToStore(String userId, long storeId, String itemName, double itemPrice, int stockAmount, String description, List<String> categories) throws Exception{
        if(!userFacade.checkPermission(userId, storeId, ADD_ITEM))
            throw new Exception("User doesn't has permission to add item to the store");
        Store store = storesRepo.findById(storeId);
        Long id = IdGenerator.generateId();
        store.addItem(id, itemName, itemPrice, stockAmount, description, categories);
        return id; //for test purposes
    }

    @Override
    public void updateItem(String userId, long storeId, long itemId, String newName, double newPrice, int stockAmount) throws Exception{
        if(!userFacade.checkPermission(userId, storeId, UPDATE_ITEM))
            throw new Exception("User doesn't has permission to update item in the store");
        Store store = storesRepo.findById(storeId);
        store.updateItem(itemId, newName, newPrice, stockAmount);
    }

    @Override
    public void deleteItem(String userId, long storeId, long itemId) throws Exception{
        if(!userFacade.checkPermission(userId, storeId, DELETE_ITEM))
            throw new Exception("User doesn't has permission to delete item in the store");
        Store store = storesRepo.findById(storeId);
        store.deleteItem(itemId);
    }

    @Override
    public void changeStorePolicy(String userId, long storeId) {
        //TODO: implement in the next version
    }

    @Override
    public void changeDiscountType(String userId, long storeId, String newType) {
        //TODO: implement in the next version
        Store store = storesRepo.findById(storeId);
    }

    @Override
    public void assignStoreOwner(String userId, long storeId, String newOwnerId)throws Exception{
        if(!userFacade.checkPermission(userId, storeId, ASSIGN_OWNER))
            throw new Exception("User doesn't has permission to assign store owner");
        Store store = storesRepo.findById(storeId);
        userFacade.assignStoreOwner(newOwnerId, storeId);
        store.assignOwner(newOwnerId);
    }

    @Override
    public void assignStoreManager(String userId, long storeId, String newManagerId, List<String> permissions)throws Exception{
        if(!userFacade.checkPermission(userId, storeId, ASSIGN_MANAGER))
            throw new Exception("User doesn't has permission to assign store manager");
        Store store = storesRepo.findById(storeId);
        userFacade.assignStoreManager(newManagerId, storeId, permissions);
        store.assignManager(newManagerId);
    }

    @Override
    public void removeStore(String userId, long storeId) throws Exception{
        if(!userFacade.checkPermission(userId, storeId, REMOVE_STORE) && !userFacade.isAdmin(userId))
            throw new Exception("User doesn't has permission to remove store");
        storesRepo.delete(storeId);
    }

    @Override
    public HashMap<String, List<String>> viewStoreManagementInfo(String userId, long storeId) throws Exception {
        if (!userFacade.checkPermission(userId, storeId, "VIEW_STORE_MANAGEMENT_INFO")) {
            throw new Exception("User doesn't have permission to view the store management information");
        }
        Store store = storesRepo.findById(storeId);

        List<String> managementIds = new ArrayList<>(store.getManagers());
        managementIds.addAll(store.getOwners());

        HashMap<String, List<String>> managementInfo = new HashMap<>();
        for (String id : managementIds) {
            managementInfo.put(id, userFacade.getUserPermission(id, storeId));
        }
        return managementInfo;
    }


    @Override
    public HashMap<Long, HashMap<Long, Integer>> viewPurchaseHistory(String userId, long storeId) throws Exception{
        if(!userFacade.checkPermission(userId, storeId, VIEW_PURCHASE_HISTORY))
            throw new Exception("User doesn't has permission to view the store purchase history");
        //TODO: alter according to purchase new signature
        HashMap<Long, List<ItemDTO>> purchases = purchaseFacade.getPurchasesByStore(storeId);
        HashMap<Long, HashMap<Long, Integer>> result = new HashMap<>();
        for(Long purchase: purchases.keySet()){
            result.put(purchase, new HashMap<>());
            for(ItemDTO itemDTO: purchases.get(purchase)) {
                result.get(purchase).put(itemDTO.getItemId(), itemDTO.getQuantity());
            }
        }
        return result;
    }

    @Override
    public HashMap<Long, String> getAllProductsInfoByStore(long storeId) {
        Store store = storesRepo.findById(storeId);
        List<Item> items = store.viewInventory();
        HashMap<Long, String> result = new HashMap<>();
        for(Item item: items){
            result.put(item.getId(), item.getName());
        }
        return result;
    }

    @Override
    public HashMap<Long, String> getAllStoreInfo() {
        HashMap<Long, String> result = new HashMap<>();
        for(Store store: storesRepo.findAll()){
            result.put(store.getId(), store.getName());
        }
        return result;
    }

    @Override
    public HashMap<Long, String> searchInStoreByCategory(long storeId, String category) {
        Store store = storesRepo.findById(storeId);
        List<Item> items = store.searchByCategory(category);
        HashMap<Long, String> result = new HashMap<>();
        for(Item item: items){
            result.put(item.getId(), item.getName());
        }
        return result;
    }

    @Override
    public HashMap<Long, String> searchInStoreByKeyWord(long storeId, String keyWord) {
        Store store = storesRepo.findById(storeId);
        List<Item> items = store.search(keyWord);
        HashMap<Long, String> result = new HashMap<>();
        for(Item item: items){
            result.put(item.getId(), item.getName());
        }
        return result;
    }

    @Override
    public HashMap<Long, String> searchInStoreByKeyWordAndCategory(long storeId, String category, String keyWord) {
        Store store = storesRepo.findById(storeId);
        List<Item> items = store.searchKeyWordWithCategory(category, keyWord);
        HashMap<Long, String> result = new HashMap<>();
        for(Item item: items){
            result.put(item.getId(), item.getName());
        }
        return result;
    }

    @Override
    public HashMap<Long, String> searchGenerallyByCategory(String category) {
        List<Store> stores = storesRepo.findAll();
        HashMap<Long, String> result = new HashMap<>();
        for(Store store: stores) {
            List<Item> items = store.searchByCategory(category);
            for (Item item : items) {
                result.put(item.getId(), item.getName());
            }
        }
        return result;
    }

    @Override
    public HashMap<Long, String> searchGenerallyByKeyWord(String keyWord) {
        List<Store> stores = storesRepo.findAll();
        HashMap<Long, String> result = new HashMap<>();
        for(Store store: stores) {
            List<Item> items = store.search(keyWord);
            for (Item item : items) {
                result.put(item.getId(), item.getName());
            }
        }
        return result;
    }

    @Override
    public HashMap<Long, String> searchGenerallyByKeyWordAndCategory(String category, String keyWord) {
        List<Store> stores = storesRepo.findAll();
        HashMap<Long, String> result = new HashMap<>();
        for(Store store: stores) {
            List<Item> items = store.searchKeyWordWithCategory(category, keyWord);
            for (Item item : items) {
                result.put(item.getId(), item.getName());
            }
        }
        return result;
    }

    @Override
    public boolean addItemToShoppingBasket(ShoppingBasket basket, long storeId, long itemId, int quantity) {
        Store store = storesRepo.findById(storeId);
        boolean isAvailable = store.isAvailable(itemId, quantity);
        if(isAvailable) {
            basket.addItem(itemId, quantity);
            return true;
        }
        return false;
    }

    public void purchaseOccurs(){
        List<ItemDTO> purchasedItems = purchaseFacade.getPurchasedItems();
        for (ItemDTO itemDto: purchasedItems){
            Store store = storesRepo.findById(itemDto.getStoreId());
            store.updateAmount(itemDto.getItemId(), itemDto.getQuantity());
        }
    }

    @Override
    public void calculateBasketPrice(ShoppingBasket basket, String code) throws Exception {
        Store store = storesRepo.findById(basket.getStoreId());
        store.calculateBasketPrice(basket, code);
    }

    @Override
    public void addHiddenDiscount(double percent, Date expirationDate, List<Long> items, long storeId, String code, boolean isStoreDiscount) {
        Store store = storesRepo.findById(storeId);
        if(isStoreDiscount)
            store.addDiscount(new HiddenDiscount(IdGenerator.generateId(), percent, expirationDate, storeId, code));
        else
            store.addDiscount(items, new HiddenDiscount(IdGenerator.generateId(), percent, expirationDate, storeId, code));
    }

    @Override
    public void addRegularDiscount(double percent, Date expirationDate, List<Long> items, long storeId, List<Long> conditionItems, boolean isStoreDiscount) {
        Store store = storesRepo.findById(storeId);
        if(isStoreDiscount)
            store.addDiscount(new RegularDiscount(IdGenerator.generateId(), percent, expirationDate, storeId, conditionItems));
        else
            store.addDiscount(items, new RegularDiscount(IdGenerator.generateId(), percent, expirationDate, storeId, conditionItems));
    }

    @Override
    public void clear() {
        this.storesRepo = null;
        this.purchaseFacade = null;
        this.userFacade = null;
    }

    @Override
    public void setStoersRepo(IRepository<Long,Store> storesRepo) {
        this.storesRepo = storesRepo;
    }
}
