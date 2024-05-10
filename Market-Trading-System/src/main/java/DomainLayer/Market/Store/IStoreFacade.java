package DomainLayer.Market.Store;

import java.util.List;

public interface IStoreFacade {
    public void createStore(long founderId, String storeName, String storeDescription);
    public void viewInventory(long storeId);
    public void addItemToStore(long storeId, String itemName, double itemPrice, int stockAmount, List<String> categoryChain);
    public void updateItemName(long storeId, long itemId, String newName);
    public void updateItemPrice(long storeId, long  itemId, double newPrice);
    public void decreaseItemAmount(long storeId, long itemId, int count);
    public void deleteItem(long storeId, long itemId);
    public void changeStorePolicy(long storeId);
    public void changeDiscountType(long storeId, String newType);
    public void removeStore(long storeId);
    public void viewPurchaseHistory(long storeId);
    public void getAllProductsInfoByStore(long storeId);
    public void searchStoreByKeyWord(String keyWord);
    public void searchItemByKeyWord(String keyWord);
    public void searchStoreByCategory(String category);
    public void searchItemByCategory(String category);
    public void searchStoreByName(String name);
    public void searchItemByName(String name);
    public void assignStoreOwner(long userId);
    public void assignStoreManager(long userId);
}
