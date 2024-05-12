package DomainLayer.Market.Store;

public class StoreController implements IStoreFacade{

    @Override
    public void createStore(long founderId, String storeName, String storeDescription) {

    }

    @Override
    public void viewInventoryByStoreOwner(long userId, long storeId) {

    }

    @Override
    public void addItemToStore(long userId, long storeId, String itemName, double itemPrice, int stockAmount, long categoryId) {

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
    public void addItemToShoppingBasket(long userId, long storeId, long itemId) {

    }
}
