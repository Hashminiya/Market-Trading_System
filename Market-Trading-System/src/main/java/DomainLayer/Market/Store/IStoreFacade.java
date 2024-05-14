package DomainLayer.Market.Store;


import DomainLayer.Market.IRepository;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.Store;

public interface IStoreFacade {

    public static IStoreFacade create(IRepository<Long, Store> storesRepo) {
        return new StoreController(storesRepo);
    }
    public void createStore(long founderId, String storeName, String storeDescription, IRepository<Long, Item.Discount> discounts, IRepository<Long, IProduct> products);
    public void viewInventoryByStoreOwner(long userId, long storeId);
    public void addItemToStore(long userId, long storeId, String itemName, double itemPrice, int stockAmount, long categoryId);
    public void updateItem(long userId, long storeId, long itemId, String newName, double newPrice, int stockAmount);//25
    public void deleteItem(long userId, long storeId, long itemId);
    public void changeStorePolicy(long userId, long storeId);
    public void changeDiscountType(long userId, long storeId, String newType);
    public void assignStoreOwner(long actorId, long userId);
    public void assignStoreManager(long actorId, long userId);
    public void removeStore(long userId, long storeId);
    public void viewStoreManagementInfo(long userId, long storeId);
    public void viewPurchaseHistory(long userId, long storeId);
    public void getAllProductsInfoByStore(long storeId);
    public void getAllStoreInfo(long storeId);
    public void searchStoreByName(String name);
    public void searchItemByName(String name);
    public void searchStoreByCategory(long category);
    public void searchItemByCategory(long category);
    public void searchStoreByKeyWord(String keyWord);
    public void searchItemByKeyWord(String keyWord);
    public void addItemToShoppingBasket(ShoppingBasket basket, long userId, long storeId, long itemId);
}
