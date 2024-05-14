package DomainLayer.Market.Store;

import DomainLayer.Market.IRepository;
import DomainLayer.Market.InMemoryRepository;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.User.IUserFacade;

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
        //TODO: assign the user as owner
        //TODO: set the store description
        storesRepo.save(newStore);
    }

    @Override
    public void viewInventoryByStoreOwner(long userId, long storeId) {
        //TODO: check the user is the store owner- decide if he must be store owner
        Store store = storesRepo.findById(storeId);
        //TODO: store.viewInventory()
    }

    @Override
    public void addItemToStore(long userId, long storeId, String itemName, double itemPrice, int stockAmount, long categoryId) {
        //TODO: check the user is the store owner
        Store store = storesRepo.findById(storeId);
        store.addProduct(itemName, itemPrice, stockAmount, categoryId);
    }

    @Override
    public void updateItem(long userId, long storeId, long itemId, String newName, double newPrice, int stockAmount) {

    }

    @Override
    public void deleteItem(long userId, long storeId, long itemId) {

    }

    @Override
    public void changeStorePolicy(long userId, long storeId) {

    }

    @Override
    public void changeDiscountType(long userId, long storeId, String newType) {

    }

    @Override
    public void assignStoreOwner(long actorId, long userId) {

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
    public void getAllProductsInfoByStore(long storeId) {

    }

    @Override
    public void getAllStoreInfo(long storeId) {

    }

    @Override
    public void searchStoreByName(String name) {

    }

    @Override
    public void searchItemByName(String name) {

    }

    @Override
    public void searchStoreByCategory(long category) {

    }

    @Override
    public void searchItemByCategory(long category) {

    }

    @Override
    public void searchStoreByKeyWord(String keyWord) {

    }

    @Override
    public void searchItemByKeyWord(String keyWord) {

    }

    @Override
    public void addItemToShoppingBasket(ShoppingBasket basket, long userId, long storeId, long itemId) {

    }
}
