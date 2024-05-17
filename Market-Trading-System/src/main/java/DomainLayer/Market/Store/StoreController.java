package DomainLayer.Market.Store;

import DomainLayer.Market.IRepository;
import DomainLayer.Market.InMemoryRepository;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.User.IUserFacade;

import java.util.HashMap;

public class StoreController implements IStoreFacade{

    private IRepository<Long, Store> storesRepo;
    private long  idCounter;

    public StoreController(IRepository<Long, Store> storesRepo) {
        this.storesRepo = storesRepo;
        this.idCounter = 0;
    }

    private synchronized long generateStoreId(){
        idCounter++;
        return idCounter;
    }

    @Override
    public void createStore(long founderId, String storeName, String storeDescription, IRepository<Long, Item.Discount> discounts, IRepository<Long, IProduct> products) {
        //TODO: check the user is registered
        long storeId = generateStoreId();
        Store newStore = new Store(storeId, storeName, discounts, products);

        //TODO: set the store description
        storesRepo.save(newStore);
    }

    @Override
    public void viewInventoryByStoreOwner(long userId, long storeId) {
        //TODO: check the user is the store owner- decide if he must be store owner
        Store store = storesRepo.findById(storeId);
        store.viewInventory();
    }

    @Override
    public void addItemToStore(long userId, long storeId, String itemName, double itemPrice, int stockAmount, long categoryId) {
        //TODO: check the user is the store owner
        Store store = storesRepo.findById(storeId);
        store.addItemToStore(itemName, itemPrice, stockAmount, categoryId);
    }

    @Override
    public void updateItem(long userId, long storeId, long itemId, String newName, double newPrice, int stockAmount) {
        //TODO: check the user is the store owner/manager with permissions
        Store store = storesRepo.findById(storeId);
        store.updateItem(itemId, newName, newPrice, stockAmount);
    }

    @Override
    public void deleteItem(long userId, long storeId, long itemId) {
        //TODO: check the user is the store owner/manager with permissions
        Store store = storesRepo.findById(storeId);
        store.deleteItem(itemId);
    }

    @Override
    public void changeStorePolicy(long userId, long storeId) {
        //TODO: implement in the next version
    }

    @Override
    public void changeDiscountType(long userId, long storeId, String newType) {
        //TODO: implement in the next version
    }

    @Override
    public void assignStoreOwner(long actorId, long userId) {
        //TODO: check the user is the store owner

    }

    @Override
    public void assignStoreManager(long actorId, long userId) {

    }

    @Override
    public void removeStore(long userId, long storeId) {

    }

    @Override
    public void viewStoreManagementInfo(long userId, long storeId) {

    }

    @Override
    public void viewPurchaseHistory(long userId, long storeId) {

    }

    @Override
    public HashMap<Long, HashMap<String, String>> getAllProductsInfoByStore(long storeId) {

        return null;
    }

    @Override
    public HashMap<Long, HashMap<String, String>> getAllStoreInfo(long storeId) {

        return null;
    }

    @Override
    public HashMap<Long, String> searchStoreByName(String name) {

        return null;
    }

    @Override
    public HashMap<Long, String> searchItemByName(String name) {

        return null;
    }

    @Override
    public HashMap<Long, String> searchStoreByCategory(long category) {

        return null;
    }

    @Override
    public HashMap<Long, String> searchItemByCategory(long category) {

        return null;
    }

    @Override
    public HashMap<Long, String> searchStoreByKeyWord(String keyWord) {

        return null;
    }

    @Override
    public HashMap<Long, String> searchItemByKeyWord(String keyWord) {

        return null;
    }

    @Override
    public void addItemToShoppingBasket(ShoppingBasket basket, long userId, long storeId, long itemId) {

    }
}
