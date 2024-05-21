package DomainLayer.Market.Store;

import DAL.ItemDTO;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.IdGenerator;
import DomainLayer.Market.Store.Item;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StoreController implements IStoreFacade{

    private IRepository<Long, Store> storesRepo;
    private IPurchaseFacade purchaseFacade;
    private IUserFacade userFacade;

    public StoreController(IRepository<Long, Store> storesRepo, IPurchaseFacade purchaseFacade, IUserFacade userFacade) {
        this.storesRepo = storesRepo;
        this.purchaseFacade = purchaseFacade;
        this.userFacade = userFacade;
    }

    private synchronized long generateStoreId(){
        return IdGenerator.generateId();
    }

    @Override
    public void createStore(String founderId, String storeName, String storeDescription, IRepository<Long, Discount> discounts) {
        //TODO: check the user is registered
        long storeId = generateStoreId();
        Store newStore = new Store(storeId, founderId, storeName, storeDescription, discounts);
        storesRepo.save(newStore);
    }

    @Override
    public HashMap<Long, Integer> viewInventoryByStoreOwner(String userId, long storeId) {
        //TODO: check the user is the store owner- decide if he must be store owner
        Store store = storesRepo.findById(storeId);
        List<Item> inventory = store.viewInventory();
        HashMap<Long, Integer> itemsInfo = new HashMap<>();
        for(Item item: inventory){
            itemsInfo.put(item.getId(), item.getQuantity());
        }
        return itemsInfo;
    }

    @Override
    public void addItemToStore(String userId, long storeId, String itemName, double itemPrice, int stockAmount, String description, List<String> categories) {
        //TODO: check the user is the store owner
        Store store = storesRepo.findById(storeId);
        store.addItem(IdGenerator.generateId(), itemName, itemPrice, stockAmount, description, categories);
    }

    @Override
    public void updateItem(String userId, long storeId, long itemId, String newName, double newPrice, int stockAmount) {
        //TODO: check the user is the store owner/manager with permissions
        Store store = storesRepo.findById(storeId);
        store.updateItem(itemId, newName, newPrice, stockAmount);
    }

    @Override
    public void deleteItem(String userId, long storeId, long itemId) {
        //TODO: check the user is the store owner/manager with permissions
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
    public void assignStoreOwner(String userId, long storeId, String newOwnerId){
        //TODO: check the user is the store owner
        Store store = storesRepo.findById(storeId);
        userFacade.assignStoreOwner(userId, storeId);
        store.assignOwner(newOwnerId);
    }

    @Override
    public void assignStoreManager(String userId, long storeId, String newManagerId){
        //TODO: check the user is the store owner/manager with permissions
        Store store = storesRepo.findById(storeId);
        userFacade.assignStoreManager(userId, storeId);
        store.assignManager(newManagerId);
    }

    @Override
    public void removeStore(String userId, long storeId) {
        //TODO: check the user is the founder
        storesRepo.delete(storeId);
    }

    @Override
    public List<String> viewStoreManagementInfo(String userId, long storeId) {
        //TODO: check the user is the store owner
        Store store = storesRepo.findById(storeId);
        List<String> managementIds = store.getManagers();
        managementIds.addAll(store.getOwners());
        return managementIds;
    }

    @Override
    public HashMap<Long, Integer> viewPurchaseHistory(String userId, long storeId) {
        //TODO: check the user is the store owner
        List<ItemDTO> itemDTOs = purchaseFacade.getPurchasesByStore(storeId);
        HashMap<Long, Integer> result = new HashMap<>();
        for(ItemDTO item: itemDTOs){
            result.put(item.getItemId(), item.getQuantity());
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
        Store store = storesRepo.findById(basket.getId());
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
}
