package DomainLayer.Market.Store;


import DomainLayer.Market.IRepository;
import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Store.Store;

import java.util.HashMap;

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
    public HashMap<Long, HashMap<String, String>> getAllProductsInfoByStore(long storeId);
    public HashMap<Long, HashMap<String, String>> getAllStoreInfo(long storeId);
    public HashMap<Long,String> searchStoreByName(String name);
    public HashMap<Long,String> searchItemByName(String name);
    public HashMap<Long,String> searchStoreByCategory(long category);
    public HashMap<Long,String> searchItemByCategory(long category);
    public HashMap<Long,String> searchStoreByKeyWord(String keyWord);
    public HashMap<Long,String> searchItemByKeyWord(String keyWord);
    public void addItemToShoppingBasket(long userId, long storeId, long itemId);
}
