package DomainLayer.Market.Store;


public interface IStoreFacade {
    public static IStoreFacade create() {
        return new StoreController();
    }
    public void createStore(long founderId, String storeName, String storeDescription);
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
    public void searchStoreByName(String name);
    public void searchItemByName(String name);
    public void searchStoreByCategory(long category);
    public void searchItemByCategory(long category);
    public void searchStoreByKeyWord(String keyWord);
    public void searchItemByKeyWord(String keyWord);
    public void addItemToShoppingBasket(long userId, long storeId, long itemId);
}
