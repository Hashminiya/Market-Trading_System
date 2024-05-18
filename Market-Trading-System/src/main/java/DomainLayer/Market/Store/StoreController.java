package DomainLayer.Market.Store;

import DAL.ItemDTO;
import DomainLayer.Market.IRepository;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.User.IUserFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreController implements IStoreFacade{

    private IRepository<Long, Store> storesRepo;
    private long  idCounter;
    private IPurchaseFacade purchaseFacade;
    private IUserFacade userFacade;

    public StoreController(IRepository<Long, Store> storesRepo, IPurchaseFacade purchaseFacade, IUserFacade userFacade) {
        this.storesRepo = storesRepo;
        this.idCounter = 0;
        this.purchaseFacade = purchaseFacade;
        this.userFacade = userFacade;
    }

    private synchronized long generateStoreId(){
        idCounter++;
        return idCounter;
    }

    @Override
    public void createStore(String founderId, String storeName, String storeDescription, IRepository<Long, Item.Discount> discounts) {
        //TODO: check the user is registered
        long storeId = generateStoreId();
        Store newStore = new Store(storeId, founderId, storeName, storeDescription, discounts);
        storesRepo.save(newStore);
    }

    @Override
    public List<String> viewInventoryByStoreOwner(String userId, long storeId) {
        //TODO: check the user is the store owner- decide if he must be store owner
        Store store = storesRepo.findById(storeId);
        List<Item> inventory = store.viewInventory();
        List<String> itemsInfo = new ArrayList<>();
        for( Item item: inventory){
            itemsInfo.add(item.getName());
        }
        return itemsInfo;
    }

    @Override
    public void addItemToStore(String userId, long storeId, String itemName, double itemPrice, int stockAmount, long categoryId) {
        //TODO: check the user is the store owner
        Store store = storesRepo.findById(storeId);
        store.addItem(itemName, itemPrice, stockAmount, categoryId);
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
    public void viewPurchaseHistory(String userId, long storeId) {
        //TODO: check the user is the store owner
        purchaseFacade.getPurchaseByStore(storeId);
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
    public boolean addItemToShoppingBasket(ShoppingBasket basket, long storeId, long itemId, int quantity) {
        Store store = storesRepo.findById(storeId);
        boolean isAvailable = store.isItemsAvailable(itemId, quantity);
        if(isAvailable) {
            basket.addItem(itemId, quantity);
            return true;
        }
        return false;
    }

    public void purchaseOccurs(){
        List<ItemDTO> purchasedItems = purchaseFacade.getPurchasedItems();
        for (ItemDTO item: purchasedItems){
            Store store = storesRepo.findById(item.getStoreId());
            store.removeFromInventory(item.getItemId(), item.getQuantity());
        }
    }
}
